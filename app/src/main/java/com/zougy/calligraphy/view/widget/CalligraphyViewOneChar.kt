package com.zougy.calligraphy.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:2020/4/20<br>
 * Email:441008824@qq.com
 */
open class CalligraphyViewOneChar : BaseCalligraphyView {

    /**
     * 屏幕宽度
     */
    protected var viewWidth = 0

    /**
     * 屏幕高度
     */
    protected var viewHeight = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    private val oneTextRectF = RectF()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (TextUtils.isEmpty(text) || text.length != 1 || canvas == null) return
        val viewSize = viewHeight.coerceAtMost(viewWidth)
        oneTextRectF.left = ((viewWidth - viewSize) / 2).toFloat()
        oneTextRectF.top = ((viewHeight - viewSize) / 2).toFloat()
        oneTextRectF.right = viewWidth - oneTextRectF.left
        oneTextRectF.bottom = viewHeight - oneTextRectF.top
        drawOneText(text[0], canvas, oneTextRectF)
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val wSize = MeasureSpec.getSize(widthMeasureSpec)
//        val wMode = getModeName(MeasureSpec.getMode(widthMeasureSpec))
//        val hSize = MeasureSpec.getSize(heightMeasureSpec)
//        val hMode = getModeName(MeasureSpec.getMode(heightMeasureSpec))
//        Log.d("CalligraphyViewOneChar", "ZLog onMeasure wSize:$wSize wMode:$wMode hSize:$hSize hMode:$hMode")
//    }
//
//    protected fun getModeName(mode: Int): String {
//        return when (mode) {
//            MeasureSpec.AT_MOST -> "AT_MOST"
//            MeasureSpec.EXACTLY -> "EXACTLY"
//            MeasureSpec.UNSPECIFIED -> "UNSPECIFIED"
//            else -> "UNKNOWN"
//        }
//    }

    open fun setShowText(text: String) {
        this.text = text
        invalidate()
    }

    /**
     * 绘制一个字
     */
    protected fun drawOneText(c: Char, canvas: Canvas, rect: RectF) {
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