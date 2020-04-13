package com.zougy.calligraphy.dao

import com.zougy.calligraphy.dao.ConsValue.DB.DB_NAME
import com.zougy.calligraphy.dao.ConsValue.DB.DB_VERSION
import com.zougy.dao.BaseDBMgr

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:04/10 0010<br>
 * Email:441008824@qq.com
 */
class CalligraphyDBMrg : BaseDBMgr(DB_NAME, DB_VERSION, null) {

    class CalligraphyDBMrg private constructor() {
        companion object {
            val instance: CalligraphyDBMrg by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                CalligraphyDBMrg()
            }
        }
    }

}