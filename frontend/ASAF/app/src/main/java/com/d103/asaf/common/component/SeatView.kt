package com.d103.asaf.common.component

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.d103.asaf.R

class SeatView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val seatText: TextView
    val seatImage: ImageView

    init {
        // Inflate XML layout resource
        inflate(context, R.layout.seatview, this)
        // Get references to the views within the custom layout
        seatText = findViewById(R.id.item_seat_textview_text)
        seatImage = findViewById(R.id.item_seat_imageview_image)
    }

}
