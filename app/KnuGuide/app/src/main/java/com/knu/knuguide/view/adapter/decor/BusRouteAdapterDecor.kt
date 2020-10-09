package com.knu.knuguide.view.adapter.decor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.knu.knuguide.R
import com.knu.knuguide.support.Utils

class BusRouteAdapterDecor(val context: Context, var spacing: Float, var margin: Float, val listener: BusRouteDecorInterface) : RecyclerView.ItemDecoration() {
    private var paint: Paint = Paint()

    init {
        spacing = Utils.dp2px(spacing)
        margin = Utils.dp2px(margin)

        paint.isAntiAlias = true
        paint.color = ContextCompat.getColor(context, R.color.item_bus_node_bottom_decor)
        paint.strokeWidth = spacing
        paint.style = Paint.Style.FILL_AND_STROKE
    }

    interface BusRouteDecorInterface {
        fun isNode(position: Int): Boolean
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position < state.itemCount - 1)
            outRect.bottom = spacing.toInt()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val offset = spacing / 2
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val childPosition = parent.getChildAdapterPosition(view)

            if (childPosition == RecyclerView.NO_POSITION) continue
            if (!listener.isNode(childPosition)) continue

            val params: RecyclerView.LayoutParams = view.layoutParams as RecyclerView.LayoutParams

            val position = params.viewAdapterPosition
            if (position < state.itemCount - 1)
                c.drawLine(view.left.toFloat() + margin, view.bottom + offset, view.right.toFloat(), view.bottom + offset, paint)
        }
    }
}
