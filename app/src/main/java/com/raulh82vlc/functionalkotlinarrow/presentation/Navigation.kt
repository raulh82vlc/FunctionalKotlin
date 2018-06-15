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
import arrow.effects.applicative
import arrow.effects.fix

object Navigation {
    fun goToFeedDetail(ctx: Context, feedId: String): IO<Unit> =
            IO.applicative().just(launch(ctx, feedId)).fix()

    private fun launch(ctx: Context, feedId: String) {
//        val intent = Intent(ctx, FeedDetailActivity::class.java)
//        intent.putExtra(
//                EXTRA_FEED_ID, feedId)
//        ctx.startActivity(intent)
    }
}