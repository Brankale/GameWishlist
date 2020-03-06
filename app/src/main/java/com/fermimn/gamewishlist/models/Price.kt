package com.fermimn.gamewishlist.models

class Price {

    var new: Float? = null
    var used: Float? = null
    var digital: Float? = null
    var preorder: Float? = null

    val oldNew: ArrayList<Float> = ArrayList()
    val oldUsed: ArrayList<Float> = ArrayList()
    val oldDigital: ArrayList<Float> = ArrayList()
    val oldPreorder: ArrayList<Float> = ArrayList()

    var newAvailable: Boolean = false
    var usedAvailable: Boolean = false
    var digitalAvailable: Boolean = false
    var preorderAvailable: Boolean = false

    fun addOldNew(price: Float) { oldNew.add(price) }
    fun addOldUsed(price: Float) { oldUsed.add(price) }
    fun addOldDigital(price: Float) { oldDigital.add(price) }
    fun addOldPreorder(price: Float) { oldPreorder.add(price) }

    fun addOldNew(prices: ArrayList<Float>) {
        for (price in prices) {
            addOldNew(price)
        }
    }

    fun addOldUsed(prices: ArrayList<Float>) {
        for (price in prices) {
            addOldUsed(price)
        }
    }

    fun addOldDigital(prices: ArrayList<Float>) {
        for (price in prices) {
            addOldDigital(price)
        }
    }

    fun addOldPreorder(prices: ArrayList<Float>) {
        for (price in prices) {
            addOldPreorder(price)
        }
    }

}