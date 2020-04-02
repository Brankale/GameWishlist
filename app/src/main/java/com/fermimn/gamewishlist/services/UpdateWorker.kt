package com.fermimn.gamewishlist.services

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fermimn.gamewishlist.App
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.models.GamePreview
import com.fermimn.gamewishlist.models.GamePreviews
import com.fermimn.gamewishlist.repositories.Repository
import java.lang.StringBuilder

// TODO: handle worker cancel()
// TODO: move notification logic away
class UpdateWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        private val TAG: String = UpdateWorker::class.java.simpleName
    }

    override fun doWork(): Result {

        try {
            Log.d(TAG, "Updating games...")

            val repository = Repository.getInstance(applicationContext)
            val games = repository.wishlist.value ?: GamePreviews()

            for (outdated in games) {
                Log.d(TAG, "Updating [${outdated.id}]...")
                val updated = repository.updateGame(outdated.id)

                if (isChanged(outdated, updated)) {
                    Log.d(TAG, "[${updated.id}] has changed. Sending notification...")
                    sendNotification(outdated, updated)
                }
            }

            Log.d(TAG, "Games updated successfully")
            return Result.success()

        } catch (ex: Exception) {
            Log.e(TAG, "exception", ex)
        }

        return Result.failure()
    }

    private fun isChanged(prev: GamePreview, curr: GamePreview) : Boolean {
        // TODO: a game can be out of stock
        // TODO: a game can exit the out of stock state
        return isNewPriceLower(prev, curr) ||
                isUsedPriceLower(prev, curr) ||
                isDigitalPriceLower(prev, curr) ||
                isPreorderPriceLower(prev, curr) ||
                hasBeenReleased(prev, curr)
    }

    private fun sendNotification(prev: GamePreview, curr: GamePreview) {
        val message = createMessage(prev, curr)
        App.sendOnUpdatesChannel(applicationContext, curr, message, message)
    }

    private fun createMessage(prev: GamePreview, curr: GamePreview) : String {
        val message = StringBuilder()

        with (applicationContext) {
            if (isNewPriceLower(prev, curr)) {
                message.append(",\n")
                message.append(getString(R.string.notif_lower_new_price))
            }
            if (isUsedPriceLower(prev, curr)) {
                message.append(",\n")
                message.append(getString(R.string.notif_lower_used_price))
            }
            if (isDigitalPriceLower(prev, curr)) {
                message.append(",\n")
                message.append(getString(R.string.notif_lower_digital_price))
            }
            if (isPreorderPriceLower(prev, curr)) {
                message.append(",\n")
                message.append(getString(R.string.notif_lower_preorder_price))
            }
            if (hasBeenReleased(prev, curr)) {
                message.append(",\n")
                message.append(getString(R.string.notif_game_released))
            }
        }

        return message.substring(2)
    }

    private fun isNewPriceLower(prev: GamePreview, curr: GamePreview) : Boolean {
        return isPriceLower(prev.newPrice, curr.newPrice)
    }

    private fun isUsedPriceLower(prev: GamePreview, curr: GamePreview) : Boolean {
        return isPriceLower(prev.usedPrice, curr.usedPrice)
    }

    private fun isDigitalPriceLower(prev: GamePreview, curr: GamePreview) : Boolean {
        return isPriceLower(prev.digitalPrice, curr.digitalPrice)
    }

    private fun isPreorderPriceLower(prev: GamePreview, curr: GamePreview) : Boolean {
        return isPriceLower(prev.preorderPrice, curr.preorderPrice)
    }

    private fun isPriceLower(prevPrice: Float?, currPrice: Float?) : Boolean {
        return if (prevPrice != null && currPrice != null) currPrice < prevPrice else false
    }

    private fun hasBeenReleased(prev: GamePreview, curr: GamePreview) : Boolean {
        return prev.preorderPrice != null && curr.preorderPrice == null
    }

}