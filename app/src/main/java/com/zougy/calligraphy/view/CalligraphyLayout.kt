package com.zougy.calligraphy.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.zougy.calligraphy.R
import com.zougy.calligraphy.view.CalligraphyLayout.GridBgStyle.fontTypes
import org.xutils.common.util.DensityUtil

/**
 * Description:绘制文本，增加田字格或者米字格<br>
 * Author:邹高原<br>
 * Date:04/03 0003<br>
 * Email:441008824@qq.com
 */
class CalligraphyLayout : View {

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
     * 网格样式
     * @see GridBgStyle
     */
    private var gridBgStyle = GridBgStyle.MATTS

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
    private var textPadding = 0

    /**
     * 字体样式
     */
    private var fontType = 0

    /**
     * 屏幕宽度
     */
    private var viewWidth = 0

    /**
     * 屏幕高度
     */
    private var viewHeight = 0

    /**
     * View距离左边的宽度
     */
    private var margin = 0

    /**
     * 一个字实际要占用的大小
     */
    private var oneTextSize = 0f

    /**
     * 是否绘制背景框
     */
    private var enableGrid = true


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, styleId: Int = 0) : super(
        context,
        attrs,
        styleId
    ) {
        if (attrs != null) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CalligraphyLayout)
            gridLineWidth =
                typeArray.getDimension(R.styleable.CalligraphyLayout_gridLineWidth, 2f)
            gridSlashWidth =
                typeArray.getDimension(R.styleable.CalligraphyLayout_gridSlashWidth, 2f)
            gridBgStyle =
                typeArray.getInt(
                    R.styleable.CalligraphyLayout_gridBgStyle,
                    GridBgStyle.MATTS
                )
            gridColor = typeArray.getColor(R.styleable.CalligraphyLayout_gridColor, Color.RED)
            gridHorSpace = typeArray.getDimension(
                R.styleable.CalligraphyLayout_gridHorSpace,
                DensityUtil.dip2px(5f).toFloat()
            ).toInt()
            gridVerSpace = typeArray.getDimension(
                R.styleable.CalligraphyLayout_gridVerSpace,
                DensityUtil.dip2px(5f).toFloat()
            ).toInt()
            text = typeArray.getString(R.styleable.CalligraphyLayout_android_text).toString()
            textColor =
                typeArray.getColor(R.styleable.CalligraphyLayout_android_textColor, Color.BLACK)
            textSize = typeArray.getDimension(
                R.styleable.CalligraphyLayout_android_textSize,
                DensityUtil.dip2px(20f).toFloat()
            )
            textPadding = typeArray.getDimension(
                R.styleable.CalligraphyLayout_android_padding,
                0f
            ).toInt()
            margin =
                typeArray.getDimension(
                    R.styleable.CalligraphyLayout_android_layout_margin,
                    0f
                ).toInt()
            enableGrid = typeArray.getBoolean(R.styleable.CalligraphyLayout_enableGridBackground, true)
            fontType = typeArray.getInt(R.styleable.CalligraphyLayout_textFontType, 0)
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

    fun setText(text: String) {
        this.text = text
        val layoutP = layoutParams
        layoutP.width = width
        layoutP.height = getTextHeight(width)
        layoutParams = layoutP
        invalidate()
    }

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
        val oneHeight =
            (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top) + textPadding * 2
        val oneSize =
            (textPaint.measureText(text[0].toString()) + textPadding * 2).coerceAtLeast(oneHeight)
        val w = width + margin * 2
        val tW = oneSize + gridHorSpace
        val column =
            (if ((w % tW).toInt() == 0) w / tW else (w / tW)).toInt()
        val row =
            (if ((text.length % column) == 0) text.length / column else (text.length / column) + 1).toInt()
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
        rect.left = col * (oneTextSize + gridHorSpace) + margin
        rect.top = row * (oneTextSize + gridVerSpace) + margin
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


    /**
     * 网格背景样式
     */
    object GridBgStyle {

        /**
         * 田字格
         */
        const val MATTS = 0

        /**
         * 米字格
         */
        const val MIZI = 1


        val fontTypes = arrayOf(
            "kaiti.TTF",
            "kaiti_1.TTF",
            "mircorsoft_kaiti.TTF",
            "ruiyunxingkai.TTF",
            "xingkai.TTF",
            "yuhongling_xingkai.TTF"
        )
    }
}