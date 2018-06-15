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

interface CacheDataSource {
    /**
     * Gets a list of feed
     */
    fun getFeed(): List<FeedItemCacheModel>

    /**
     * Save feed
     * @param itemCacheModels list of items from cache
     */
    fun saveFeed(itemCacheModels: List<FeedItemCacheModel>)

    fun isEmpty(): Boolean

    /**
     * Gets a [FeedItemCacheModel] from the dictionary created once info is retrieved
     * @param title title of the feed
     * @param authorId author id
     */
    fun getItemFromFeed(title: String, authorId: String): FeedItemCacheModel?
}