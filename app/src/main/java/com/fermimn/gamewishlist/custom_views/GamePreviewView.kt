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
import com.fermimn.gamewishlist.models.Price
import com.github.brankale.models.Availability
import com.github.brankale.models.Game
import com.github.brankale.models.ItemCondition
import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException

class GamePreviewView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    companion object {
        @Suppress("unused")
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
        View.inflate(context, R.layout.partial_game_preview, this)
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
        val gamePreview = Game.Builder(id)
            .name(typedArray.getString(R.styleable.GamePreviewView_title) ?: "")
            .publisher(typedArray.getString(R.styleable.GamePreviewView_publisher))
            .build()

//        gamePreview.cover = typedArray.getString(R.styleable.GamePreviewView_cover)
//        gamePreview.name =
//        gamePreview.platform = typedArray.getString(R.styleable.GamePreviewView_platform)
//        gamePreview.publisher = typedArray.getString(R.styleable.GamePreviewView_publisher)

//        gamePreview.newPrice = getFloat(typedArray, R.styleable.GamePreviewView_price_new)
//        gamePreview.usedPrice = getFloat(typedArray, R.styleable.GamePreviewView_price_used)
//        gamePreview.preorderPrice = getFloat(typedArray, R.styleable.GamePreviewView_price_preorder)
//        gamePreview.digitalPrice = getFloat(typedArray, R.styleable.GamePreviewView_price_digital)
//        gamePreview.addOldNewPrice( getFloat(typedArray, R.styleable.GamePreviewView_price_old_new) )
//        gamePreview.addOldUsedPrice( getFloat(typedArray, R.styleable.GamePreviewView_price_old_used) )
//        gamePreview.addOldPreorderPrice( getFloat(typedArray, R.styleable.GamePreviewView_price_old_preorder) )
//        gamePreview.addOldDigitalPrice( getFloat(typedArray, R.styleable.GamePreviewView_price_old_digital) )
//        gamePreview.newAvailable = typedArray.getBoolean(R.styleable.GamePreviewView_price_new, false)
//        gamePreview.usedAvailable = typedArray.getBoolean(R.styleable.GamePreviewView_price_used, false)
//        gamePreview.preorderAvailable = typedArray.getBoolean(R.styleable.GamePreviewView_price_preorder, false)
//        gamePreview.digitalAvailable = typedArray.getBoolean(R.styleable.GamePreviewView_price_digital, false)

        typedArray.recycle()

        bind(gamePreview)
    }

    private fun getFloat(typedArray: TypedArray, resourceId: Int) : Float? {
        val price = typedArray.getFloat(resourceId, -1f)
        return if (price != -1f) price else null
    }

    fun bind(gamePreview: com.github.brankale.models.Game) {
        with (gamePreview) {
//            setCover(coverUrl)
            setTitle(name)
            setPlatformAndPublisher("N.D.", publisher)
            setPrices(prices)
        }
    }

    private fun setCover(cover: String?) {
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

    private fun setPrices(prices: List<com.github.brankale.models.Price>?) {
        priceFirst.visibility = View.GONE
        priceSecond.visibility = View.GONE

        when (prices?.size) {
            1 -> setPriceView(priceFirst, prices[0])
            2 -> setPriceView(priceSecond, prices[1])
            else -> throw IllegalArgumentException()
        }
    }

    private fun setPriceView(view: PriceView, price: com.github.brankale.models.Price) {
        view.visibility = View.VISIBLE
        with (price) {
            when (condition) {
                ItemCondition.NEW ->
                    when (availability) {
                        Availability.PREORDER -> view.bind(Price.USED, price.price.toFloat(), null, true)
                        Availability.IN_STOCK -> view.bind(Price.USED, price.price.toFloat(), null, true)
                        Availability.OUT_OF_STOCK -> view.bind(Price.USED, price.price.toFloat(), null, false)
                        else -> {
                            // do nothing
                        }
                    }
                ItemCondition.USED ->
                    when (availability) {
                        Availability.IN_STOCK -> view.bind(Price.USED, price.price.toFloat(), null, true)
                        Availability.OUT_OF_STOCK -> view.bind(Price.USED, price.price.toFloat(), null, false)
                        else -> throw IllegalArgumentException()
                    }
                else -> {
                    // do nothing
                }
            }
        }
    }

}