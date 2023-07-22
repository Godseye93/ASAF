package com.d103.asaf

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import app.futured.donut.DonutProgressView

class SearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val searchEditText: EditText
    val searchImage: ImageView
    val searchBackground : CardView

    init {
        // Inflate XML layout resource
        inflate(context, R.layout.search_bar, this)

        // Get references to the views within the custom layout
        searchEditText = findViewById(R.id.search_bar_edittext_search)
        searchImage = findViewById(R.id.search_bar_imageview_search)
        searchBackground = findViewById(R.id.search_bar_cardview_search)

        searchImage.setOnClickListener {
            searchBackground.isVisible = !searchBackground.isVisible
            searchEditText.isVisible = !searchEditText.isVisible
        }
    }
}
