package com.zougy.calligraphy

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import org.xutils.common.util.DensityUtil

/**
 * Description:绘制文本，增加田字格或者米字格<br>
 * Author:邹高原<br>
 * Date:04/03 0003<br>
 * Email:441008824@qq.com
 */
class CalligraphyTextView : View {

    /**
     * 网格边框画笔
     */
    private val gridBorderPaint = Paint()

    /**
     * 网格线画笔
     */
    private val gridLinePaint = Paint()

    /**
     * 字体画笔
     */
    private val textPaint = TextPaint()

    /**
     * 网格线的大小
     */
    private var gridLineWidth = 2f

    /**
     * 斜线大小
     */
    private var gridSlashWidth = 2f

    /**
     * 网格线是否为虚线（不包含边框）
     */
    private var gridSlashImgLine = true

    /**
     * 网格颜色
     */
    private var gridColor = Color.RED

    /**
     * 每个字水平间隔
     */
    private var gridHorSpace = DensityUtil.dip2px(5f)

    /**
     * 每行的间距
     */
    private var gridVerSpace = DensityUtil.dip2px(5f)

    /**
     * 显示的文本
     */
    private var text = ""

    /**
     * 字体颜色
     */
    private var textColor = Color.BLACK

    /**
     * 字体大小
     */
    private var textSize = DensityUtil.dip2px(20f).toFloat()

    /**
     * 字体距离边框的距离
     */
    private var textPadding = DensityUtil.dip2px(10f)

    /**
     * 屏幕宽度
     */
    private var viewWidth = 0

    /**
     * 屏幕高度
     */
    private var viewHeight = 0

    private var marginStart = DensityUtil.dip2px(10f)

    private var marginTop = DensityUtil.dip2px(10f)

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
    ) {
        if (attrs != null) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CalligraphyTextView)
            gridLineWidth = typeArray.getDimension(R.styleable.CalligraphyTextView_gridLineWidth, 2f)
            gridSlashWidth = typeArray.getDimension(R.styleable.CalligraphyTextView_gridSlashWidth, 2f)
            gridSlashImgLine = typeArray.getBoolean(R.styleable.CalligraphyTextView_gridSlashImgLine, true)
            gridColor = typeArray.getColor(R.styleable.CalligraphyTextView_gridColor, Color.RED)
            gridHorSpace = typeArray.getDimension(R.styleable.CalligraphyTextView_gridHorSpace, DensityUtil.dip2px(5f).toFloat()).toInt()
            gridVerSpace = typeArray.getDimension(R.styleable.CalligraphyTextView_gridVerSpace, DensityUtil.dip2px(5f).toFloat()).toInt()
            text = typeArray.getString(R.styleable.CalligraphyTextView_android_text).toString()
            textColor = typeArray.getColor(R.styleable.CalligraphyTextView_android_textColor, Color.BLACK)
            textSize = typeArray.getDimension(R.styleable.CalligraphyTextView_android_textSize, DensityUtil.dip2px(20f).toFloat())
            textPadding = typeArray.getDimension(R.styleable.CalligraphyTextView_android_padding, DensityUtil.dip2px(10f).toFloat()).toInt()
            marginStart =
                typeArray.getDimension(R.styleable.CalligraphyTextView_android_layout_marginStart, DensityUtil.dip2px(10f).toFloat()).toInt()
            marginTop = typeArray.getDimension(R.styleable.CalligraphyTextView_android_layout_marginTop, DensityUtil.dip2px(10f).toFloat()).toInt()
            typeArray.recycle()
        }

        gridBorderPaint.isAntiAlias = true
        gridBorderPaint.strokeWidth = gridLineWidth
        gridBorderPaint.color = gridColor
        gridBorderPaint.style = Paint.Style.STROKE

        gridLinePaint.isAntiAlias = true
        gridLinePaint.strokeWidth = gridSlashWidth
        gridLinePaint.color = gridColor
        if (gridSlashImgLine) {
            gridLinePaint.pathEffect = DashPathEffect(floatArrayOf(4f, 4f), 0f)
        }

        textPaint.isAntiAlias = true
        textPaint.textSize = textSize
        textPaint.color = textColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (TextUtils.isEmpty(text)) return
        if (viewWidth == 0 || viewHeight == 0) return
        val oneWordHeight = (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top) + textPadding * 2
        oneTextSize = (textPaint.measureText(text[0].toString()) + textPadding * 2).coerceAtLeast(oneWordHeight)
        val tW = oneTextSize + gridHorSpace
        val column = (if ((viewWidth % tW).toInt() == 0) viewWidth / tW else (viewWidth / tW)).toInt()
        val row = (if ((text.length % column) == 0) text.length / column else (text.length / column) + 1).toInt()
        Log.d("CalligraphyTextView", "ZLog onDraw col:$column row:$row length:${text.length} text:$text")
        for (x in 0 until row) {
            for (y in 0 until column) {
                val index = x * row + y
                if (index >= text.length) return
                drawOneText(text[x * column + y], y, x, canvas)
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
        rect.left = col * (oneTextSize + gridHorSpace) + marginStart
        rect.top = row * (oneTextSize + gridVerSpace) + marginTop
        rect.right = rect.left + oneTextSize
        rect.bottom = rect.top + oneTextSize
        canvas?.drawRect(rect, gridBorderPaint)
        val textW = textPaint.measureText(c.toString())
        val fm = textPaint.fontMetrics
        val baseLine = (rect.height() + (fm.bottom - fm.top)) / 2 - fm.descent
        canvas?.drawText(c.toString(), rect.left + (rect.width() - textW) / 2, rect.top + baseLine, textPaint)
    }

}