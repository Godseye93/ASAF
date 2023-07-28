package com.d103.asaf.common.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import app.futured.donut.DonutProgressView
import com.d103.asaf.R

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


        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideSoftKeyboard(v)
            }
        }
        searchImage.setOnClickListener {
            hideSearch()
        }
    }
    private fun hideSoftKeyboard(view: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun hideSearch() {
        searchBackground.isVisible = !searchBackground.isVisible
        searchEditText.isVisible = !searchEditText.isVisible
    }

    fun setSearchClickListener(function: () -> Unit) {
        searchImage.setOnClickListener {
            hideSearch()
            function()
        }
    }
}
