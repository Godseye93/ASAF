package com.d103.asaf.ui.bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.d103.asaf.databinding.FragmentBusBinding
import com.d103.asaf.databinding.FragmentMapBinding
import net.daum.mf.map.api.CameraUpdateFactory
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    lateinit var binding: FragmentMapBinding
    lateinit var busBinding : FragmentBusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater)
        busBinding = FragmentBusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = MapView(context)
        binding.mapView.addView(mapView)

        busBinding.fragmentBusMapSeoulBtn.setOnClickListener {
            addMarkerToSeoul()
        }
        busBinding.fragmentBusMapGumiBtn.setOnClickListener {
            addMarkerToGumi()
        }

    }

    // 서울에 마커 추가하는 함수
    fun addMarkerToSeoul() {
        val seoulMarker = MapPOIItem()
        seoulMarker.mapPoint = MapPoint.mapPointWithGeoCoord(37.5012647456244, 127.03958123605)
        mapView.addPOIItem(seoulMarker)
        // 해당 위치로 지도 포커스 이동
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(seoulMarker.mapPoint, -5f))

        // 맵뷰 업데이트
        mapView.invalidate()
    }

    // 구미에 마커 추가하는 함수
    fun addMarkerToGumi() {
        val gumiMarker = MapPOIItem()
        gumiMarker.mapPoint = MapPoint.mapPointWithGeoCoord(36.10716020659603, 128.4162128358147)
        mapView.addPOIItem(gumiMarker)
        // 해당 위치로 지도 포커스 이동
        mapView.moveCamera(CameraUpdateFactory.newMapPoint(gumiMarker.mapPoint, -5f))

        // 맵뷰 업데이트
        mapView.invalidate()
    }
}