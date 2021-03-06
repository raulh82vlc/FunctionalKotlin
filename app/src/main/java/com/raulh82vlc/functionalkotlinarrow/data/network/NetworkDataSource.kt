/*
 * Copyright (C) 2018 Raul Hernandez Lopez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.raulh82vlc.functionalkotlinarrow.data.network

import arrow.Kind
import arrow.core.*
import arrow.effects.IO
import arrow.effects.async
import arrow.effects.fix
import arrow.effects.monadError
import arrow.effects.typeclasses.Async
import arrow.typeclasses.binding
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.raulh82vlc.functionalkotlinarrow.data.cache.model.FeedItemCacheModel
import com.raulh82vlc.functionalkotlinarrow.data.map
import com.raulh82vlc.functionalkotlinarrow.data.network.error.ApiException
import com.raulh82vlc.functionalkotlinarrow.data.network.model.FeedApiModel
import kotlinx.coroutines.experimental.CommonPool
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.experimental.async as asyncCoroutines

/**
 * Data source responsible of network requests
 */
object NetworkDataSource {

    private const val JSON_FLICKR_FEED_LITERAL = "jsonFlickrFeed("
    private const val JSON_FLICKR_FEED_KEYWORD = "jsonFlickrFeed\\("
    private const val API_HOST = "https://api.flickr.com/services/"

    private val gson = Gson()

    private val retrofit = Retrofit.Builder()
            .baseUrl(API_HOST)
            .client(createOkHttp())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(FeedApi::class.java)

    private fun createOkHttp(): OkHttpClient =
            OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()

    fun requestFeed(format: String): IO<List<FeedItemCacheModel>> {
        val monadError = IO.monadError()
        return monadError.binding {
            val result = runInAsyncContext(
                    f = { transformToRepository(retrofit.getFeed(format)) },
                    onError = { monadError.raiseError<List<FeedItemCacheModel>>(it) },
                    onSuccess = { monadError.just(it) },
                    AC = IO.async()
            ).bind()
            result.bind()
        }.fix()
    }

    private fun transformToRepository(feed: Call<ResponseBody>) =
            execute(feed).toOption()
                .map { it.string() }
                .filter { hasFeedFormat(it) }
                .map { map(deserializeFeedJSON(extractJSONFromResponse(it)).feedItems) }
                .getOrElse { emptyList() }

    private fun hasFeedFormat(responseBodyResult: String): Boolean =
        responseBodyResult.contains(JSON_FLICKR_FEED_LITERAL)

    private fun extractJSONFromResponse(response: String) : String {
        val responseOutput = response.replaceFirst(JSON_FLICKR_FEED_KEYWORD.toRegex(), "")
        return responseOutput.substring(0, responseOutput.length - 1)
    }

    private fun deserializeFeedJSON(responseJson: String) =
            gson.fromJson(responseJson, FeedApiModel::class.java)

    /**
     * Run in Async context a Coroutine
     * Thanks to Jorge Castillo for this method
     * package com.github.jorgecastillo.kotlinandroid.io.algebras.persistence
     */
    private fun <F, A, B> runInAsyncContext(
            f: () -> A,
            onError: (Throwable) -> B,
            onSuccess: (A) -> B, AC: Async<F>): Kind<F, B> {
        return AC.async { proc ->
            asyncCoroutines(CommonPool) {
                val result = Try { f() }.fold(onError, onSuccess)
                proc(result.right())
            }
        }
    }

    @Throws(ApiException::class)
    fun execute(call: Call<ResponseBody>): ResponseBody? {
        var response: Response<ResponseBody>?
        try {
            response = call.execute()
        } catch (e: IOException) {
            throw ApiException("Network error", e)
        }

        if (response != null && response.isSuccessful) {
            return response.body()
        } else {
            throw ApiException(response.errorBody().toString(), null)
        }
    }
}
