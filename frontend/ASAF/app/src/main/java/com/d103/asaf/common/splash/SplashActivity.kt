package com.d103.asaf.common.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val splashAnim: LottieAnimationView = findViewById(R.id.loading_image)

//        // raw 리소스 폴더에 있는 Lottie 애니메이션 파일의 이름들을 리스트로 가져옵니다.
//        val animationNames = mutableListOf(R.raw.female, R.raw.male)
//
//        // 랜덤하게 Lottie 애니메이션 파일을 선택합니다.
//        animationNames.shuffle()

        splashAnim.setAnimation(R.raw.asafsplash)

        splashAnim.playAnimation() // 애니메이션 재생

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1200)
    }
}