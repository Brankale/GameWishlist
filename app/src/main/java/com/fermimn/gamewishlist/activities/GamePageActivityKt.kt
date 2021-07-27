package com.fermimn.gamewishlist.activities

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.databinding.ActivityGamePageBinding
import com.fermimn.gamewishlist.gamestop.GameStop
import com.fermimn.gamewishlist.gamestop.GameStop.Companion.getGamePageUrl
import com.fermimn.gamewishlist.models.Game
import com.fermimn.gamewishlist.models.GamePreview
import com.fermimn.gamewishlist.models.GamePreviews
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel

class GamePageActivityKt : AppCompatActivity() {

    companion object {
        val TAG: String = GamePageActivityKt::class.java.simpleName
    }

    private lateinit var binding: ActivityGamePageBinding

    private lateinit var wishlistViewModel: WishlistViewModel
    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.actionBar)

        wishlistViewModel = ViewModelProvider(this).get(WishlistViewModel::class.java)

        intent?.apply {
            val gameId: Int = getIntExtra("gameID", 0)
            Log.d(TAG, "Received gameId: $gameId")

            game = wishlistViewModel.getGame(gameId)
        }
    }

    override fun onStart() {
        super.onStart()
        if (game == null)
            Log.wtf(TAG, "empty game page: game was not set")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_game_page, menu)

        val wishlist: GamePreviews? = wishlistViewModel.wishlist.value

        game?.let {
            title = it.title

            if (wishlist?.contains(it) == true) {
                // show add button if the game is not in the wishlist
                menu?.findItem(R.id.action_add_game)?.isVisible = false
                menu?.findItem(R.id.action_remove_game)?.isVisible = true
            } else {
                // show remove button if the game is already in the wishlist
                menu?.findItem(R.id.action_add_game)?.isVisible = true
                menu?.findItem(R.id.action_remove_game)?.isVisible = false
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> { openSettings(); true }
            R.id.action_add_game -> { showAddGameDialog(); true }
            R.id.action_remove_game -> { showRemoveGameDialog(); true }
            R.id.action_open_website -> { openWebsite(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun showAddGameDialog() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_add_game_to_wishlist_title))
                .setMessage(getString(R.string.dialog_add_game_to_wishlist_text))
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                    game?.let { wishlistViewModel.addGame(it) }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }

    private fun showRemoveGameDialog() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_remove_game_from_wishlist_title))
                .setMessage(getString(R.string.dialog_remove_game_from_wishlist_text))
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                    game?.let { wishlistViewModel.removeGame(it.id) }
                    finish()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
    }

    private fun openWebsite() {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(getGamePageUrl(game?.id ?: 0))
        }
        startActivity(intent)
    }

    private fun showContent() {
        binding.sectionInfo.title.text = game?.title
        binding.sectionInfo.publisher.text = game?.publisher
        binding.sectionInfo.platform.text = game?.platform

        if (game?.genres != null)
            binding.sectionInfo.genres.text = getGenresString(game?.genres!!)
        else
            binding.sectionInfo.genresContainer.visibility = View.GONE

        if (game?.releaseDate != null)
            binding.sectionInfo.releaseDate.text = game?.releaseDate
        else
            binding.sectionInfo.releaseDateContainer.visibility = View.GONE

        if (game?.players != null) {
            binding.sectionInfo.players.text = game?.players
            binding.sectionInfo.playersContainer.visibility = View.VISIBLE
        }

        if (game?.website != null) {
            binding.sectionInfo.officialSite.text = getWebsiteString(game?.website!!)
            binding.sectionInfo.officialSite.movementMethod = LinkMovementMethod.getInstance()
            binding.sectionInfo.officialSite.visibility = View.VISIBLE
        }
    }

    private fun getGenresString(genres: ArrayList<String>) : String {
        val stringBuilder = StringBuilder()
        for (genre in genres) {
            stringBuilder.append(genre).append("/")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        return stringBuilder.toString()
    }

    private fun getWebsiteString(website: String) : String {
        val domain = website.split("/").toTypedArray()[2]
        val html = "<a href='$website'>$domain</a>"
        return HtmlCompat.fromHtml(html,HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }

}