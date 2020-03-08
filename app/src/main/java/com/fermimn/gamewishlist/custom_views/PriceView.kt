package com.fermimn.gamewishlist.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.fermimn.gamewishlist.R

class PriceView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    companion object {
        private val TAG: String = PriceView::class.java.simpleName
    }

    private val categoryView: TextView
    private val priceView: TextView
    private val oldPriceView: TextView

    init {
        View.inflate(context, R.layout.partial_game_price_new, this)
        categoryView = findViewById(R.id.category)
        priceView = findViewById(R.id.price)
        oldPriceView = findViewById(R.id.old_price)

        attrs?.let { setAttrs(it) }
    }

    private fun setAttrs(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.PriceView, 0, 0)
        val category = typedArray.getString(R.styleable.PriceView_category)
        val price = getFloat(typedArray, R.styleable.PriceView_price)
        val oldPrice = getFloat(typedArray, R.styleable.PriceView_old_price)
        val available = typedArray.getBoolean(R.styleable.PriceView_available, false)

        typedArray.recycle()

        var oldPrices: ArrayList<Float>? = null
        oldPrice?.let {
            oldPrices = ArrayList()
            oldPrices!!.add(it)
        }

        bind(category, price, oldPrices, available)
    }

    private fun getFloat(typedArray: TypedArray, resourceId: Int) : Float? {
        val price = typedArray.getFloat(resourceId, -1f)
        return if (price != -1f) price else null
    }

    fun bind(category: String?, price: Float?, oldPrices: ArrayList<Float>?, availability: Boolean) {
        categoryView.text = resources.getString(R.string.price_category, category)
        setPrice(price)
        setOldPrices(oldPrices)
        setAvailability(availability)
    }

    private fun setPrice(price: Float?) {
        priceView.text = resources.getString(R.string.price_currency, price.toString())
    }

    private fun setOldPrices(oldPrices: ArrayList<Float>?) {
        if (oldPrices != null) {
            for (oldPrice in oldPrices) {
                // TODO: create a TextView for each oldPrice
                setOldPrice(oldPrice)
            }
        } else {
            oldPriceView.visibility = View.GONE
        }
    }

    private fun setOldPrice(oldPrice: Float?) {
        oldPriceView.text = resources.getString(R.string.price_currency, oldPrice.toString())
        oldPriceView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        oldPriceView.visibility = View.VISIBLE
    }

    private fun setAvailability(availability: Boolean) {
        if (availability)
            setBackgroundResource(R.color.colorAccent)
        else
            setBackgroundResource(R.color.price_not_available)
    }

}