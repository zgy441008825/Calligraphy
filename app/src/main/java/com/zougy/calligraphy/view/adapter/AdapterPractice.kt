package com.zougy.calligraphy.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zougy.calligraphy.R
import com.zougy.calligraphy.view.widget.CalligraphyView

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:4/16 016<br>
 * Email:441008824@qq.com
 */
class AdapterPractice(showString: MutableList<Char>) :
    BaseQuickAdapter<Char, BaseViewHolder>(R.layout.layout_calligraphy_list_view, showString) {

    override fun convert(holder: BaseViewHolder, item: Char) {
        holder.getView<CalligraphyView>(R.id.layoutCalligraphyTextView).postDelayed({
            holder.getView<CalligraphyView>(R.id.layoutCalligraphyTextView).setText(item.toString())
        }, 10)
    }


}