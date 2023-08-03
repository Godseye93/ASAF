package com.d103.asaf.ui.schedule

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.TextView
import com.d103.asaf.R

class NotiCustomDialog(context : Context, val detail : String, val title : String) : Dialog(context, R.style.CustomDialogStyle) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom_notil)
        val titleText = findViewById<TextView>(R.id.dailog_title)
        val contentText = findViewById<TextView>(R.id.dailog_noti_detail_textView)
        titleText.text = title
        contentText.text = detail
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val radius = 70f
        val backgroundDrawable = GradientDrawable()

        backgroundDrawable.setColor(Color.WHITE)
        backgroundDrawable.cornerRadius = radius
        window?.setBackgroundDrawable(backgroundDrawable)

        setCanceledOnTouchOutside(true)
    }
}