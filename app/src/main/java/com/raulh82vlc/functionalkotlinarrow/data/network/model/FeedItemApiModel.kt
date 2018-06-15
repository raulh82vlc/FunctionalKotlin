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

package com.raulh82vlc.functionalkotlinarrow.data.network.model

import com.google.gson.annotations.SerializedName

data class FeedItemApiModel(
        @SerializedName("title") val title: String,
        @SerializedName("link") val link: String,
        @SerializedName("media") val media: MediaItem,
        @SerializedName("description") val description: String,
        @SerializedName("date_taken") val dateTaken: String,
        @SerializedName("published") val published: String,
        @SerializedName("author") val author: String,
        @SerializedName("author_id") val authorId: String,
        @SerializedName("tags") val tags: String) {

    data class MediaItem(@SerializedName("m") val url: String)
}