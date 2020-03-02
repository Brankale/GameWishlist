package com.fermimn.gamewishlist.models

open class GamePreview (val id: Int) {

    var title: String? = null
    var platform: String? = null
    var publisher: String? = null
    var cover: String? = null
    private val prices: Price = Price()      // a game can exist without a price

    var newPrice: Float?
        get() = prices.new
        set(value) { prices.new = value }

    var usedPrice: Float?
        get() = prices.used
        set(value) { prices.used = value }

    var digitalPrice: Float?
        get() = prices.digital
        set(value) { prices.digital = value }

    var preorderPrice: Float?
        get() = prices.preorder
        set(value) { prices.preorder = value }

    val oldNewPrices: ArrayList<Float>
        get() = prices.oldNew

    val oldUsedPrices: ArrayList<Float>
        get() = prices.oldUsed

    val oldDigitalPrices: ArrayList<Float>
        get() = prices.oldDigital

    val oldPreorderPrices: ArrayList<Float>
        get() = prices.oldPreorder

    var newAvailable: Boolean
        get() = prices.newAvailable
        set(value) { prices.newAvailable = value }

    var usedAvailable: Boolean
        get() = prices.usedAvailable
        set(value) { prices.usedAvailable = value }

    var digitalAvailable: Boolean
        get() = prices.digitalAvailable
        set(value) { prices.digitalAvailable = value }

    var preorderAvailable: Boolean
        get() = prices.preorderAvailable
        set(value) { prices.preorderAvailable = value }

    fun addOldNewPrice(price: Float) {
        prices.addOldNew(price)
    }

    fun addOldUsedPrice(price: Float) {
        prices.addOldUsed(price)
    }

    fun addOldDigitalPrice(price: Float) {
        prices.addOldDigital(price)
    }

    fun addOldPreorderPrice(price: Float) {
        prices.addOldPreorder(price)
    }

    fun addOldNewPrices(prices: ArrayList<Float>) {
        this.prices.addOldNew(prices)
    }

    fun addOldUsedPrices(prices: ArrayList<Float>) {
        this.prices.addOldUsed(prices)
    }

    fun addOldDigitalPrices(prices: ArrayList<Float>) {
        this.prices.addOldDigital(prices)
    }

    fun addOldPreorderPrices(prices: ArrayList<Float>) {
        this.prices.addOldPreorder(prices)
    }

    final override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (other !is GamePreview)
            return false

        if (id != other.id)
            return false

        return true
    }

    final override fun hashCode(): Int = id

}