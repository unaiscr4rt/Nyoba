package com.Aoneroom

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class AoneroomProvider : MainAPI() {
    override var mainUrl = "https://h5.aoneroom.com"
    override var name = "Aoneroom"
    override var lang = "id"

    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries
    )

    override val hasMainPage = true

    override val mainPage = mainPageOf(
        "https://h5.aoneroom.com/id/ranking-list/8WtKSYJVEb1?id=997144265920760504&page_from=more_SUBJECTS_MOVIE" to "Popular Movie",
        "https://h5.aoneroom.com/id/ranking-list/eJvhCT8w3t1?id=1232643093049001320&page_from=more_SUBJECTS_MOVIE" to "Popular Series"
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        return newHomePageResponse(
            request.name,
            emptyList(),
            hasNext = false
        )
    }
}
