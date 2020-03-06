package com.fermimn.gamewishlist.models

class Game(id: Int) : GamePreview(id) {

    var players: String? = null
    var releaseDate: String? = null
    var description: String? = null
    var website: String? = null
    var promos: ArrayList<Promo>? = null
    var genres: ArrayList<String>? = null
    var pegi: ArrayList<String>? = null
    var gallery: ArrayList<String>? = null
    var validForPromo: Boolean = false

    fun addGenre(genre: String) {
        genres = genres ?: ArrayList()
        genres?.add(genre)
    }

    fun addPromo(promo: Promo) {
        promos = promos ?: ArrayList()
        promos?.add(promo)
    }

    fun addPegi(pegiType: String) {
        pegi = pegi ?: ArrayList()
        pegi?.add(pegiType)
    }

    fun addImage(image: String) {
        gallery = gallery ?: ArrayList()
        gallery?.add(image)
    }

}