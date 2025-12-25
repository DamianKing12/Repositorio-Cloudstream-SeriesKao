package com.DamianKing12

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.safeApiCall

class SeriesKaoProvider : MainAPI() {
    override var name = "SeriesKao"
    override var mainUrl = "https://serieskao.tv"
    override var supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        return safeApiCall {
            val document = app.get(data).document
            val iframeUrl = document.selectFirst("iframe[src*=/embed/]")?.attr("src")
                ?: return@safeApiCall false

            val iframeResponse = app.get(iframeUrl, referer = data).text

            // Extracción de tokens con Regex
            val fileCode = Regex("file_code\\s*:\\s*['\"]([^'\"]+)['\"]").find(iframeResponse)?.groupValues?.get(1)
                ?: return@safeApiCall false
            val hash = Regex("hash\\s*:\\s*['\"]([^'\"]+)['\"]").find(iframeResponse)?.groupValues?.get(1)
                ?: return@safeApiCall false
            val token = Regex("t\\s*=\\s*([^&'\"]+)").find(iframeResponse)?.groupValues?.get(1)
            val session = Regex("s\\s*=\\s*(\\d+)").find(iframeResponse)?.groupValues?.get(1)
            
            // Carpeta dinámica
            val folderId = Regex("/(\\d{5})/${fileCode}_").find(iframeResponse)?.groupValues?.get(1) ?: "06438"

            // Validación Handshake (DL)
            app.get(
                "https://callistanise.com/dl",
                params = mapOf(
                    "op" to "view",
                    "file_code" to fileCode,
                    "hash" to hash,
                    "embed" to "1"
                ),
                referer = iframeUrl
            )

            // Construcción de URL final
            val finalUrl = "https://hgc0uswxhnn8.acek-cdn.com/hls2/01/$folderId/${fileCode}_,l,n,h,.urlset/master.m3u8?t=$token&s=$session"

            // Uso de newExtractorLink siguiendo la Regla #2 (Detección automática de HLS)
            callback(
                newExtractorLink(
                    source = this.name,
                    name = this.name,
                    url = finalUrl
                ) {
                    referer = "https://callistanise.com/"
                    quality = Qualities.P1080.value
                }
            )
            true
        } ?: false
    }
}
