package com.zougy.calligraphy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.zougy.calligraphy.view.activity.PracticeActivity
import com.zougy.calligraphy.view.adapter.AdapterPractice
import com.zougy.calligraphy.view.adapter.RecyclerViewDecoration
import com.zougy.views.onClickOnSinge
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "ZLog onCreate ")
        acMainRecyclerView.adapter = AdapterPractice(getString(R.string.testString).toMutableList())
        acMainRecyclerView.layoutManager = GridLayoutManager(this, 6)
        acMainRecyclerView.addItemDecoration(RecyclerViewDecoration(offset = 30, drawColor = Color.BLUE))

        acMainBtStart.onClickOnSinge({
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("text", getString(R.string.testString))
            startActivity(intent)
        })
    }
}