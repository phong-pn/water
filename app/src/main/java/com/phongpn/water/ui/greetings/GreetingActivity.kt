package com.phongpn.water.ui.greetings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.phongpn.water.R

class GreetingActivity : AppCompatActivity() {
    private val viewModel: GreetingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_greeting, MainGreetingFragment(), null)
            .commit()
    }
}