package com.fermimn.gamewishlist.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.databinding.ActivityMainBinding
import com.fermimn.gamewishlist.fragments.SearchFragment
import com.fermimn.gamewishlist.fragments.WishlistFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var sectionSearch: SearchFragment
    private lateinit var sectionWishlist: WishlistFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val icon = AppCompatResources.getDrawable(this, R.drawable.ic_search_black_24dp)
        binding.switchButton.setImageDrawable(icon)
        setListeners()

        // set action bar
        // DOCS: https://developer.android.com/training/appbar/setting-up
        // DOCS: https://developer.android.com/training/appbar/actions
        val toolbar = binding.actionBar;
        toolbar.title = getString(R.string.section_wishlist)
        setSupportActionBar(toolbar)

        sectionSearch = SearchFragment()
        sectionWishlist = WishlistFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, sectionWishlist, "wishlist_section")
        transaction.add(R.id.container, sectionSearch, "search_section")
        transaction.hide(sectionSearch)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (sectionWishlist.isHidden) {
            switchToWishlist()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_search -> {
                if (sectionSearch.isHidden)
                    switchToSearch()
                return true
            }
            R.id.action_open_website -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.site_gamestop)))
                startActivity(browserIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setListeners() {
        binding.switchButton.setOnClickListener {
            sectionSwitch()
        }
    }

    /**
     * Hide wishlist and show search section and vice-versa
     */
    private fun sectionSwitch() {
        if (sectionSearch.isHidden)
            switchToSearch()
        else
            switchToWishlist()
    }

    private fun switchToSearch() {
        val transaction = supportFragmentManager.beginTransaction()

        // show search section
        transaction.hide(sectionWishlist)
        transaction.show(sectionSearch)

        // set action bar title
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.section_search)

        // set floating button image
        val icon = AppCompatResources.getDrawable(this, R.drawable.ic_videogame_asset_black_24dp)
        binding.switchButton.setImageDrawable(icon)

        transaction.commit()
    }

    private fun switchToWishlist() {
        val transaction = supportFragmentManager.beginTransaction()

        // show wishlist section
        transaction.show(sectionWishlist)
        transaction.hide(sectionSearch)

        // set action bar title
        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.section_wishlist)

        // set floating button image
        val icon = AppCompatResources.getDrawable(this, R.drawable.ic_search_black_24dp)
        binding.switchButton.setImageDrawable(icon)

        transaction.commit()
    }

}