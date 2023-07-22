package com.d103.asaf.common.component

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.R
import com.d103.asaf.common.component.adapter.DropdownAdapter

class Dropdown @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var isClicked:Boolean = true
) : LinearLayout(context, attrs, defStyleAttr) {

    val dropdownText: TextView
    val dropdownBtn: ImageView
    val dropdownList: RecyclerView
    var dataList = mutableListOf("2반", "3반", "4반", "5반")
    val adapter : DropdownAdapter
    init {
        // 사이즈 2칸 고정

        // Inflate XML layout resource
        inflate(context, R.layout.dropdown, this)

        // Get references to the views within the custom layout
        dropdownText = findViewById(R.id.dropdown_textview_text)
        dropdownBtn = findViewById(R.id.dropdown_imageview_button)
        dropdownList = findViewById(R.id.dropdown_recyclerview)
        adapter = DropdownAdapter(dataList, this)
        dropdownList.adapter = adapter

        dropdownBtn.setOnClickListener {
            dropdownList.isVisible = !dropdownList.isVisible
            if(isClicked) dropdownBtn.setImageResource(R.drawable.dropdown_arrow_up)
            else dropdownBtn.setImageResource(R.drawable.dropdown_arrow)
            isClicked = !isClicked
            dataList.sort()
            dropdownList.scrollToPosition(0)
            adapter.notifyDataSetChanged()
            false
        }
    }
}
