package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_history")
data class DownloadHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tiktokId: String,
    val title: String,
    val authorUsername: String,
    val authorNickname: String,
    val authorAvatarUrl: String,
    val coverUrl: String,
    val videoUrl: String,
    val musicUrl: String,
    val duration: Int,
    val size: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val localVideoPath: String? = null,
    val localMusicPath: String? = null
)
