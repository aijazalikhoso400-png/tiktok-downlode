package com.example.data.repository

import com.example.data.local.DownloadHistoryDao
import com.example.data.model.DownloadHistory
import com.example.data.network.TikWmApiService
import com.example.data.network.TikWmResponse
import kotlinx.coroutines.flow.Flow

class TikTokRepository(
    private val apiService: TikWmApiService,
    private val historyDao: DownloadHistoryDao
) {
    val allHistory: Flow<List<DownloadHistory>> = historyDao.getAllHistory()

    suspend fun fetchVideoDetails(url: String): TikWmResponse {
        return apiService.fetchVideoDetails(url)
    }

    suspend fun saveToHistory(history: DownloadHistory): Long {
        return historyDao.insertHistory(history)
    }

    suspend fun updateHistory(history: DownloadHistory) {
        historyDao.updateHistory(history)
    }

    suspend fun deleteHistoryItem(id: Int) {
        historyDao.deleteById(id)
    }

    suspend fun getHistoryByTiktokId(tiktokId: String): DownloadHistory? {
        return historyDao.getHistoryByTiktokId(tiktokId)
    }
}
