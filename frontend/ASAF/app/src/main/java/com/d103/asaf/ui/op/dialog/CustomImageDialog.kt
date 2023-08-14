package com.d103.asaf.ui.op.dialog

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.d103.asaf.R

class CustomImageDialog(context: Context, imageUrl: String) : AlertDialog(context) {

    private val imageView: ImageView
    private val scaleGestureDetector: ScaleGestureDetector
    private val gestureDetector: GestureDetector
    var downX = 0f // 드래그 시작 X 좌표
    var downY = 0f // 드래그 시작 Y 좌표

    init {
        val dialogView = layoutInflater.inflate(R.layout.dialog_sign_image, null)
        setView(dialogView)

        imageView = dialogView.findViewById(R.id.dialogImageView)
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())

        // 이미지 로딩 등의 작업
        Glide.with(dialogView.rootView)
            .load(imageUrl)
            .into(imageView)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // ScaleGestureDetector와 GestureDetector를 함께 사용하여 이미지뷰를 확대 및 축소할 수 있게 함
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        // 드래그 기능 추가
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val currentX = event.rawX
                val currentY = event.rawY
                downX = currentX - imageView.translationX
                downY = currentY - imageView.translationY
            }
            MotionEvent.ACTION_MOVE -> {
                imageView.translationX = event.rawX - downX
                imageView.translationY = event.rawY - downY
            }
        }

        return super.onTouchEvent(event)
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        var scaleFactor = 1.0f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            // 더블 탭 이벤트 처리 (원래 크기로 복원)
            imageView.scaleX = 1.0f
            imageView.scaleY = 1.0f
            return true
        }
    }
}