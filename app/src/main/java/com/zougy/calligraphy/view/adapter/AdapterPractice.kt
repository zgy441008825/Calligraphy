package com.zougy.calligraphy.view.adapter

import android.util.Log
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zougy.calligraphy.R
import com.zougy.calligraphy.view.widget.CalligraphyViewOneChar

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:4/16 016<br>
 * Email:441008824@qq.com
 */
class AdapterPractice(showString: MutableList<Char>) :
    BaseQuickAdapter<Char, BaseViewHolder>(R.layout.layout_calligraphy_list_view, showString) {

    override fun convert(holder: BaseViewHolder, item: Char) {
        holder.getView<CalligraphyViewOneChar>(R.id.layoutCalligraphyTextView).setShowText(item.toString())
    }

}