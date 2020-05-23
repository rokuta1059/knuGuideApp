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

class PreviewAnnouncementDecor(val context: Context, var spacing: Float) : RecyclerView.ItemDecoration() {
    private var paint: Paint = Paint()

    init {
        spacing = Utils.dp2px(spacing)

        paint.isAntiAlias = true
        paint.color = ContextCompat.getColor(context, R.color.preview_announcement_divider)
        paint.strokeWidth = spacing
        paint.style = Paint.Style.FILL_AND_STROKE
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
            val params: RecyclerView.LayoutParams = view.layoutParams as RecyclerView.LayoutParams

            val position = params.viewAdapterPosition
            if (position < state.itemCount - 1)
                c.drawLine(view.left.toFloat(), view.bottom + offset, view.right.toFloat(), view.bottom + offset, paint)
        }
    }
}
