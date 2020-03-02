package com.fermimn.gamewishlist.models

class Price {

    var new: Float? = null
    var used: Float? = null
    var digital: Float? = null
    var preorder: Float? = null

    val oldNew: ArrayList<Float> by lazy { ArrayList<Float>() }
    val oldUsed: ArrayList<Float> by lazy { ArrayList<Float>() }
    val oldDigital: ArrayList<Float> by lazy { ArrayList<Float>() }
    val oldPreorder: ArrayList<Float> by lazy { ArrayList<Float>() }

    var newAvailable: Boolean = false
    var usedAvailable: Boolean = false
    var digitalAvailable: Boolean = false
    var preorderAvailable: Boolean = false

    fun hasNewPrice(): Boolean = new != null
    fun hasUsedPrice(): Boolean = used != null
    fun hasDigitalPrice(): Boolean = digital != null
    fun hasPreorderPrice(): Boolean = preorder != null

    fun hasOldNewPrices(): Boolean = oldNew.isNotEmpty()
    fun hasOldUsedPrices(): Boolean = oldUsed.isNotEmpty()
    fun hasOldDigitalPrices(): Boolean = oldDigital.isNotEmpty()
    fun hasOldPreorderPrices(): Boolean = oldPreorder.isNotEmpty()

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