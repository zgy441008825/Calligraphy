package com.zougy.calligraphy

import android.app.Application
import org.xutils.x

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:04/03 0003<br>
 * Email:441008824@qq.com
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        x.Ext.init(this)
    }
}