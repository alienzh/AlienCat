package com.zhangw.aliencat.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.zhangw.aliencat.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Example of a call to a native method
        sample_text.text = "alienCat"

        sample_text.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@WelcomeActivity, KpActivity::class.java))
        })
    }
}
