package com.fermimn.gamewishlist.models

import java.lang.RuntimeException

class Promo(val header: String) {

    var text: String? = null
    var findMore: String? = null
    var findMoreUrl: String? = null     // a URL that can be associated with "findMore" var

    init {
        if (header.isEmpty()) {
            throw PromoException("empty header")
        }
    }

    fun hasFindMore(): Boolean = findMore != null

}

private class PromoException(message: String?) : RuntimeException(message)