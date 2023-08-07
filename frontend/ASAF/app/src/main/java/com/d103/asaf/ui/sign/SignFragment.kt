package com.d103.asaf.ui.sign

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.d103.asaf.R
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentSignBinding
import com.d103.asaf.databinding.FragmentSignNextBinding
import com.d103.asaf.ui.join.TAG
import com.d103.asaf.ui.library.student.LibraryUseDrawFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SignFragment : BaseFragment<FragmentSignBinding>(FragmentSignBinding::bind, R.layout.fragment_sign) {
    companion object {
        fun instance(signMonth: String, totDay: String, attDay: String, subMonth: String, subDay: String): SignDrawFragment {
            val fragment = SignDrawFragment()
            val args = Bundle()
            args.putString("signMonth", signMonth)
            args.putString("totDay", totDay)
            args.putString("attDay", attDay)
            args.putString("subMonth", subMonth)
            args.putString("subDay", subDay)
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var draw: DrawSign
    lateinit var document: Document
    private var signMonth = ""
    private var totDay = ""
    private var attDay = ""
    private var subMonth = ""
    private var subDay = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signMonth = requireArguments().getString("signMonth") ?: "1"
        totDay = requireArguments().getString("totDay") ?: "1"
        attDay = requireArguments().getString("attDay") ?: "1"
        subMonth = requireArguments().getString("subMonth") ?: "1"
        subDay = requireArguments().getString("subDay") ?: "1"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        draw = binding.fragmentSignConfirmDraw
        draw.setSign(SignDrawFragment.draw?.getSign() ?: listOf(),"SignFragment")

        initView()
        initEvent()
    }

    private fun initView() {
        setUserInfo()
    }

    private fun setUserInfo() {
        val today = todayToString()
        val year = today[0]
        val name = ApplicationClass.sharedPreferences.getString("memberName")
        val classCode = SignDrawFragment.myClass?.classCode
        Log.d("이름", "setUserInfo: $name")

        // 유저 정보 모두 필요
        document = Document(today[1], name?:"", SignDrawFragment.regionName, classCode.toString(), "", "", "12", "29")
        Log.d("사인", "setUserInfo: $totDay $attDay")
        binding.apply {
            fragmentSignConfirmTvYear.text = year
            fragmentSignConfirmTvMonth1.text = signMonth
            fragmentSignConfirmTvMonth2.text = signMonth
            fragmentSignConfirmTvMonth3.text = signMonth
            fragmentSignConfirmTvMonth4.text = signMonth

            fragmentSignConfirmTvDay.text = subDay
            fragmentSignConfirmTvCampus.text = document.campus // 캠퍼스 정보
            fragmentSignConfirmTvClass.text = document.class_ // 반정보

            fragmentSignConfirmTvName1.text = document.name // 유저 이름
            fragmentSignConfirmTvName2.text = document.name
            fragmentSignConfirmTvName3.text = document.name

            fragmentSignConfirmTvClassDay.text = totDay
            fragmentSignConfirmTvAttendDay.text = attDay
        }
    }

    private fun initEvent() {
        binding.fragmentSignConfirmBtnSave.setOnClickListener {
            val sdf = SimpleDateFormat("yyyyMMddHHmmss") //년,월,일,시간 포멧 설정
            val time = Date() //파일명 중복 방지를 위해 사용될 현재시간
            val current_time = sdf.format(time) //String형 변수에 저장
            //Request_Capture(binding.fragmentSignConfirmDocument, current_time + "_capture");

            requestCapture(binding.fragmentSignConfirmDocument, "${document.campus}_${document.class_}반_${document.name}");
        }
    }

    // 특정 레이아웃 캡쳐해서 저장하기
    private fun requestCapture(view: View?, title: String) {
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

    // ---------------------------------------- 서명 업로드 ---------------------------------
    fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        val file: File? = getFileFromUri(context, uri)
        if (file == null) {
            // 파일을 가져오지 못한 경우 처리할 로직을 작성하세요.
            return null
        }
        val requestFile: RequestBody = createRequestBodyFromFile(file)
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        val filePath = uriToFilePath(context, uri)
        return if (filePath != null) File(filePath) else null
    }

    private fun uriToFilePath(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return filePath
    }

    private fun createRequestBodyFromFile(file: File): RequestBody {
        val MEDIA_TYPE_IMAGE = "image/png".toMediaTypeOrNull()
        val inputStream: InputStream = FileInputStream(file)
        val byteArray = inputStream.readBytes()
        return byteArray.toRequestBody(MEDIA_TYPE_IMAGE)
    }
}