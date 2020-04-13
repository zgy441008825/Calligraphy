package com.zougy.calligraphy.dao

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:04/10 0010<br>
 * Email:441008824@qq.com
 */

/**
 * 定义一些常量
 */
object ConsValue {


    object DB {

        /**
         * 数据库版本
         */
        const val DB_VERSION = 1;

        /**
         * 数据库名称
         */
        const val DB_NAME = "calligraphy.db"

        /**
         * 数据库表列名——ID
         */
        const val TAB_COL_NAME_ID = "id"

        /**
         * 书名
         */
        const val TAB_BOOK_NAME = "name"

        /**
         * 作者
         */
        const val TAB_BOOK_AUTHOR = "author"

        /**
         * 时间
         */
        const val TAB_BOOK_YEAR = "year"

        /**
         * 内容
         */
        const val TAB_BOOK_CONTENT = "content"

        /**
         * 词条类型 ${CopybookType}
         */
        const val TAB_BOOK_TYPE = "type"


    }

}