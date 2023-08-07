package com.d103.asaf.ui.market

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.MarketImage
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentMarketRegisterBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
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
class MarketRegisterFragment : BaseFragment<FragmentMarketRegisterBinding>(FragmentMarketRegisterBinding::bind, R.layout.fragment_market_register) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel : MarketRegisterFragmentViewModel by viewModels()
    private val STORAGE_PERMISSION_CODE = 2 // 원하는 값으로 변경 가능
    private lateinit var adapter : MarketPhotoRegisterAdapter
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                Log.d(TAG, "사진 골랐음: ${it}")
                // 선택한 이미지 URI를 사용하여 이미지뷰에 설정합니다.
//                binding.fragmentMarketRegisterAddImageView.setImageURI(it) // TEST
                val photoImage = MarketImage(it)
                viewModel.photoRegisterList.add(photoImage)
                Log.d(TAG, "데이터 클래스 :  $photoImage")
                Log.d(TAG, "리스트: ${viewModel.photoRegisterList}")
                adapter.submitList(viewModel.photoRegisterList)
            }
        }
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
        Log.d(TAG, "onViewCreated: 다시불림?")
        test()
        init()

    }

    fun test() {
        val test1 = MarketImage(Uri.parse("data:image/jpeg;base64,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"))
        val test2 = MarketImage(Uri.parse("data:image/png;base64,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"))
        viewModel.photoRegisterList.add(test1)
        viewModel.photoRegisterList.add(test2)
    }

    fun init() {

        // 뒤로 가기 버튼
        binding.fragmentMarketRegisterBackButton.setOnClickListener {
            (requireActivity() as MainActivity).showStudentBottomNaviagtionBarFromFragment()
            (requireActivity() as MainActivity).onBackPressed()

        }

        //adpater 설정
        adapter = MarketPhotoRegisterAdapter(requireContext())
        binding.fragmentMarketRegisterRecyclerview.adapter = adapter
        adapter.submitList(viewModel.photoRegisterList)

        // 앨범 접근 - 버튼 클릭 시
        binding.fragmentMarketRegisterAddImageView.setOnClickListener {
            checkAndRequestStoragePermission()
        }




    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        // 이미지를 선택하는 요청을 `ActivityResultLauncher`로 보냅니다.
        imagePickerLauncher.launch(intent)
    }


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
        return RequestBody.create(MEDIA_TYPE_IMAGE, byteArray)
    }

    private fun uploadProfileImage(email: String, imageUri: Uri) {
        val file = File(imageUri.path)
        val profileImagePart = createMultipartFromUri(requireContext(), imageUri)
        val emailRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, email)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "uploadProfileImage: 이미지 확인--------")
                Log.d(TAG, "uploadProfileImage: $email 로 $profileImagePart 보낸다")

                // 서버에 프로필 이미지 업로드 요청
                val response = RetrofitUtil.memberService.uploadProfileImage(emailRequestBody, profileImagePart!!)
                Log.d(TAG, "uploadProfileImage: ${response.errorBody()?.string()}")
                Log.d(TAG, "uploadProfileImage: ${response.body()}")
                if (response.isSuccessful && response.body() != null && response.body() == true) {
                    // 이미지 업로드 성공 처리
                    Log.d(TAG, "uploadProfileImage: 이미지 업로드 성공")
                } else {
                    // 이미지 업로드 실패 처리
                    Log.e(TAG, "uploadProfileImage: 이미지 업로드 실패")
                }
            } catch (e: Exception) {
                // 예외 처리 로직
                Log.e(TAG, "uploadProfileImage: Error", e)
            }
        }
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