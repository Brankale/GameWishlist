package com.fermimn.gamewishlist.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.fermimn.gamewishlist.R
import com.fermimn.gamewishlist.models.Price
import java.lang.IllegalArgumentException

class PriceView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    companion object {
        @Suppress("unused")
        private val TAG: String = PriceView::class.java.simpleName
    }

    private val categoryView: TextView
    private val priceView: TextView
    private val oldPriceView: TextView

    init {
        View.inflate(context, R.layout.partial_game_price, this)
        categoryView = findViewById(R.id.category)
        priceView = findViewById(R.id.price)
        oldPriceView = findViewById(R.id.old_price)

        attrs?.let { setAttrs(it) }
    }

    private fun setAttrs(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.PriceView, 0, 0)
        val category = typedArray.getInt(R.styleable.PriceView_category, -1)
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

    fun bind(categoryId: Int, price: Float?, oldPrices: List<Float>?, availability: Boolean) {
        setCategory(categoryId)
        setPrice(price)
        setOldPrices(oldPrices)
        setAvailability(availability)
    }

    private fun setCategory(categoryId: Int) {
        val category = getCategoryString(categoryId)
        categoryView.text = resources.getString(R.string.price_category, category)
    }

    private fun getCategoryString(category: Int) = when (category) {
        Price.UNKNOWN -> resources.getText(R.string.price_unknown)
        Price.NEW -> resources.getText(R.string.price_new)
        Price.USED -> resources.getText(R.string.price_used)
        Price.DIGITAL -> resources.getText(R.string.price_digital)
        Price.PREORDER -> resources.getText(R.string.price_preorder)
        else -> throw IllegalArgumentException("Unknown price category. Passed value = $category")
    }

    private fun setPrice(price: Float?) {
        priceView.text = resources.getString(R.string.price_currency, price.toString())
    }

    private fun setOldPrices(oldPrices: List<Float>?) {
        if (oldPrices != null && oldPrices.isNotEmpty()) {
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
            setBackgroundResource(R.color.out_of_stock)
    }

    fun setTextSize(unit: Int, size: Float) {
        categoryView.setTextSize(unit, size)
        priceView.setTextSize(unit, size)
        oldPriceView.setTextSize(unit, size)
    }

}