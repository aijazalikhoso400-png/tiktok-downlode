package com.example.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.util.regex.Pattern

object TikTokUtils {

    fun extractTikTokUrl(text: String): String? {
        val urlRegex = "https?://[a-zA-Z0-9-._~:/?#\\[\\]@!$&'()*+,;=]+"
        val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            val url = matcher.group()
            if (url.contains("tiktok.com")) {
                return url
            }
        }
        return null
    }

    fun isValidTikTokUrl(url: String): Boolean {
        return url.contains("tiktok.com") && (url.startsWith("http://") || url.startsWith("https://"))
    }

    fun startDownload(
        context: Context,
        url: String,
        title: String,
        fileName: String
    ): Long {
        return try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri).apply {
                setTitle(title)
                setDescription("Downloading $fileName")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }
            Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed to start: ${e.message}", Toast.LENGTH_LONG).show()
            -1L
        }
    }
}
