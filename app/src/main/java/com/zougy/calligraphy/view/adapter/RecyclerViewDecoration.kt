package com.zougy.calligraphy.view.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:2020/4/21<br>
 * Email:441008824@qq.com
 */
@Suppress("CAST_NEVER_SUCCEEDS")
class RecyclerViewDecoration(
    private val offset: Int = 0,//item间的间距
    drawColor: Int = Color.TRANSPARENT
) :
    RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.isAntiAlias = true
        paint.color = drawColor
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        when (parent.adapter) {
            is LinearLayoutManager -> {
                val adapter = parent.adapter as LinearLayoutManager
                if (adapter.orientation == RecyclerView.HORIZONTAL)
                    drawHor(c, parent)
                else
                    drawVer(c, parent)
            }
            is GridLayoutManager -> drawGrid(c, parent)
        }
    }

    private fun drawHor(c: Canvas, parent: RecyclerView) {
        for (i in 0 until parent.childCount) {
            if (i < parent.childCount - 1) {
                val childView = parent.getChildAt(i)
                val rect = Rect(childView.right, childView.top, childView.right + offset, childView.bottom)
                c.drawRect(rect, paint)
            }
        }
    }

    private fun drawVer(c: Canvas, parent: RecyclerView) {
        for (i in 0 until parent.childCount) {
            if (i < parent.childCount - 1) {
                val childView = parent.getChildAt(i)
                val rect = Rect(childView.left, childView.bottom, childView.right, childView.bottom + offset)
                c.drawRect(rect, paint)
            }
        }
    }

    private fun drawGrid(c: Canvas, parent: RecyclerView) {
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val childCount = parent.childCount

        //总共多少行
        val rowCount = if (childCount / spanCount == 0) childCount / spanCount else childCount / spanCount + 1

        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            //当前是第几列
            val column = (i % spanCount)
            //当前是第几行
            val row = i / spanCount
            if (column < spanCount - 1) {//最后一列，右边没有
                val rect = Rect(childView.right, childView.top, childView.right + offset, childView.bottom)
                c.drawRect(rect, paint)
            }
            if (row < rowCount - 1) {//最后一行，底部没有
                val rect = Rect(childView.left, childView.bottom, childView.right, childView.bottom + offset)
                c.drawRect(rect, paint)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when (parent.adapter) {
            is LinearLayoutManager -> {
                val adapter = parent.adapter as LinearLayoutManager
                if (adapter.orientation == RecyclerView.HORIZONTAL)
                    outRect.right = offset
                else
                    outRect.bottom = offset
            }
            is GridLayoutManager -> setGridOffset(outRect, view, parent)
        }
    }

    private fun setGridOffset(outRect: Rect, view: View, parent: RecyclerView) {

    }


}