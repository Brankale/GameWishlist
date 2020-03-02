package com.fermimn.gamewishlist.models

class Price {

    var new: Float? = null
    var used: Float? = null
    var digital: Float? = null
    var preorder: Float? = null

    // TODO: try to remove setters

    var oldNew: ArrayList<Float>? = null
        set(value) {
            if (value != null && value.isNotEmpty()) {
                field = value
            }
        }

    var oldUsed: ArrayList<Float>? = null
        set(value) {
            if (value != null && value.isNotEmpty()) {
                field = value
            }
        }

    var oldDigital: ArrayList<Float>? = null
        set(value) {
            if (value != null && value.isNotEmpty()) {
                field = value
            }
        }

    var oldPreorder: ArrayList<Float>? = null
        set(value) {
            if (value != null && value.isNotEmpty()) {
                field = value
            }
        }

    var newAvailable: Boolean = false
    var usedAvailable: Boolean = false
    var digitalAvailable: Boolean = false
    var preorderAvailable: Boolean = false

    fun hasNewPrice(): Boolean = new != null
    fun hasUsedPrice(): Boolean = used != null
    fun hasDigitalPrice(): Boolean = digital != null
    fun hasPreorderPrice(): Boolean = preorder != null

    fun hasOldNewPrices(): Boolean = oldNew?.isNotEmpty() ?: false
    fun hasOldUsedPrices(): Boolean = oldUsed?.isNotEmpty() ?: false
    fun hasOldDigitalPrices(): Boolean = oldDigital?.isNotEmpty() ?: false
    fun hasOldPreorderPrices(): Boolean = oldPreorder?.isNotEmpty() ?: false

    fun addOldNew(price: Float?) {
        price?.let {
            oldNew = oldNew ?: ArrayList()
            oldNew?.add(it)
        }
    }

    fun addOldUsed(price: Float?) {
        price?.let {
            oldUsed = oldUsed ?: ArrayList()
            oldUsed?.add(it)
        }
    }

    fun addOldDigital(price: Float?) {
        price?.let {
            oldDigital = oldDigital ?: ArrayList()
            oldDigital?.add(it)
        }
    }

    fun addOldPreorder(price: Float?) {
        price?.let {
            oldPreorder = oldPreorder ?: ArrayList()
            oldPreorder?.add(it)
        }
    }

}