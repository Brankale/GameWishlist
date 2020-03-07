package com.fermimn.gamewishlist.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fermimn.gamewishlist.models.GamePreview


class GamePreviewRecyclerView(context: Context, attrs: AttributeSet?)
    : RecyclerView(context, attrs) {

    companion object {
        private val TAG: String = GamePreviewRecyclerView::class.java.simpleName
    }

    init {
        setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        setLayoutManager(layoutManager)

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        addItemDecoration(dividerItemDecoration)
    }

}

class GameAdapter(val gamePreviews: ArrayList<GamePreview>?)
    : RecyclerView.Adapter<GameViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val item = GamePreviewView(parent.context, null)
        return GameViewHolder(item)
    }

    override fun getItemCount(): Int = gamePreviews?.size ?: 0

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        gamePreviews?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemId(position: Int): Long {
        gamePreviews?.let {
            return it[position].id.toLong()
        }
        return -1
    }

}

class GameViewHolder(val view: GamePreviewView) : RecyclerView.ViewHolder(view) {

    fun bind(gamePreview : GamePreview) {
        view.bind(gamePreview)
    }

}