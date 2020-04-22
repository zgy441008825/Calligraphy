package com.zougy.calligraphy.view.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import com.zougy.calligraphy.R
import com.zougy.calligraphy.view.widget.CalligraphyLayout.LayoutType
import com.zougy.calligraphy.view.widget.CalligraphyLayout.LayoutType.NORMAL
import com.zougy.calligraphy.view.widget.CalligraphyLayout.LayoutType.SINGLE_LINE
import com.zougy.calligraphy.view.widget.CalligraphyLayout.LayoutType.VER_LEFT
import com.zougy.calligraphy.view.widget.CalligraphyLayout.LayoutType.VER_RIGHT

/**
 * Description:绘制一段文字，可以是名言警句、诗、词.
 *
 * 通过设置[layoutType]来切换样式。需要注意是，如果不是[LayoutType.NORMAL]，需要换行的地方可手动设置\n来处理。
 *
 * Author:邹高原
 *
 * Date:2020/4/21
 *
 * Email:441008824@qq.com
 */
class CalligraphyLayout : CalligraphyViewOneChar {

    /**
     * 布局模式
     * @see LayoutType
     */
    var layoutType = NORMAL
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 每个字水平间距
     */
    private var itemHorSpace = 0

    /**
     * 每行的间距
     */
    private var itemVerSpace = 0

    /**
     * 绘制View的图片
     */
    private lateinit var bitmapView: Bitmap

