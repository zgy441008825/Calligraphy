package com.zougy.calligraphy.dao;

import java.io.Serializable;

/**
 * Description:词条类型，包含常见的诗、词、散文、名句<br>
 * Author:邹高原<br>
 * Date:04/10 0010<br>
 * Email:441008824@qq.com
 */
public class CopybookType implements Serializable {

    /**
     * 诗
     */
    public static final int poem = 0;

    /**
     * 词
     */
    public static final int poetry = 1;

    /**
     * 散文
     */
    public static final int prose = 2;

    /**
     * 名句
     */
    public static final int rhesis = 3;

}
