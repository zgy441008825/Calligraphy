package com.zougy.calligraphy.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:2020/4/20<br>
 * Email:441008824@qq.com
 */
class CalligraphyViewOneChar : BaseCalligraphyView {

    /**
     * 屏幕宽度
     */
    private var viewWidth = 0

    /**
     * 屏幕高度
     */
    private var viewHeight = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (TextUtils.isEmpty(text) || text.length != 1 || canvas == null) return
        drawOneText(text[0], canvas)
    }

    fun setShowText(text: String) {
        this.text = text
        invalidate()
    }

    /**
     * 绘制一个字
     */
    private fun drawOneText(c: Char, canvas: Canvas) {
        val rect = RectF()
        val viewSize = viewHeight.coerceAtMost(viewWidth)
        rect.left = ((viewWidth - viewSize) / 2).toFloat()
        rect.top = ((viewHeight - viewSize) / 2).toFloat()
        rect.right = viewWidth - rect.left
        rect.bottom = viewHeight - rect.top
        if (enableGrid) {
            //绘制背景框
            canvas.drawRect(rect, gridBorderPaint)

            //绘制田字格
            canvas.drawLine(
                rect.left,
                rect.top + rect.height() / 2,
                rect.right,
                rect.top + rect.height() / 2,
                gridLinePaint
            )
            canvas.drawLine(
                rect.left + rect.width() / 2,
                rect.top,
                rect.left + rect.width() / 2,
                rect.bottom,
                gridLinePaint
            )

            //米字格
            if (gridBgStyle == GridBgStyle.MIZI) {
                canvas.drawLine(rect.left, rect.top, rect.right, rect.bottom, gridLinePaint)
                canvas.drawLine(rect.right, rect.top, rect.left, rect.bottom, gridLinePaint)
            }
        }

        val textW = textPaint.measureText(c.toString())
        val fm = textPaint.fontMetrics
        val baseLine = (rect.height() + (fm.bottom - fm.top)) / 2 - fm.descent
        canvas.drawText(
            c.toString(),
            rect.left + (rect.width() - textW) / 2,
            rect.top + baseLine,
            textPaint
        )
    }

}