    private lateinit var bitmapCanvas: Canvas

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)

    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)
        if (attrs != null) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CalligraphyLayout)
            itemHorSpace = typeArray.getDimension(R.styleable.CalligraphyLayout_gridHorSpace, 0f).toInt()
            itemVerSpace = typeArray.getDimension(R.styleable.CalligraphyLayout_gridVerSpace, 0f).toInt()
            layoutType = typeArray.getInt(R.styleable.CalligraphyLayout_layoutType, NORMAL)
            typeArray.recycle()
        }
        Log.d("CalligraphyLayout", "ZLog initView $itemHorSpace  $itemVerSpace")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmapView = Bitmap.createBitmap(measureBitmapWidth(), measureBitmapHeight(), Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmapView)
        bitmapCanvas.drawColor(Color.YELLOW)
        Log.d("CalligraphyLayout", "ZLog onSizeChanged bitmapView:${bitmapView.width} ${bitmapView.height}")
        layoutText()
    }

    /**
     * 获取图片的宽度
     *
     * 如果是普通模式，宽度为View的宽度，高度可变。
     *
     * 如果是单行模式，宽度为所有文字的宽度。
     *
     * 如果是垂直布局，如果有手动设置\n字符串分割，则根据\n来判断有多少列，宽度为列*每个字的宽度。如果没有设置则宽度为view的宽度。
     */
    private fun measureBitmapWidth(): Int {
        if (TextUtils.isEmpty(text)) return viewWidth
        var width = viewWidth
        val oneTextSize = getOneTextSize()
        when (layoutType) {
            NORMAL -> {
                width = viewWidth
            }
            SINGLE_LINE -> {
                width = (oneTextSize + itemHorSpace) * text.length + marginStart + marginRight
            }
            VER_LEFT,
            VER_RIGHT -> {
                width = if (text.contentEquals("\n")) {
                    val textArr = text.split("\n")
                    (oneTextSize + itemHorSpace) * textArr.size + marginStart + marginRight
                } else {
                    val rowCnt = if (viewHeight % oneTextSize == 0) viewHeight / oneTextSize else viewHeight / oneTextSize - 1
                    (oneTextSize + itemHorSpace) * rowCnt + marginStart + marginRight
                }
            }
        }
        return if (width > viewWidth) width else viewWidth
    }

    /**
     * 获取图片的高度
     *
     * 如果是普通模式，如果包含\n,则进行分组，并且判断每组的字数是否大于一行最多的个数，从而确定多少行，再乘以每个字的高度。
     *
     * 单行模式则是一个字的高度
     *
     * 垂直模式：如果有\n则需要分组，获取最大长度的一组。没有\n直接返回View的高度
     */
    private fun measureBitmapHeight(): Int {
        if (TextUtils.isEmpty(text)) return viewHeight
        var height = viewHeight
        val oneTextSize = getOneTextSize()
        when (layoutType) {
            NORMAL -> {
                //一行显示多少个字
                val textRowCnt = if ((viewWidth % oneTextSize) != 0) viewWidth / oneTextSize - 1 else viewWidth / oneTextSize
                Log.d("CalligraphyLayout", "ZLog measureBitmapHeight textRowCnt:$textRowCnt  viewWidth:$viewWidth  oneTextW:$oneTextSize")
                val columns =
                    if (text.contentEquals("\n")) {
                        val textArr = text.split("\n")
                        var colCnt = 0
                        textArr.filter { it.length > textRowCnt }.forEach {
                            colCnt += if (it.length % textRowCnt == 0) it.length / textRowCnt else it.length % textRowCnt + 1
                        }
                        textArr.size + colCnt
                    } else {//如果不包含手动换行，则直接计算有多少行
                        if (text.length % textRowCnt != 0) text.length / textRowCnt + 1 else text.length / textRowCnt
                    }
                Log.d("CalligraphyLayout", "ZLog measureBitmapHeight columns:$columns oneTextH:$oneTextSize text:${text.length}")
                height = columns * oneTextSize
            }
            SINGLE_LINE -> {
                height = oneTextSize
            }
            VER_LEFT,
            VER_RIGHT -> {
                height = if (text.contentEquals("\n")) {
                    val maxLen = text.split("\n").maxBy { it.length }!!.length
                    maxLen * oneTextSize
                } else {
                    viewHeight
                }
            }
        }
        return if (height > viewHeight) height else viewHeight
    }

    /**
     * 布局文本
     */
    private fun layoutText() {
        when (layoutType) {
            NORMAL -> drawNormal()
            SINGLE_LINE -> drawSingleLine()
            VER_LEFT -> drawVerMode(true)
            VER_RIGHT -> drawVerMode(false)
        }
        invalidate()
    }

    /**
     * 获取一个字占用空间的大小，取宽高的最大值。
     *
     * 其大小包含了左右padding和边框线的宽度
     */
    private fun getOneTextSize(): Int {
        val oneTextW = (textPaint.measureText(text[0].toString()) + textPadding * 2 + textPaint.strokeWidth * 2).toInt()
        val oneTextH = ((textPaint.fontMetrics.bottom - textPaint.fontMetrics.top) + textPadding * 2 + textPaint.strokeWidth * 2).toInt()
        return oneTextH.coerceAtLeast(oneTextW)
    }

    /**
     * 绘制正常模式的text
     */
    private fun drawNormal() {
        val oneTextSize = getOneTextSize()
        if (text.contentEquals("\n")){
            val textArrays = text.split("\n")
            val rectF = RectF()
            var rowCnt = 0
            textArrays.forEach {
                Log.d("CalligraphyLayout", "ZLog drawNormal one line string $it")
                for (i in 0 until it.length){

                }
            }
        }
    }

    /**
     * 绘制单行模式
     */
    private fun drawSingleLine() {

    }

    /**
     * 绘制垂直模式
     * @param left true 从左往右，  false 从右往左
     */
    private fun drawVerMode(left: Boolean) {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmapView, 0f, 0f, gridLinePaint)
    }

    /**
     * 文字布局模式
     */
    object LayoutType {

        /**
         * 布局模式——普通。
         */
        const val NORMAL = 0

        /**
         * 布局模式——单行
         */
        const val SINGLE_LINE = 1

        /**
         * 布局模式——垂直，从左往右
         */
        const val VER_LEFT = 2

        /**
         * 布局模式——垂直，从右往左
         */
        const val VER_RIGHT = 3

    }
}