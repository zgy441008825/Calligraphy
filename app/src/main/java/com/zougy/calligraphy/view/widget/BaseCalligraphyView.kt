package com.zougy.calligraphy.view.widget

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.zougy.calligraphy.R
import org.xutils.common.util.DensityUtil

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:2020/4/20<br>
 * Email:441008824@qq.com
 */
abstract class BaseCalligraphyView : View {

    /**
     * 网格边框画笔
     */
    val gridBorderPaint = Paint()

    /**
     * 网格线画笔
     */
    val gridLinePaint = Paint()

    /**
     * 字体画笔
     */
    val textPaint = TextPaint()

    /**
     * 网格线的大小
     */
    private var gridLineWidth = 4f

    /**
     * 斜线大小
     */
    private var gridSlashWidth = 2f

    /**
     * 网格样式
     * @see GridBgStyle
     */
    var gridBgStyle = GridBgStyle.MIZI

    /**
     * 网格颜色
     */
    private var gridColor = Color.parseColor("#C93636")

    /**
     * 显示的文本
     */
    var text = ""

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
    var textPadding = 0

    /**
     * 字体样式
     */
    private var fontType = 0

    /**
     * 是否绘制背景框
     */
    var enableGrid = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    protected open fun initView(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.BaseCalligraphyView)
            gridLineWidth =
                typeArray.getDimension(R.styleable.BaseCalligraphyView_gridLineWidth, gridLineWidth)
            gridSlashWidth =
                typeArray.getDimension(
                    R.styleable.BaseCalligraphyView_gridSlashWidth,
                    gridSlashWidth
                )
            gridBgStyle =
                typeArray.getInt(
                    R.styleable.BaseCalligraphyView_gridBgStyle,
                    gridBgStyle
                )
            gridColor = typeArray.getColor(R.styleable.BaseCalligraphyView_gridColor, gridColor)
            text = typeArray.getString(R.styleable.BaseCalligraphyView_android_text).toString()
            textColor =
                typeArray.getColor(R.styleable.BaseCalligraphyView_android_textColor, textColor)
            textSize = typeArray.getDimension(
                R.styleable.BaseCalligraphyView_android_textSize,
                textSize
            )
            textPadding = typeArray.getDimension(
                R.styleable.BaseCalligraphyView_android_padding,
                textPadding.toFloat()
            ).toInt()
            enableGrid = typeArray.getBoolean(
                R.styleable.BaseCalligraphyView_enableGridBackground,
                enableGrid
            )
            fontType = typeArray.getInt(R.styleable.BaseCalligraphyView_textFontType, fontType)

            typeArray.recycle()
        }

        gridBorderPaint.isAntiAlias = true
        gridBorderPaint.strokeWidth = gridLineWidth
        gridBorderPaint.color = gridColor
        gridBorderPaint.style = Paint.Style.STROKE

        gridLinePaint.isAntiAlias = true
        gridLinePaint.strokeWidth = gridSlashWidth
        gridLinePaint.color = gridColor
        gridLinePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 20f)
        setLayerType(LAYER_TYPE_SOFTWARE, gridLinePaint)

        textPaint.isAntiAlias = true
        textPaint.textSize = textSize
        textPaint.color = textColor
        textPaint.typeface =
            Typeface.createFromAsset(context.assets, GridBgStyle.fontTypes[fontType])
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