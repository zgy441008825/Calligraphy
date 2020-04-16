package com.zougy.calligraphy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zougy.calligraphy.view.activity.PracticeActivity
import com.zougy.views.onClickOnSinge
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        acMainBtStart.onClickOnSinge({
            val intent = Intent(this, PracticeActivity::class.java)
            intent.putExtra("text", acMainCallView.getText())
            startActivity(intent)
        })
        acMainCallView.postDelayed({
            acMainCallView.setText(getString(R.string.testString))
        }, 10)
    }
}