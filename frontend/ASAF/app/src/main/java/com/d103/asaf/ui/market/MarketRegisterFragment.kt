package com.d103.asaf.ui.market

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.MarketImage
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentMarketRegisterBinding
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
private const val TAG = "MarketRegisterFragment ASAF"
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarketRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarketRegisterFragment : BaseFragment<FragmentMarketRegisterBinding>(FragmentMarketRegisterBinding::bind, R.layout.fragment_market_register) ,OnImageClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel : MarketRegisterFragmentViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_CODE = 2 // 원하는 값으로 변경 가능
    private lateinit var adapter : MarketPhotoRegisterAdapter
//    private val userProfileImage =
    private val userName =  ApplicationClass.sharedPreferences.getString("memberName")

    private  val selectImagesActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                //If multiple image selected
                if (data?.clipData != null) {

                    val count = data.clipData?.itemCount ?: 0
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        viewModel.photoRegisterList.add(MarketImage(imageUri))
                        val multiPart = createMultipartFromUri(requireContext(), imageUri)
                        Log.d("리스트", "리스트 : $multiPart")
                        if (multiPart != null) {
                            viewModel.photoIamgeFileList.add(multiPart)
                        }
                    }
                    Log.d(TAG, "리스트: ${viewModel.photoIamgeFileList}")
                }
                //If single image selected
                else if (data?.data != null) {
                    val imageUri: Uri? = data.data
                    if(imageUri !=null) {
                        viewModel.photoRegisterList.add(MarketImage(imageUri))
                        val multiPart = createMultipartFromUri(requireContext(), imageUri)
                        Log.d("리스트", "리스트 : $multiPart")
                        if (multiPart != null) {
                            viewModel.photoIamgeFileList.add(multiPart)
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged()

        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.userImage = ApplicationClass.sharedPreferences.getString("profile_image")!!
        Log.d(TAG, "onViewCreated: 다시불림?")
        init()

    }

    fun init() {

        Log.d(TAG, "init:${ApplicationClass.sharedPreferences.getString("profile_image")}  ${ApplicationClass.sharedPreferences.getInt("id")}")

        // 뒤로 가기 버튼
        binding.fragmentMarketRegisterBackButton.setOnClickListener {
            (requireActivity() as MainActivity).showStudentBottomNaviagtionBarFromFragment()
            (requireActivity() as MainActivity).onBackPressed()

        }

        //adpater 설정
        adapter = MarketPhotoRegisterAdapter(viewModel.photoRegisterList, requireContext(), this)
        binding.fragmentMarketRegisterRecyclerview.adapter = adapter






        // 앨범 접근 - 버튼 클릭 시
        binding.fragmentMarketRegisterAddImageButton.setOnClickListener {
            checkAndRequestStoragePermission()
        }
        // 등록하기

        binding.fragmentMarketRegisterButton.setOnClickListener {

            if(binding.fragmentMarketRegisterTitleEdittext.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), "제목을 채워주세요", Toast.LENGTH_SHORT).show()
            }
            else if(binding.marketDetailEdittext.text.isNullOrEmpty()){
                Toast.makeText(requireContext(), "내용을 채워주세요", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.d(TAG, "프로필: ${ApplicationClass.sharedPreferences.getString("profile_image")} ")
                Log.d(TAG, "프로필 2: ${sharedViewModel.userImage}")
                viewModel.marketInfo = Market(System.currentTimeMillis(),
                    binding.fragmentMarketRegisterTitleEdittext.text.toString(),
                    binding.marketDetailEdittext.text.toString(),
                    ApplicationClass.sharedPreferences.getInt("id"),
                    sharedViewModel.userImage,
                    userName!!
                )

//                viewModel.post()
                CoroutineScope(Dispatchers.Main).launch {
                    val response = RetrofitUtil.marketService.post(createMarketRequestBody(viewModel.marketInfo), viewModel.photoIamgeFileList)
                    if(response.isSuccessful){
                        Log.d(TAG, "post: ${response.body()} ")
                        findNavController().navigateUp()
                        (requireActivity() as MainActivity).showStudentBottomNaviagtionBarFromFragment()
                    }
                    else{
                        Log.d(TAG, "post: ${response}")
                    }
                }


            }


        }




    }
    fun createMarketRequestBody(market: Market): RequestBody {
        val json = Gson().toJson(market)
        return json.toRequestBody("application/json".toMediaTypeOrNull())
    }
    override fun onImageClick(position: Int) {
        val item = viewModel.photoRegisterList[position]
//        showImageDialog(item.imageUri)
    }

    override fun onCancelClick(position: Int) {
        viewModel.photoRegisterList.removeAt(position)
        viewModel.photoIamgeFileList.removeAt(position)
        adapter.notifyDataSetChanged()
    }


    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
//
//        // 이미지를 선택하는 요청을 `ActivityResultLauncher`로 보냅니다.
//        imagePickerLauncher.launch(intent)

//        val intent = Intent(ACTION_GET_CONTENT)

//        intent.type = "image/*"
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "image/*"
        selectImagesActivityResult.launch(intent)
    }


    fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        val file: File? = createImageFileFromUri(context, uri)
        Log.d(TAG, "파일 가져오기: $file")
        if (file == null) {
            // 파일을 가져오지 못한 경우 처리할 로직을 작성하세요.
            return null
        }


        val requestFile: RequestBody = createRequestBodyFromFile(file)
        return MultipartBody.Part.createFormData("ImageFiles", file.name, requestFile)
    }



    private fun createRequestBodyFromFile(file: File): RequestBody {
        val MEDIA_TYPE_IMAGE = "image/*".toMediaTypeOrNull()
        val inputStream: InputStream = FileInputStream(file)
        val byteArray = inputStream.readBytes()
        return RequestBody.create(MEDIA_TYPE_IMAGE, byteArray)
    }



    private fun checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 이미 퍼미션을 가지고 있는 경우
            openGalleryForImage()
        } else {
            // 퍼미션을 요청합니다.
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션이 승인된 경우
                openGalleryForImage()
            } else {
                // 퍼미션이 거부된 경우
                Toast.makeText(requireContext(), "갤러리 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    fun createImageFileFromUri(context: Context, uri: Uri): File? {
//        try {
//            val contentResolver: ContentResolver = context.contentResolver
//
//            // Get file name from MediaStore
//            val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
//            val cursor = contentResolver.query(uri, projection, null, null, null)
//            cursor?.use {
//                if (it.moveToFirst()) {
//                    val fileName = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
//                    val tempFile = File(context.cacheDir, fileName)
//                    val outputStream = FileOutputStream(tempFile)
//
//                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
//                    inputStream?.use { input ->
//                        val buffer = ByteArray(4 * 1024) // Adjust buffer size as needed
//                        var bytesRead: Int
//                        while (input.read(buffer).also { bytesRead = it } != -1) {
//                            outputStream.write(buffer, 0, bytesRead)
//                        }
//                    }
//
//                    outputStream.close()
//                    return tempFile
//                }
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return null
//    }

    //version 2
    fun createImageFileFromUri(context: Context, uri: Uri): File? {
        try {
            val contentResolver: ContentResolver = context.contentResolver

            // Get file name from MediaStore
            val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val fileName = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    val tempFile = File(context.cacheDir, fileName)

                    val resizedBitmap = resizeImage(uri, 720, 480) // 이 부분 추가

                    val outputStream = FileOutputStream(tempFile)

                    val byteArray = bitmapToByteArray(resizedBitmap) // 비트맵을 바이트 배열로 변환

                    outputStream.write(byteArray)

                    outputStream.close()
                    return tempFile
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    fun resizeImage(uri: Uri, targetWidth: Int, targetHeight: Int): Bitmap? {
        try {
            val inputStream = context?.contentResolver?.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            val scaleFactor = calculateInSampleSize(options, targetWidth, targetHeight)

            val newOptions = BitmapFactory.Options()
            newOptions.inSampleSize = scaleFactor
            val newInputStream = context?.contentResolver?.openInputStream(uri)
            val resizedBitmap = BitmapFactory.decodeStream(newInputStream, null, newOptions)
            newInputStream?.close()

            val rotation = getRotationFromExif(context, uri) // Exif 회전 정보 가져오기
            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat()) // 회전 정보 적용
            return resizedBitmap?.let { Bitmap.createBitmap(it, 0, 0, resizedBitmap.width, resizedBitmap.height, matrix, true) }

        } catch (e: Exception) {
            Log.e(TAG, "Error while resizing image: ${e.message}")
            return null
        }
    }

    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, stream) // 이미지를 JPEG 형식으로 압축
        return stream.toByteArray()
    }
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }


    fun getRotationFromExif(context: Context?, uri: Uri): Int {
        var rotation = 0
        try {
            val exif = context?.contentResolver?.openInputStream(uri)?.let { ExifInterface(it) }
            val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting exif rotation: ${e.message}")
        }
        return rotation
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MarketRegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MarketRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}