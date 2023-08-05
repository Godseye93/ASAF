package com.d103.asaf.ui.library

import android.Manifest
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.Region

object Constants {
    const val PERMISSION_REQUEST_CODE = 8

    val RUNTIME_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT
    )
    const val BEACON_UUID = "FDA50693-A4E2-4FB1-AFCF-C6EB07647825"
    const val BEACON_MAJOR = "10004"
    const val BEACON_MINOR = "54480"
    const val BEACON_MAC = "00:81:F9:44:39:58"

    // Beacon의 Region 설정
    // 비교데이터들로, 설치 지역이 어딘지 판단하기 위한 데이터.
    //estimote : apple, eddystone : google
    val REGIONS = Region(
        "estimote",
        listOf(
            Identifier.parse(BEACON_UUID),
            Identifier.parse(BEACON_MAJOR),
            Identifier.parse(BEACON_MINOR)),
        BEACON_MAC
    )
    const val BEACON_DISTANCE = 5 // 50cm 이내
}