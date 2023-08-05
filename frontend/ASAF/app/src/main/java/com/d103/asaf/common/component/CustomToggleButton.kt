package com.d103.asaf.common.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.d103.asaf.R

class CustomToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val seatText: TextView
    val lockerText: TextView
    val moneyText : TextView
    val cardViewFocus : CardView

    init {
        inflate(context, R.layout.toggle_button, this)

        cardViewFocus = findViewById(R.id.cardViewFocus)
        seatText = findViewById<TextView>(R.id.seatText)
        lockerText = findViewById<TextView>(R.id.lockerText)
        moneyText = findViewById<TextView>(R.id.moneyText)

        seatText.setOnClickListener {
            moveViewToTarget(cardViewFocus, seatText)
        }

        lockerText.setOnClickListener {
            moveViewToTarget(cardViewFocus, lockerText)
        }

        moneyText.setOnClickListener {
            moveViewToTarget(cardViewFocus, moneyText)
        }
    }

    fun setFirstButtonClickListener(function: () -> Unit) {
        seatText.setOnClickListener {
            function()
            moveViewToTarget(cardViewFocus, seatText)
        }
    }

    fun setSecondButtonClickListener(function: () -> Unit) {
        lockerText.setOnClickListener {
            function()
            moveViewToTarget(cardViewFocus, lockerText)
        }
    }

    fun setThirdButtonClickListener(function: () -> Unit) {
        moneyText.setOnClickListener {
            function()
            moveViewToTarget(cardViewFocus, moneyText)
        }
    }
    private fun moveViewToTarget(movingView: View, targetView: TextView) {
        // targetView의 좌표와 movingView의 좌표를 구함
        val targetX = targetView.x + 5f
        val movingX = movingView.x

        // movingView와 targetView 사이의 거리를 계산
        val distanceX = targetX - movingX

        // 이동 애니메이션 생성
        val translateAnimation = TranslateAnimation(0f, distanceX, 0f, 0f)
        translateAnimation.duration = 100

        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                seatText.isClickable = true
                lockerText.isClickable = true
                moneyText.isClickable = true
                // 선택된 뷰는 클릭 불가능하게
                targetView.isClickable = false
            }

            override fun onAnimationEnd(animation: Animation) {
                // 애니메이션이 종료된 후에 실행되는 콜백 메서드
                movingView.clearAnimation() // 애니메이션을 제거하여 원래 위치에 고정

                movingView.x = targetX

                seatText.setTextColor(Color.parseColor("#9B9B9B"))
                moneyText.setTextColor(Color.parseColor("#9B9B9B"))
                lockerText.setTextColor(Color.parseColor("#9B9B9B"))

                targetView.setTextColor(Color.parseColor("#5669FF"))
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // 뷰에 애니메이션 적용
        movingView.startAnimation(translateAnimation)
    }
}