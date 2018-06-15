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

package com.raulh82vlc.functionalkotlinarrow.data.cache

import com.raulh82vlc.functionalkotlinarrow.data.cache.model.FeedItemCacheModel
import java.util.ArrayList
import java.util.HashMap

/**
 * Cache data source implementation {@link CacheDataSource} where the repo has its data saved
 *
 * @author Raul Hernandez Lopez
 */
object CacheDataSourceImpl : CacheDataSource {

    private val listOfItems = ArrayList<FeedItemCacheModel>()
    private val dictionaryOfItems = HashMap<String, FeedItemCacheModel>()

    override fun getFeed(): List<FeedItemCacheModel> = listOfItems

    override fun saveFeed(itemCacheModels: List<FeedItemCacheModel>) {
        listOfItems.clear()
        dictionaryOfItems.clear()
        for (item in itemCacheModels) {
            listOfItems.add(item)
            dictionaryOfItems[item.title + item.authorId] = item
        }
    }

    override fun isEmpty() = listOfItems.isEmpty()

    override fun getItemFromFeed(title: String, authorId: String): FeedItemCacheModel?
            = dictionaryOfItems[title + authorId]

}