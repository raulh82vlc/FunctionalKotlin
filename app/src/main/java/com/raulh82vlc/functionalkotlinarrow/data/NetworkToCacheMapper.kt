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

package com.raulh82vlc.functionalkotlinarrow.data

import com.raulh82vlc.functionalkotlinarrow.data.cache.model.FeedItemCacheModel
import com.raulh82vlc.functionalkotlinarrow.data.network.model.FeedItemApiModel
import java.util.*

    private const val REGEX_WHITESPACE = "\\s+"

    fun map(apiInput: List<FeedItemApiModel>): List<FeedItemCacheModel> {
        val cacheOutput = ArrayList<FeedItemCacheModel>()
        for (apiItem in apiInput) {
            cacheOutput.add(
                    FeedItemCacheModel(
                            apiItem.title,
                            apiItem.link,
                            FeedItemCacheModel.MediaItem(apiItem.media.url),
                            apiItem.description,
                            apiItem.dateTaken,
                            apiItem.published,
                            apiItem.author,
                            apiItem.authorId,
                            mapTags(apiItem.tags))
            )
        }
        return cacheOutput
    }

    private fun mapTags(tags: String): List<String> {
        val strings = tags.split(REGEX_WHITESPACE.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        // this avoids empty space passed as a value
        return if (strings.size == 1 && strings[0].isEmpty()) {
            ArrayList()
        } else Arrays.asList(*strings)
    }