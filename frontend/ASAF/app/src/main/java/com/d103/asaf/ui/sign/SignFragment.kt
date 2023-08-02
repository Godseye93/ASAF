package com.d103.asaf.ui.sign

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentSignBinding
import com.d103.asaf.databinding.FragmentSignNextBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SignFragment : BaseFragment<FragmentSignBinding>(FragmentSignBinding::bind, R.layout.fragment_sign) {
    lateinit var draw: DrawSign
    lateinit var document: Document

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        draw = binding.fragmentSignConfirmDraw

        initView()
        initEvent()
    }

    private fun initView() {
        setUserInfo()
    }

    private fun setUserInfo() {
        val today = todayToString()
        val year = today[0]
        // 유저 정보 모두 필요
        document = Document(today[1], "박현우", "부울경", "11", "", "", "12", "29")

        binding.apply {
            fragmentSignConfirmTvYear.text = year
            fragmentSignConfirmTvMonth1.text = document.month
            fragmentSignConfirmTvMonth2.text = document.month
            fragmentSignConfirmTvMonth3.text = document.month
            fragmentSignConfirmTvMonth4.text = document.subMonth
            fragmentSignConfirmTvDay.text = document.subDay
            fragmentSignConfirmTvCampus.text = document.campus
            fragmentSignConfirmTvClass.text = document.class_
            fragmentSignConfirmTvName1.text = document.name
            fragmentSignConfirmTvName2.text = document.name
            fragmentSignConfirmTvName3.text = document.name
            fragmentSignConfirmTvClassDay.text = document.classDay
            fragmentSignConfirmTvAttendDay.text = document.attendDay
        }
    }

    private fun initEvent() {
        binding.fragmentSignConfirmBtnSave.setOnClickListener {
            val sdf = SimpleDateFormat("yyyyMMddHHmmss") //년,월,일,시간 포멧 설정
            val time = Date() //파일명 중복 방지를 위해 사용될 현재시간
            val current_time = sdf.format(time) //String형 변수에 저장
            //Request_Capture(binding.fragmentSignConfirmDocument, current_time + "_capture");

            request_Capture(binding.fragmentSignConfirmDocument, "${document.campus}_${document.class_}반_${document.name}");
        }
    }

    // 특정 레이아웃 캡쳐해서 저장하기
    private fun request_Capture(view: View?, title: String) {
        if (view == null) { // Null Point Exception ERROR 방지
            println("::::ERROR:::: view == NULL")
            return
        }

        /* 캡쳐 파일 저장 */
        val bitmap =  view.drawToBitmap()

        var fos: OutputStream? = null

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, "$title.png")
            fos = FileOutputStream(image)
        }
        Log.d("서명저장", "Request_Capture: ${Environment.DIRECTORY_PICTURES} ${MediaStore.MediaColumns.RELATIVE_PATH}")
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    private fun todayToString(): List<String> {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        return formatter.format(calendar.time).split("/")
    }
}