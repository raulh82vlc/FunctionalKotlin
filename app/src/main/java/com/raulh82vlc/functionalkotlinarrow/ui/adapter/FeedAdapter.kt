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

package com.raulh82vlc.functionalkotlinarrow.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raulh82vlc.functionalkotlinarrow.R
import com.raulh82vlc.functionalkotlinarrow.data.cache.model.FeedItemCacheModel
import kotlinx.android.synthetic.main.item_list.view.*

class FeedAdapter(
        var feedList: List<FeedItemCacheModel> = ArrayList(),
        val itemClick: (FeedItemCacheModel) -> Unit) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return FeedViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, pos: Int) {
        holder.bind(feedList[pos])
    }

    override fun getItemCount() = feedList.size

    fun fillData(feed: List<FeedItemCacheModel>) {
        feedList = feed
        notifyDataSetChanged()
    }

    class FeedViewHolder(view: View,
                     val itemClick: (FeedItemCacheModel) -> Unit) : RecyclerView.ViewHolder(
            view) {

        fun bind(itemFeed: FeedItemCacheModel) {
            with(itemFeed) {
                com.squareup.picasso.Picasso.with(itemView.context)
                        .load(itemFeed.media.url).into(itemView.iv_image)
                itemView.txt_title.text = itemFeed.title
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}