package com.lagradost.cloudstream3.movieproviders // Ajusta según tu paquete

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import org.jsoup.nodes.Document

class SeriesKaoProvider : MainAPI() {
    override var name = "SeriesKao"
    override var mainUrl = "https://serieskao.tv" // Ajusta si es otra
    override var supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun loadLinks(
        data: String,
        isDataJob: Boolean,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        
        // Usamos safeApiCall para evitar que el plugin se cierre si algo falla
        return safeApiCall {
            val document = app.get(data).document
            val iframeUrl = document.select("iframe[src*=/embed/]").attr("src")
            
            if (iframeUrl.isEmpty()) return@safeApiCall false

            val iframeResponse = app.get(iframeUrl, referer = data).text

            // Extracción con Regex (usando la lógica universal que descubrimos)
            val fileCode = Regex("file_code\\s*:\\s*['\"]([^'\"]+)['\"]").find(iframeResponse)?.groupValues?.get(1)
            val hash = Regex("hash\\s*:\\s*['\"]([^'\"]+)['\"]").find(iframeResponse)?.groupValues?.get(1)
            val token = Regex("t\\s*=\\s*([^&'\"]+)").find(iframeResponse)?.groupValues?.get(1)
            val session = Regex("s\\s*=\\s*(\\d+)").find(iframeResponse)?.groupValues?.get(1)
            
            // Captura de la carpeta dinámica que cambia por película
            val folderId = Regex("/(\\d{5})/${fileCode}_").find(iframeResponse)?.groupValues?.get(1) ?: "06438"

            if (fileCode != null && hash != null) {
                // Validación obligatoria contra el servidor de descarga
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

                // Enlace maestro M3U8
                val finalUrl = "https://hgc0uswxhnn8.acek-cdn.com/hls2/01/$folderId/${fileCode}_,l,n,h,.urlset/master.m3u8?t=$token&s=$session"

                callback.invoke(
                    ExtractorLink(
                        source = this.name,
                        name = this.name,
                        url = finalUrl,
                        referer = "https://callistanise.com/",
                        quality = Qualities.P1080.value, // Ponemos 1080 por defecto, el M3U8 ajustará el resto
                        isM3u8 = true
                    )
                )
            }
            true
        } ?: false
    }
}
