package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DownloadHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadHistoryDao {
    @Query("SELECT * FROM download_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<DownloadHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: DownloadHistory): Long

    @Update
    suspend fun updateHistory(history: DownloadHistory)

    @Delete
    suspend fun deleteHistory(history: DownloadHistory)

    @Query("DELETE FROM download_history WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM download_history WHERE tiktokId = :tiktokId LIMIT 1")
    suspend fun getHistoryByTiktokId(tiktokId: String): DownloadHistory?
}
