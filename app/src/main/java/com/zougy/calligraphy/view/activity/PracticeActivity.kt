package com.zougy.calligraphy.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zougy.calligraphy.R
import com.zougy.calligraphy.view.adapter.AdapterPractice
import kotlinx.android.synthetic.main.activity_practice.*

class PracticeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)
        val showStrings = intent.getStringExtra("text")
        if (showStrings == null || showStrings.isEmpty()) return
        acPracticeRecyclerView.adapter = AdapterPractice(showStrings.toMutableList())
        acPracticeRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        acPracticeCalligraphyView.postDelayed({
            acPracticeCalligraphyView.setShowText(showStrings[0].toString())
        }, 1)
    }
}
