package com.zougy.calligraphy.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import com.zougy.calligraphy.R
import org.xutils.common.util.DensityUtil

/**
 * Description:绘制文本，增加田字格或者米字格<br>
 * Author:邹高原<br>
 * Date:04/03 0003<br>
 * Email:441008824@qq.com
 */
class CalligraphyViewLayout : BaseCalligraphyView {

    /**
     * 每个字水平间隔
     */
    private var gridHorSpace = DensityUtil.dip2px(5f)

    /**
     * 每行的间距
     */
    private var gridVerSpace = DensityUtil.dip2px(5f)

    /**
     * 屏幕宽度
     */
    private var viewWidth = 0

    /**
     * 屏幕高度
     */
    private var viewHeight = 0

    /**
     * 一个字实际要占用的大小
     */
    private var oneTextSize = 0f


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, styleId: Int = 0) : super(
        context,
        attrs,
        styleId
    )

    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)
        if (attrs != null) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CalligraphyViewLayout)
            gridHorSpace = typeArray.getDimension(
                R.styleable.CalligraphyViewLayout_gridHorSpace,
                gridHorSpace.toFloat()
            ).toInt()
            gridVerSpace = typeArray.getDimension(
                R.styleable.CalligraphyViewLayout_gridVerSpace,
                gridVerSpace.toFloat()
            ).toInt()
            typeArray.recycle()
        }
    }

    fun setShowText(text: String) {
        this.text = text
        val layoutP = layoutParams
        layoutP.width = width
        layoutP.height = getTextHeight(width)
        layoutParams = layoutP
        invalidate()
    }

    fun getShowText(): String = text

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    private var widthMode = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (TextUtils.isEmpty(text)) return
        widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val hSpec = MeasureSpec.makeMeasureSpec(getTextHeight(MeasureSpec.getSize(widthMeasureSpec)), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, hSpec)
    }

    private fun getTextHeight(width: Int): Int {
        val oneHeight = (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top) + textPadding * 2
        val oneSize = (textPaint.measureText(text[0].toString()) + textPadding * 2).coerceAtLeast(oneHeight)
        val tW = oneSize + gridHorSpace
        val column = (if ((width % tW).toInt() == 0) width / tW else (width / tW)).toInt()
        val row = (if ((text.length % column) == 0) text.length / column else (text.length / column) + 1).toInt()
        return (row * tW + margin * 2).toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (TextUtils.isEmpty(text)) return
        if (viewWidth == 0 || viewHeight == 0) return
        val oneWordHeight =
            (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top) + textPadding * 2
        oneTextSize = (textPaint.measureText(text[0].toString()) + textPadding * 2).coerceAtLeast(
            oneWordHeight
        )
        val tW = oneTextSize + gridHorSpace
        val column =
            (if ((viewWidth % tW).toInt() == 0) viewWidth / tW else (viewWidth / tW)).toInt()
        val row =
            (if ((text.length % column) == 0) text.length / column else (text.length / column) + 1).toInt()
        for (x in 0 until row) {
            for (y in 0 until column) {
                val index = x * column + y
                if (index >= text.length) return
                drawOneText(text[index], y, x, canvas)
            }
        }
    }

    /**
     * 绘制一个字
     * @param col 列
     * @param row 行
     */
    private fun drawOneText(c: Char, col: Int, row: Int, canvas: Canvas?) {
        val rect = RectF()
        rect.left = col * (oneTextSize + gridHorSpace) + margin + gridBorderPaint.strokeWidth
        rect.top = row * (oneTextSize + gridVerSpace) + margin + gridBorderPaint.strokeWidth
        rect.right = rect.left + oneTextSize
        rect.bottom = rect.top + oneTextSize
        if (enableGrid) {
            //绘制背景框
            canvas?.drawRect(rect, gridBorderPaint)

            //绘制田字格
            canvas?.drawLine(
                rect.left,
                rect.top + rect.height() / 2,
                rect.right,
                rect.top + rect.height() / 2,
                gridLinePaint
            )
            canvas?.drawLine(
                rect.left + rect.width() / 2,
                rect.top,
                rect.left + rect.width() / 2,
                rect.bottom,
                gridLinePaint
            )

            //米字格
            if (gridBgStyle == GridBgStyle.MIZI) {
                canvas?.drawLine(rect.left, rect.top, rect.right, rect.bottom, gridLinePaint)
                canvas?.drawLine(rect.right, rect.top, rect.left, rect.bottom, gridLinePaint)
            }
        }

        val textW = textPaint.measureText(c.toString())
        val fm = textPaint.fontMetrics
        val baseLine = (rect.height() + (fm.bottom - fm.top)) / 2 - fm.descent
        canvas?.drawText(
            c.toString(),
            rect.left + (rect.width() - textW) / 2,
            rect.top + baseLine,
            textPaint
        )
    }

}