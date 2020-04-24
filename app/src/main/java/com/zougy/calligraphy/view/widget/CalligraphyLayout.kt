package com.zougy.calligraphy.view.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Scroller
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
    private var layoutType = NORMAL

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

    /**
     * 滑动控件
     */
    private lateinit var scroller: Scroller


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
        scroller = Scroller(context, null, true)
    }

    override fun computeScroll() {
        super.computeScroll()
//        Log.d("CalligraphyLayout", "ZLog computeScroll ${scroller.computeScrollOffset()}")
//        Log.d("CalligraphyLayout", "ZLog computeScroll ${scroller.currX}  ${scroller.currY}")
//        if (scroller.computeScrollOffset()) {
//            scrollTo(scroller.currX, scrollY)
//            invalidate()
//        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmapView = Bitmap.createBitmap(measureBitmapWidth(), measureBitmapHeight(), Bitmap.Config.ARGB_8888)
        bitmapCanvas = Canvas(bitmapView)
        Log.d("CalligraphyLayout", "ZLog onSizeChanged bitmapView:${bitmapView.width} ${bitmapView.height}")
        Log.d("CalligraphyLayout", "ZLog onSizeChanged view:$viewWidth  $viewHeight")
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
                width = if (text.contains("\n")) {
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
                val columns =
                    if (text.contains("\n")) {
                        var colCnt = 0
                        text.split("\n").forEach {
                            if (it.length > textRowCnt)
                                colCnt += if (it.length % textRowCnt == 0) it.length / textRowCnt else it.length / textRowCnt + 1
                            else
                                colCnt++
                        }
                        colCnt
                    } else {//如果不包含手动换行，则直接计算有多少行
                        if (text.length % textRowCnt != 0) text.length / textRowCnt + 1 else text.length / textRowCnt
                    }
                height = columns * (oneTextSize + itemVerSpace) - itemVerSpace
            }
            SINGLE_LINE -> {
                height = oneTextSize + itemVerSpace * 2
            }
            VER_LEFT,
            VER_RIGHT -> {
                height = if (text.contains("\n")) {
                    val maxLen = text.split("\n").maxBy { it.length }!!.length
                    maxLen * (oneTextSize + itemVerSpace)
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
     * 其大小包含了padding和边框线的宽度
     */
    private fun getOneTextSize(): Int {
        val oneTextW = (textPaint.measureText(text[0].toString()) + (textPadding + gridBorderPaint.strokeWidth) * 2).toInt()
        val oneTextH = ((textPaint.fontMetrics.bottom - textPaint.fontMetrics.top) + (textPadding + gridBorderPaint.strokeWidth) * 2).toInt()
        return oneTextH.coerceAtLeast(oneTextW)
    }

    /**
     * 绘制正常模式的text
     */
    private fun drawNormal() {
        val oneTextSize = getOneTextSize()
        val rectF = RectF(0f, 0f, 0f, 0f)
        var rowCnt = 0//当前第几行
        if (text.contains("\n")) {
            text.split("\n").forEach { it ->
                var index = 0//记录当前行绘制到第几列
                it.forEach {
                    rectF.left = (index * (oneTextSize + itemHorSpace)).toFloat()
                    rectF.right = rectF.left + oneTextSize
                    if (rectF.right > viewWidth) {//换行
                        index = 0
                        rowCnt++
                        rectF.left = 0f
                        rectF.right = rectF.left + oneTextSize
                    }
                    rectF.top = (rowCnt * (oneTextSize + itemVerSpace)).toFloat()
                    rectF.bottom = rectF.top + oneTextSize
                    drawOneText(it, bitmapCanvas, rectF)
                    index++
                }
                rowCnt++
            }
        } else {
            var index = 0//记录当前行绘制到第几列
            text.forEach {
                rectF.left = (index * (oneTextSize + itemHorSpace)).toFloat()
                rectF.right = rectF.left + oneTextSize
                if (rectF.right > viewWidth) {//换行
                    index = 0
                    rowCnt++
                    rectF.left = 0f
                    rectF.right = rectF.left + oneTextSize
                }
                rectF.top = (rowCnt * (oneTextSize + itemVerSpace)).toFloat()
                rectF.bottom = rectF.top + oneTextSize
                drawOneText(it, bitmapCanvas, rectF)
                index++
            }
            /*for (i in text.indices) {
                rectF.left = index * (oneTextSize + itemHorSpace) + gridBorderPaint.strokeWidth
                rectF.right = rectF.left + oneTextSize + gridBorderPaint.strokeWidth
                if (rectF.right > viewWidth) {//换行
                    index = 0
                    rowCnt++
                    rectF.left = gridBorderPaint.strokeWidth
                    rectF.right = rectF.left + oneTextSize + gridBorderPaint.strokeWidth
                }
                rectF.top = rowCnt * (oneTextSize + itemVerSpace) + gridBorderPaint.strokeWidth
                rectF.bottom = rectF.top + oneTextSize + gridBorderPaint.strokeWidth
                drawOneText(text[i], bitmapCanvas, rectF)
                index++
            }*/
        }
        bitmapCanvas.drawText("test", 10f, bitmapView.height - 20f, textPaint)
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

    private var touchPointX = 0
    private var touchPointY = 0
    private var moveXCnt = 0
    private var moveYCnt = 0

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (bitmapView.width <= viewWidth && bitmapView.height <= viewHeight) return true
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchPointX = it.x.toInt()
                    touchPointY = it.y.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = it.x.toInt()
                    val y = it.y.toInt()
                    var moveX = touchPointX - x
                    var moveY = touchPointY - y
                    moveXCnt += moveX
                    moveYCnt += moveY
                    val xCannotMove = moveXCnt < 0 || moveXCnt > (bitmapView.width - viewWidth)
                    val yCannotMove = moveYCnt < 0 || moveYCnt > (bitmapView.height - viewHeight)
                    if (bitmapView.width > viewWidth && bitmapView.height <= viewHeight) {//水平滑动
                        if (xCannotMove) {
                            moveXCnt -= moveX
                            return true
                        }
                        moveY = 0
                    } else if (bitmapView.height > viewHeight && bitmapView.width <= viewWidth) {//垂直滑动
                        if (yCannotMove) {
                            moveYCnt -= moveY
                            return true
                        }
                        moveX = 0
                    } else if (bitmapView.width > viewWidth && bitmapView.height > viewHeight) {
                        if (xCannotMove && yCannotMove) {
                            moveXCnt -= moveX
                            moveYCnt -= moveY
                            return true
                        }
                    }
                    scrollBy(moveX, moveY)
                    scroller.startScroll(scroller.currX, scroller.currY, moveX, moveY)
                    touchPointX = x
                    touchPointY = y
                }
            }
        }
        return true
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