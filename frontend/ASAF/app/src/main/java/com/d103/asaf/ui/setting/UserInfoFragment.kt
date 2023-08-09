package com.d103.asaf.ui.setting

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.api.MemberService
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentSettingUserinfoBinding
import com.ssafy.template.util.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

private const val TAG = "UserInfoFragment_cjw"
class UserInfoFragment: Fragment() {
    private var _binding: FragmentSettingUserinfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserInfoFragmentViewModel
    private val sharedViewModel: SharedViewModel by viewModels()

    // profile image 관련
    private lateinit var tempDate: String
    private lateinit var tempUri : Uri
    private val STORAGE_PERMISSION_CODE = 1 // 원하는 값으로 변경 가능
    // 이미지 선택을 위한 ActivityResultLauncher 선언
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                Log.d(TAG, "URI-JoIN: $uri")
                // 선택한 이미지 URI를 사용하여 이미지뷰에 설정합니다.
                binding.fragmentSettingUserinfoImageviewProfile.setImageURI(uri)
                tempUri = uri
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingUserinfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInfo()

        //
        binding.fragmentSettingUserinfoButtonUpdate.setOnClickListener {
            // 프로필 사진 delete

            // 프로필 사진 update
//            uploadProfileImage(ApplicationClass.sharedPreferences.getString("memberEmail")!!, tempUri)
            lifecycleScope.launch {
                uploadProfileImage(ApplicationClass.sharedPreferences.getString("memberEmail")!!, tempUri)
//                Toast.makeText(requireContext(), "프로필 이미지를 변경했습니다.", Toast.LENGTH_SHORT).show()
                // login fragment로 이동.
                findNavController().navigateUp()
            }


            // 회원정보 update


            initInfo()
        }

        binding.fragmentSettingUserinfoButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.fragmentSettingUserinfoImageviewProfile.setOnClickListener {
            checkAndRequestStoragePermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰 바인딩 해제
        _binding = null
    }

    fun initInfo(){
        binding.fragmentSettingUserinfoEditTVName.setText("${ApplicationClass.sharedPreferences.getString("memberName")}")
        binding.fragmentSettingUserinfoEditTVEmail.setText("${ApplicationClass.sharedPreferences.getString("memberEmail")}")
        binding.fragmentSettingUserinfoEditTVPass.setText("${ApplicationClass.sharedPreferences.getString("memberPassword")}")
        binding.fragmentSettingUserinfoEditTVBirth.setText("${ApplicationClass.sharedPreferences.getString("birth_date")}")
        val imageUrl = "${ApplicationClass.API_URL}member/${ApplicationClass.sharedPreferences.getString("memberEmail")}/profile-image"
        Log.d(TAG, "initInfo___: $imageUrl")
        val requestOptions = RequestOptions().transform(CircleCrop())
        Glide.with(this)
            .load(imageUrl)
            .apply(requestOptions)
            .into(binding.fragmentSettingUserinfoImageviewProfile)
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

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        // 이미지를 선택하는 요청을 `ActivityResultLauncher`로 보냅니다.
        imagePickerLauncher.launch(intent)
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
                if (response.isSuccessful && response.body() == true) {
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
        Log.d(TAG, "URI-join:$uri")
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
}