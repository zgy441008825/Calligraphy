package com.zougy.calligraphy.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.zougy.calligraphy.R
import org.xutils.common.util.DensityUtil

import com.zougy.calligraphy.view.CalligraphyLayout.GridBgStyle
import com.zougy.calligraphy.view.CalligraphyLayout.GridBgStyle.fontTypes

/**
 * Description:绘制一个带田字格或米字格的文字<br>
 * Author:邹高原<br>
 * Date:04/06 0006<br>
 * Email:441008824@qq.com
 */
class CalligraphyView : View {

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
     * 网格线的大小(4边的线条)
     */
    private var gridLineWidth = 2f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 斜线大小(虚线网格)
     */
    private var gridSlashWidth = 2f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 网格样式
     * @see GridBgStyle
     */
    private var gridBgStyle = GridBgStyle.MATTS
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 网格颜色
     */
    private var gridColor = Color.RED
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 显示的文本
     */
    private var text = ""
        set(value) {
            if (TextUtils.isEmpty(value) || value.length != 1) return
            field = value
            invalidate()
        }

    /**
     * 字体颜色
     */
    private var textColor = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 字体大小
     */
    private var textSize = DensityUtil.dip2px(20f).toFloat()
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 字体距离边框的距离
     */
    private var textPadding = 0
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 字体样式
     */
    private var fontType = 0
        set(value) {
            if (value < 0 || value >= fontTypes.size) return
            field = value
            invalidate()
        }

    /**
     * 屏幕宽度
     */
    private var viewWidth = 0

    /**
     * 屏幕高度
     */
    private var viewHeight = 0

    /**
     * View距离边的宽度
     */
    private var margin = 0
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 是否绘制背景框
     */
    private var enableGrid = true
        set(value) {
            field = value
            invalidate()
        }


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, styleId: Int = 0) : super(
        context,
        attrs,
        styleId
    ) {
        if (attrs != null) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CalligraphyView)
            gridLineWidth =
                typeArray.getDimension(R.styleable.CalligraphyView_gridLineWidth, 2f)
            gridSlashWidth =
                typeArray.getDimension(R.styleable.CalligraphyView_gridSlashWidth, 2f)
            gridBgStyle =
                typeArray.getInt(
                    R.styleable.CalligraphyView_gridBgStyle,
                    GridBgStyle.MATTS
                )
            gridColor = typeArray.getColor(R.styleable.CalligraphyView_gridColor, Color.RED)
            text = typeArray.getString(R.styleable.CalligraphyView_android_text).toString()
            textColor =
                typeArray.getColor(R.styleable.CalligraphyView_android_textColor, Color.BLACK)
            textSize = typeArray.getDimension(
                R.styleable.CalligraphyView_android_textSize,
                DensityUtil.dip2px(20f).toFloat()
            )
            textPadding = typeArray.getDimension(
                R.styleable.CalligraphyView_android_padding,
                0f
            ).toInt()
            margin =
                typeArray.getDimension(
                    R.styleable.CalligraphyView_android_layout_margin,
                    0f
                ).toInt()
            enableGrid = typeArray.getBoolean(R.styleable.CalligraphyView_enableGridBackground, true)
            fontType = typeArray.getInt(R.styleable.CalligraphyView_textFontType, 0)
            typeArray.recycle()
        }

        gridBorderPaint.isAntiAlias = true
        gridBorderPaint.strokeWidth = gridLineWidth
        gridBorderPaint.color = gridColor
        gridBorderPaint.style = Paint.Style.STROKE

        gridLinePaint.isAntiAlias = true
        gridLinePaint.strokeWidth = gridSlashWidth
        gridLinePaint.color = gridColor
        gridLinePaint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 40f)

        textPaint.isAntiAlias = true
        textPaint.textSize = textSize
        textPaint.color = textColor
        textPaint.typeface = Typeface.createFromAsset(context.assets, fontTypes[fontType])
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (TextUtils.isEmpty(text) || text.length != 1) return
        if (viewWidth == 0 || viewHeight == 0) return
        drawOneText(text[0], canvas)
    }

    /**
     * 绘制一个字
     */
    private fun drawOneText(c: Char, canvas: Canvas?) {
        val oneWordHeight =
            (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top) + textPadding * 2
        val oneTextSize = (textPaint.measureText(text[0].toString()) + textPadding * 2).coerceAtLeast(
            oneWordHeight
        )
        val rect = RectF()
        rect.left = (viewWidth - oneTextSize) / 2 + margin.toFloat() + gridLineWidth
        rect.top = (viewHeight - oneTextSize) / 2 + margin.toFloat() + gridLineWidth
        rect.right = rect.left + oneTextSize - gridLineWidth
        rect.bottom = rect.top + oneTextSize - gridLineWidth
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