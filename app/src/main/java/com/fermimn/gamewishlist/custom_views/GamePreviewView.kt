package com.fermimn.gamewishlist.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.getIntOrThrow
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.models.GamePreview
import com.squareup.picasso.Picasso

class GamePreviewView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    companion object {
        private val TAG: String = GamePreviewView::class.java.simpleName
    }

    // in all cases a game can have max 2 categories of prices
    // NEW/USED, PREORDER, DIGITAL
    private val priceFirst: PriceView
    private val priceSecond: PriceView
    private val coverView: ImageView
    private val titleView: TextView
    private val infoView: TextView

    init {
        View.inflate(context, R.layout.partial_game_preview_new, this)
        coverView = findViewById(R.id.cover)
        titleView = findViewById(R.id.title)
        infoView = findViewById(R.id.platform_publisher)
        priceFirst = findViewById(R.id.price_first)
        priceSecond = findViewById(R.id.price_second)

        // TODO: xml layout params are lost in the inflate phase so they must be set
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        attrs?.let { setAttrs(it) }
    }

    private fun setAttrs(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.GamePreviewView, 0, 0)

        val id: Int = typedArray.getIntOrThrow(R.styleable.GamePreviewView_cover)
        val gamePreview = GamePreview(id)

        gamePreview.cover = typedArray.getString(R.styleable.GamePreviewView_cover)
        gamePreview.title = typedArray.getString(R.styleable.GamePreviewView_title)
        gamePreview.platform = typedArray.getString(R.styleable.GamePreviewView_platform)
        gamePreview.publisher = typedArray.getString(R.styleable.GamePreviewView_publisher)

        gamePreview.newPrice = getFloat(typedArray, R.styleable.GamePreviewView_price_new)
        gamePreview.usedPrice = getFloat(typedArray, R.styleable.GamePreviewView_price_used)
        gamePreview.preorderPrice = getFloat(typedArray, R.styleable.GamePreviewView_price_preorder)
        gamePreview.digitalPrice = getFloat(typedArray, R.styleable.GamePreviewView_price_digital)
        gamePreview.addOldNewPrice( getFloat(typedArray, R.styleable.GamePreviewView_price_old_new) )
        gamePreview.addOldUsedPrice( getFloat(typedArray, R.styleable.GamePreviewView_price_old_used) )
        gamePreview.addOldPreorderPrice( getFloat(typedArray, R.styleable.GamePreviewView_price_old_preorder) )
        gamePreview.addOldDigitalPrice( getFloat(typedArray, R.styleable.GamePreviewView_price_old_digital) )
        gamePreview.newAvailable = typedArray.getBoolean(R.styleable.GamePreviewView_price_new, false)
        gamePreview.usedAvailable = typedArray.getBoolean(R.styleable.GamePreviewView_price_used, false)
        gamePreview.preorderAvailable = typedArray.getBoolean(R.styleable.GamePreviewView_price_preorder, false)
        gamePreview.digitalAvailable = typedArray.getBoolean(R.styleable.GamePreviewView_price_digital, false)

        typedArray.recycle()

        bind(gamePreview)
    }

    private fun getFloat(typedArray: TypedArray, resourceId: Int) : Float? {
        val price = typedArray.getFloat(resourceId, -1f)
        return if (price != -1f) price else null
    }

    fun bind(gamePreview: GamePreview) {
        with (gamePreview) {
            setCover(cover)
            setTitle(title)
            setPlatformAndPublisher(platform, publisher)
            setPrices(gamePreview)
        }
    }

    private fun setCover(cover: String?) {
        // check is not necessary but helps to avoid warnings and exceptions
        cover?.let {
            Picasso.get().load(cover).into(coverView)
        }
    }

    private fun setTitle(title: String?) {
        titleView.text = title
    }

    private fun setPlatformAndPublisher(platform: String?, publisher: String?) {
        infoView.isSingleLine = true
        infoView.ellipsize = TextUtils.TruncateAt.END

        val str = "<b>$platform</b> by <b>$publisher</b>"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            infoView.text = Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
        } else {
            infoView.text = Html.fromHtml(str)
        }
    }

    private fun setPrices(gamePreview: GamePreview) {
        with (gamePreview) {
            priceFirst.visibility = View.GONE
            priceSecond.visibility = View.GONE

            if (usedPrice != null) {
                val category = resources.getString(R.string.price_used)
                priceSecond.bind(category, usedPrice, oldUsedPrices, usedAvailable)
                priceSecond.visibility = View.VISIBLE
            }

            if (newPrice != null) {
                val category = resources.getString(R.string.price_new)
                priceFirst.bind(category, newPrice, oldNewPrices, newAvailable)
                priceFirst.visibility = View.VISIBLE
                return  // if the game is new, it can't be digital or on preorder
            }

            if (preorderPrice != null) {
                val category =
                        if (preorderAvailable) {
                            resources.getString(R.string.price_preorder)
                        } else {
                            resources.getString(R.string.price_booking)
                        }
                priceFirst.bind(category, preorderPrice, oldPreorderPrices, preorderAvailable)
                priceFirst.visibility = View.VISIBLE
                return  // if the game is on preorder, it can't be new or digital
            }

            if (digitalPrice != null) {
                val category = resources.getString(R.string.price_digital)
                priceFirst.bind(category, digitalPrice, oldDigitalPrices, digitalAvailable)
                priceFirst.visibility = View.VISIBLE
                return  // if the game is digital, it can't be new or on preorder
            }
        }
    }

}