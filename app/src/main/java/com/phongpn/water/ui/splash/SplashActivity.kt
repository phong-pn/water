package com.phongpn.water.ui.splash

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.lifecycle.lifecycleScope
import com.phongpn.water.R
import com.phongpn.water.ui.MainActivity
import com.phongpn.water.ui.greetings.GreetingActivity
import com.phongpn.water.util.profileparams.AppSetting
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.S) {
            installSplashScreen()
//            setContentView(R.layout.activity_splash)
            continueApp()
        }
        else {
            setContentView(R.layout.activity_splash)
            icon_launcher.apply {
                animation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.icon_splash_anim)
                animate().withEndAction {
                    continueApp()
                }.setDuration(1000).start()
            }
        }
    }
    private fun continueApp(){
        if (AppSetting.getInstance().firstLaunch) {
            startActivity(Intent(this@SplashActivity, GreetingActivity::class.java))
        }
        else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
        finish()
    }
}