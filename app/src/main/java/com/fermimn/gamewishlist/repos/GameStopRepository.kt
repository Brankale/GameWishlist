package com.fermimn.gamewishlist.repos

import com.github.brankale.models.Game
import com.github.brankale.parsers.search_results.SearchResultsParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStopRepository @Inject constructor() {

    suspend fun search(game: String): List<Game> =
        withContext(Dispatchers.IO) {
            return@withContext SearchResultsParser.parse(SearchResultsParser.Companion.SearchParams(game))
        }

}