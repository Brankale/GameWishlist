package com.fermimn.gamewishlist.repositories.xml

interface Xml {

    companion object {
        const val GAME = "game"
        const val TITLE = "title"
        const val PLATFORM = "platform"
        const val PUBLISHER = "publisher"
        const val PEGI = "pegi"
        const val PEGI_TYPE = "type"
        const val GENRES = "genres"
        const val GENRE = "genre"
        const val OFFICIAL_SITE = "officialSite"
        const val PLAYERS = "players"
        const val RELEASE_DATE = "releaseDate"
        const val DESCRIPTION = "description"
        const val COVER = "cover"
        const val GALLERY = "gallery"
        const val IMAGE = "image"
        const val VALID_FOR_PROMO = "validForPromo"

        const val PRICES = "prices"
        const val PRICE = "price"
        const val NEW = "newPrice"
        const val USED = "usedPrice"
        const val PREORDER = "preorderPrice"
        const val DIGITAL = "digitalPrice"
        const val OLD_NEW = "olderNewPrices"
        const val OLD_USED = "olderUsedPrices"
        const val OLD_PREORDER = "olderPreorderPrices"
        const val OLD_DIGITAL = "olderDigitalPrices"
        const val NEW_AVAILABLE = "newAvailable"
        const val USED_AVAILABLE = "usedAvailable"
        const val PREORDER_AVAILABLE = "preorderAvailable"
        const val DIGITAL_AVAILABLE = "digitalAvailable"

        const val PROMOS = "promos"
        const val PROMO = "promo"
        const val HEADER = "header"
        const val SUB_HEADER = "subHeader"
        const val FIND_MORE = "findMoreMsg"
        const val FIND_MORE_URL = "findMoreUrl"
    }

}