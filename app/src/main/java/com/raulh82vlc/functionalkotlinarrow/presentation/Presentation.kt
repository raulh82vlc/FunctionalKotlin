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

package com.raulh82vlc.functionalkotlinarrow.presentation

import android.content.Context
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.monadError
import arrow.typeclasses.bindingCatch
import com.raulh82vlc.functionalkotlinarrow.data.cache.model.FeedItemCacheModel
import com.raulh82vlc.functionalkotlinarrow.domain.FeedUseCase

interface FeedListView {
    fun showNetworkError(t: Throwable)
    fun showFeed(feed: List<FeedItemCacheModel>)
}

object Presentation {
    fun onClickFeedItem(ctx: Context, heroId: String): IO<Unit> =
            Navigation.goToFeedDetail(ctx, heroId)

    fun showFeed(view: FeedListView): IO<Unit> {
        val monadError = IO.monadError()
        return monadError.bindingCatch {
            val result = FeedUseCase.getFeed().handleError {
                displayError(view, it); emptyList()
            }.bind()

            monadError.just(view.showFeed(result)).bind()
        }.fix()
    }

    private fun displayError(view: FeedListView, t: Throwable): IO<Unit> =
            IO.monadError().just(view.showNetworkError(t)).fix()
}