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

class AnnouncementDecor(val context: Context, var spacing: Float) : RecyclerView.ItemDecoration() {
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
}
