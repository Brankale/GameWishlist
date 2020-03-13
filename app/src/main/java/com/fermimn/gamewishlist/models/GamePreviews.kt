package com.fermimn.gamewishlist.models

import android.util.Log

class GamePreviews : ArrayList<GamePreview>() {

    companion object {
        private val TAG: String = GamePreviews::class.java.simpleName
    }

    override fun add(element: GamePreview): Boolean {

        if (contains(element)) {
            Log.w(TAG, "you cannot add duplicate gamePreview to the list")
            return false
        }

        return super.add(element)
    }

    override fun add(index: Int, element: GamePreview) {

        if (contains(element)) {
            Log.w(TAG, "you cannot add duplicate gamePreview to the list")
            return
        }

        super.add(index, element)
    }

    override fun addAll(elements: Collection<GamePreview>): Boolean {
        return super.addAll(LinkedHashSet(elements))
    }

    override fun addAll(index: Int, elements: Collection<GamePreview>): Boolean {
        return super.addAll(index, LinkedHashSet(elements))
    }

}