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

package com.raulh82vlc.functionalkotlinarrow.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import arrow.effects.fix
import com.raulh82vlc.functionalkotlinarrow.R
import com.raulh82vlc.functionalkotlinarrow.data.cache.model.FeedItemCacheModel
import com.raulh82vlc.functionalkotlinarrow.presentation.FeedListView
import com.raulh82vlc.functionalkotlinarrow.presentation.Presentation
import com.raulh82vlc.functionalkotlinarrow.ui.adapter.FeedAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FeedListView {
    private lateinit var feedAdapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFeedList()

        Presentation.showFeed(this).fix().unsafeRunAsync {}
    }

    private fun setFeedList() {
        feed_rv.setHasFixedSize(true)
        feed_rv.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedAdapter(itemClick = {
            Presentation.onClickFeedItem(this, it.authorId).fix().unsafeRunAsync {}
        })
        feed_rv.adapter = feedAdapter
    }

    override fun showNetworkError(t: Throwable) = runOnUiThread {
        Toast.makeText(this, t.localizedMessage, Toast.LENGTH_LONG).show()
        showNoResults()
    }

    override fun showFeed(feed: List<FeedItemCacheModel>) = runOnUiThread {
        if (feed.isEmpty()) {
            showNoResults()
        } else {
            showResults()
            feedAdapter.fillData(feed)
        }
    }

    private fun showNoResults() {
        no_results_view.visibility = View.VISIBLE
        feed_rv.visibility = View.GONE
    }

    private fun showResults() {
        no_results_view.visibility = View.GONE
        no_results_view.visibility = View.VISIBLE
    }
}
