package com.d103.asaf.ui.schedule

import com.d103.asaf.common.model.dto.Noti

interface NotiUpdateListener {
    fun onNotiUpdate(noti: Noti)
}