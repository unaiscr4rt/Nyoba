package com.Aoneroom

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class AoneroomProvider : MainAPI() {
    override var mainUrl = "https://movieboxhd.net"
    override var name = "Aoneroom"
    override var lang = "id"

    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries
    )

    override val hasMainPage = true

    override val mainPage = mainPageOf(
        "$mainUrl/id/ranking-list/8WtKSYJVEb1?id=997144265920760504&page_from=more_SUBJECTS_MOVIE" to "Popular Movie",
        "$mainUrl/id/ranking-list/eJvhCT8w3t1?id=1232643093049001320&page_from=more_SUBJECTS_MOVIE" to "Popular Series"
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val url = if (page <= 1) {
            request.data
        } else {
            request.data.replace(
                Regex("([?&])page=\\d+"),
                "$1page=$page"
            ).let {
                if (it.contains(Regex("[?&]page="))) it
                else "$it&page=$page"
            }
        }

        val document = app.get(url).document
        val results = document.select("a[href]")
            .mapNotNull { item ->
                val link = item.attr("abs:href")
                val image = item.selectFirst("img")
                val title = image?.attr("alt")
                    ?.takeIf { it.isNotBlank() }
                    ?: item.text().trim()

                val poster = image?.let {
                    it.attr("data-src")
                        .ifBlank { it.attr("src") }
                        .ifBlank { it.attr("data-original") }
                }

                if (
                    title.isBlank() ||
                    poster.isNullOrBlank() ||
                    link.isBlank() ||
                    link == mainUrl ||
                    !link.contains("movieboxhd.net")
                ) {
                    null
                } else {
                    newMovieSearchResponse(
                        title,
                        link,
                        if (request.name.contains("Series", true))
                            TvType.TvSeries
                        else
                            TvType.Movie
                    ) {
                        posterUrl = poster
                    }
                }
            }
            .distinctBy { it.url }
            .take(30)

        return newHomePageResponse(
            request.name,
            results,
            hasNext = results.isNotEmpty()
        )
    }
}
