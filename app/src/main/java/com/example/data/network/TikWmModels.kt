package com.example.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TikWmResponse(
    @Json(name = "code") val code: Int,
    @Json(name = "msg") val msg: String,
    @Json(name = "data") val data: TikWmData?
)

@JsonClass(generateAdapter = true)
data class TikWmData(
    @Json(name = "id") val id: String,
    @Json(name = "region") val region: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "cover") val cover: String?,
    @Json(name = "duration") val duration: Int?,
    @Json(name = "play") val play: String?,       // Video without watermark
    @Json(name = "wmplay") val wmplay: String?,   // Video with watermark
    @Json(name = "hdplay") val hdplay: String?,   // HD Video without watermark
    @Json(name = "size") val size: Long?,
    @Json(name = "wm_size") val wmSize: Long?,
    @Json(name = "hd_size") val hdSize: Long?,
    @Json(name = "music") val musicUrl: String?,
    @Json(name = "music_info") val musicInfo: TikWmMusicInfo?,
    @Json(name = "author") val author: TikWmAuthor?,
    @Json(name = "digg_count") val diggCount: Int?,
    @Json(name = "comment_count") val commentCount: Int?,
    @Json(name = "share_count") val shareCount: Int?,
    @Json(name = "play_count") val playCount: Int?,
    @Json(name = "images") val images: List<String>?
)

@JsonClass(generateAdapter = true)
data class TikWmMusicInfo(
    @Json(name = "id") val id: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "play") val playUrl: String?,
    @Json(name = "author") val author: String?,
    @Json(name = "cover") val coverUrl: String?
)

@JsonClass(generateAdapter = true)
data class TikWmAuthor(
    @Json(name = "id") val id: String?,
    @Json(name = "unique_id") val uniqueId: String?,
    @Json(name = "nickname") val nickname: String?,
    @Json(name = "avatar") val avatarUrl: String?
)
