package com.fermimn.gamewishlist.models

import androidx.recyclerview.widget.DiffUtil

class GamePreviewDiffUtilCallback(
        private val oldGamePreviews: GamePreviews,
        private val newGamePreviews: GamePreviews
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldGamePreviews.size
    override fun getNewListSize(): Int = newGamePreviews.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldGamePreviews[oldItemPosition].id == newGamePreviews[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGamePreview = oldGamePreviews[oldItemPosition]
        val newGamePreview = newGamePreviews[newItemPosition]

        return oldGamePreview.title == newGamePreview.title &&
                oldGamePreview.cover == newGamePreview.cover &&
                oldGamePreview.platform == newGamePreview.platform &&
                oldGamePreview.publisher == newGamePreview.publisher &&
                oldGamePreview.newPrice == newGamePreview.newPrice &&
                oldGamePreview.usedPrice == newGamePreview.usedPrice &&
                oldGamePreview.digitalPrice == newGamePreview.digitalPrice &&
                oldGamePreview.preorderPrice == newGamePreview.preorderPrice &&
                oldGamePreview.newAvailable == newGamePreview.newAvailable &&
                oldGamePreview.usedAvailable == newGamePreview.usedAvailable &&
                oldGamePreview.digitalAvailable == newGamePreview.digitalAvailable &&
                oldGamePreview.preorderAvailable == newGamePreview.preorderAvailable &&
                oldGamePreview.oldNewPrices == newGamePreview.oldNewPrices &&
                oldGamePreview.oldUsedPrices == newGamePreview.oldUsedPrices &&
                oldGamePreview.oldDigitalPrices == newGamePreview.oldDigitalPrices &&
                oldGamePreview.oldPreorderPrices == newGamePreview.oldPreorderPrices
    }

}