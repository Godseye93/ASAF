package com.d103.asaf.common.model.dto

import android.net.Uri

data class MarketImage(val id : Int, val imageUri: Uri, val postId : Int) {

    constructor(uri: Uri) : this(0, uri, 0)

}
