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
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentSignBinding
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
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
    private var signMonth = arguments?.getString("signMonth") ?: "0"
    private var totDay = arguments?.getString("totDay") ?: "0"
    private var attDay = arguments?.getString("attDay") ?: "0"
    private var subMonth = arguments?.getString("subMonth")
    private var subDay = arguments?.getString("subDay")

    val today = todayToString()
    val year = today[0]
    //val month = signMonth
    private val name = ApplicationClass.sharedPreferences.getString("memberName")
    private val classCode = SignDrawFragment.myClass?.classCode
    private var curSign = DocSign()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signMonth = requireArguments().getString("signMonth") ?: "1"
        totDay = requireArguments().getString("totDay") ?: "1"
        attDay = requireArguments().getString("attDay") ?: "1"
        subMonth = requireArguments().getString("subMonth") ?: "1"
        subDay = requireArguments().getString("subDay") ?: "1"

        curSign = SignDrawFragment.myClass?.let { DocSign(0, it.classNum,it.classCode,it.regionCode,it.generationCode,
            it.userId,"",name?:"",addZero(signMonth)) } ?: DocSign()

        Log.d("지금사인", "onCreate: $curSign")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        draw = binding.fragmentSignConfirmDraw
        draw.setSign(SignDrawFragment.draw?.getSign() ?: listOf(),"SignFragment")

        Log.d("보내는사인", "보내는사인: $curSign")

        initView()
        initEvent()
    }

    private fun initView() {
        setUserInfo()
    }

    private fun setUserInfo() {
        Log.d("이름", "setUserInfo: $name")

        // 유저 정보 모두 필요
        document = Document(signMonth, name?:"", SignDrawFragment.regionName, classCode.toString(), "", "", "12", "29")
        Log.d("사인", "setUserInfo: $totDay $attDay")
        binding.apply {
            fragmentSignConfirmTvYear.text = year
            fragmentSignConfirmTvMonth1.text = signMonth
            fragmentSignConfirmTvMonth2.text = signMonth
            fragmentSignConfirmTvMonth3.text = signMonth
            fragmentSignConfirmTvMonth4.text = subMonth

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
            //Request_Capture(binding.fragmentSignConfirmDocument, current_time + "_capture");
            requestCapture(binding.fragmentSignConfirmDocument, "${document.campus}_${document.class_}반_${document.name}",curSign)
            findNavController().navigate(R.id.action_signFragment_to_signDrawFragment)
        }
    }

    // 특정 레이아웃 캡쳐해서 저장하기
    private fun requestCapture(view: View?, title: String, curSing: DocSign) {
        if (view == null) { // Null Point Exception ERROR 방지
            println("::::ERROR:::: view == NULL")
            return
        }

        /* 캡쳐 파일 저장 */
        val bitmap =  view.drawToBitmap()
        var fos: OutputStream? = null
        var image: File? = null
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                image = File(imagesDir, "$title.png")

                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            image = File(imagesDir, "$title.png")
            fos = FileOutputStream(image,false)
        }

        Log.d("서명저장", "Request_Capture: ${Environment.DIRECTORY_PICTURES} ${MediaStore.MediaColumns.RELATIVE_PATH}")
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            CoroutineScope(Dispatchers.IO).launch{
                try {
                    val response = withContext(Dispatchers.IO) {
                        uploadSigns(curSign,image!!)
                    }
                    if (response.isSuccessful) {
                        Log.d("서명보내기", "requestCapture: 서명 보내기 완료")
                        try {
                            Log.d("서명보내기이미지", "requestCapture: $image")
                            if(image != null && (image as File).exists()) (image as File).delete()
                        }catch (e:Exception) {
                            Log.e("파일없음", "파일없음 삭제 오류: ${e.message}", e)
                        }

                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(requireContext(), "서명 보내기 네트워크 오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("서명", "서명 오류: ${e.message}", e)
                }
            }
        }
    }

    private fun todayToString(): List<String> {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        return formatter.format(calendar.time).split("/")
    }

    // ---------------------------------------- 서명 업로드 ---------------------------------
    suspend fun uploadSigns(sign: DocSign, file: File): Response<Boolean> {
        Log.d("보내는사인 정보", "uploadSigns: $sign")
        val signsJson = Gson().toJson(sign) // Convert signs list to JSON string

        val signsRequestBody = signsJson.toRequestBody("application/json".toMediaTypeOrNull())
        val filePart = createMultipartFromUri(file)

        return RetrofitUtil.opService.postSigns(signsRequestBody, filePart)
    }

    fun createMultipartFromUri(file: File): MultipartBody.Part {
        val requestFile: RequestBody = createRequestBodyFromFile(file)
        return MultipartBody.Part.createFormData("ImageFile", file.name, requestFile)
    }

    private fun createRequestBodyFromFile(file: File): RequestBody {
        val MEDIA_TYPE_IMAGE = "image/*".toMediaTypeOrNull()
        val inputStream: InputStream = FileInputStream(file)
        val byteArray = inputStream.readBytes()
        return RequestBody.create(MEDIA_TYPE_IMAGE, byteArray)
    }

    private fun addZero(curM: String): String {
        if(curM.length < 2) return "0$curM"
        else return "$curM"
    }
}