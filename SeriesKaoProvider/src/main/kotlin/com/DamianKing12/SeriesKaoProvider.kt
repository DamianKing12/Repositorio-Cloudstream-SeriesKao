override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data, headers = headers).document

        // 1. Subtítulos
        doc.select("track[kind=subtitles]").forEach { track ->
            val src = track.attr("src")
            if (src.isNotBlank()) {
                subtitleCallback(
                    SubtitleFile(
                        src,
                        track.attr("srclang") ?: "es"
                    )
                )
            }
        }

        // 2. Extracción de IFRAMES
        doc.select("iframe").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotBlank()) {
                callback(
                    ExtractorLink(
                        source = "SeriesKao",
                        name = "Enlace Externo",
                        url = src,
                        referer = mainUrl,
                        quality = Qualities.Unknown.value,
                        isM3u8 = src.contains(".m3u8", ignoreCase = true)
                    )
                )
            }
        }

        // 3. Extracción de Servidores desde JSON
        val scriptElement = doc.selectFirst("script:containsData(var servers =)")
        if (scriptElement != null) {
            val serversJson = scriptElement.data().substringAfter("var servers = ").substringBefore(";").trim()
            try {
                val servers = parseJson<List<ServerData>>(serversJson)
                servers.forEach { server ->
                    val cleanUrl = server.url.replace("\\/", "/")
                    callback(
                        ExtractorLink(
                            source = server.title,
                            name = server.title,
                            url = cleanUrl,
                            referer = mainUrl,
                            quality = getQuality(server.title),
                            isM3u8 = cleanUrl.contains(".m3u8", ignoreCase = true)
                        )
                    )
                }
            } catch (e: Exception) { e.printStackTrace() }
        }

        return true
    }
