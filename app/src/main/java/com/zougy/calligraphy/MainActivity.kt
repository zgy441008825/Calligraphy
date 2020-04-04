package com.zougy.calligraphy

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zougy.views.onClickOnSinge
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainAcBt.onClickOnSinge({
            mainAcViewText.setText("123")
        })
    }
}
