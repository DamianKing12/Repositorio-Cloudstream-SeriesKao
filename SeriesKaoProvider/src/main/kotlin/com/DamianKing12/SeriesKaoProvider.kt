package com.DamianKing12
//HI David
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.app

class SeriesKaoProvider : MainAPI() {
    override var name = "SeriesKao Indexer"
    override var mainUrl = "https://serieskao.top"
    override var supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "es"
    override val hasMainPage = true

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=$query"
        val doc = app.get(url).document

        return doc.select("div.result-item").mapNotNull {
            val title = it.selectFirst("div.title a")?.text() ?: return@mapNotNull null
            val href = it.selectFirst("div.title a")?.attr("href") ?: ""
            val poster = it.selectFirst("img")?.attr("src")
            val type = if (href.contains("/series/")) TvType.TvSeries else TvType.Movie

            newMovieSearchResponse(title, href, type) {
                this.posterUrl = fixUrl(poster)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document
        val title = doc.selectFirst("h1")?.text()?.trim() ?: ""
        val poster = doc.selectFirst("div.poster img")?.attr("src")
        val plot = doc.selectFirst("div.wp-content p")?.text()

        val episodes = doc.select("ul.episodios li").mapNotNull {
            val epHref = it.selectFirst("a")?.attr("href") ?: return@mapNotNull null
            val epTitle = it.selectFirst("div.episodiotitle a")?.text() ?: ""
            newEpisode(epHref) { this.name = epTitle }
        }

        return if (episodes.isEmpty()) {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = fixUrl(poster)
                this.plot = plot
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = fixUrl(poster)
                this.plot = plot
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data).document
        doc.select("iframe").amap {
            var iframeUrl = it.attr("src")
            if (iframeUrl.startsWith("//")) iframeUrl = "https:$iframeUrl"
            loadExtractor(iframeUrl, data, subtitleCallback, callback)
        }
        return true
    }
}