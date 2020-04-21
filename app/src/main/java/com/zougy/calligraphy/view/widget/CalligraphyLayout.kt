package com.zougy.calligraphy.view.widget

import android.content.Context
import android.util.AttributeSet

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:2020/4/21<br>
 * Email:441008824@qq.com
 */
class CalligraphyLayout : CalligraphyViewOneChar {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(context, attributeSet, defStyle)

    object LayoutType{

        /**
         * 布局模式——普通。
         */
        const val NORMAL = 0

    }
}