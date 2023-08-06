package com.d103.asaf.ui.library.adapter

import com.d103.asaf.common.model.dto.Book

interface NavigationListener {
    fun navigateToDestination(book: Book)
}