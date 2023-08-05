package com.d103.asaf.ui.schedule

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.common.model.dto.Noti

class NotiCustomDialog(context : Context, var noti : Noti, var fragment : Fragment) : Dialog(context, R.style.CustomDialogStyle) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_custom_noti)
        val titleText = findViewById<EditText>(R.id.dailog_title_udpate)
        val contentText = findViewById<EditText>(R.id.noti_detail_edittext_update)
        titleText.setText(noti.title)
        contentText.setText(noti.content)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val radius = 70f
        val backgroundDrawable = GradientDrawable()

        backgroundDrawable.setColor(Color.WHITE)
        backgroundDrawable.cornerRadius = radius
        window?.setBackgroundDrawable(backgroundDrawable)

        val updateButton = findViewById<Button>(R.id.dailog_noti_button_update)
        updateButton.setOnClickListener {
            var newNoti  = noti.copy()
            newNoti.title = titleText.text.toString()
            newNoti.content = contentText.text.toString()
            (fragment as ScheduleFragment).onNotiUpdate(newNoti)
            dismiss()
        }

        setCanceledOnTouchOutside(true)
    }
}