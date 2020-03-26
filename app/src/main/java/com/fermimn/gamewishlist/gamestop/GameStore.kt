package com.fermimn.gamewishlist.gamestop

import com.fermimn.gamewishlist.models.Game
import com.fermimn.gamewishlist.models.GamePreviews

interface GameStore {

    fun search(game: String) : GamePreviews
    fun getGame(id: Int) : Game

}