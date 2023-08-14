package com.d103.asaf.ui.join

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d103.asaf.MainActivity
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.common.util.RetrofitUtil.Companion.memberService
import com.d103.asaf.databinding.FragmentJoinBinding
import com.d103.asaf.ui.sign.SignDrawFragment.Companion.regionCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.Calendar

private const val TAG = "JoinFragment_cjw"
class JoinFragment : Fragment() {

    private lateinit var binding: FragmentJoinBinding
    private val viewModel: JoinFragmentViewModel by viewModels()
//    private lateinit var loginFragment: LoginFragment
    private lateinit var tempDate: String

    var generationCode : Int = 0
    var regionCode : Int = 0
    var classCode : Int = 0
//    val generationCode = binding.spinnerNth.selectedItem.toString().toInt()
//    val regionCode = binding.spinnerRegion.selectedItem.toString().toInt()
//    val classCode = binding.spinnerClassNum.selectedItem.toString().toInt()

    private var tempUri : Uri? = null
    private val STORAGE_PERMISSION_CODE = 1 // 원하는 값으로 변경 가능
    // 이미지 선택을 위한 ActivityResultLauncher 선언
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                Log.d(TAG, "URI-JoIN: $uri")
                // 선택한 이미지 URI를 사용하여 이미지뷰에 설정합니다.
                binding.fragmentJoinImageviewProfile.setImageURI(uri)
                tempUri = uri
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        loginFragment = context as LoginFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view 초기 설정
        setupViews()
        // Spinner default 값을 설정하는 메서드를 호출
        setSpinnerDefaultValues()
    }

    private fun setupViews() {

        // information spinner adapter
        setSpinnerAdapters()

        // 뒤로가기 버튼 클릭 시,
        binding.fragmentJoinButtonBack.setOnClickListener {
            findNavController().navigateUp()
//            findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
        }
        // 생년월일 클릭 시 달력 표시
        binding.fragmentJoinEditTVBirth.setOnClickListener {
            showDatePickerDialog()
        }

        binding.fragmentJoinLayoutBirth.setOnClickListener{
            showDatePickerDialog()
        }

        binding.fragmentJoinButtonSignup.setOnClickListener {
            val name = binding.fragmentJoinEditTVName.text.toString()
            val email = binding.fragmentJoinEditTVEmail.text.toString()
            val password = binding.fragmentJoinEditTVPass.text.toString()
            val confirmPassword = binding.fragmentJoinEditTVPassConfirm.text.toString()
            val birth = binding.fragmentJoinEditTVBirth.text.toString()
            val information = "${binding.spinnerNth.selectedItem}${binding.spinnerRegion.selectedItem}${binding.spinnerClassNum.selectedItem}"
            val studentNumber = binding.fragmentJoinEditTVStudentNumber.text.toString()
            val phoneNumber = binding.fragmentJoinEditTVPhoneNumber.text.toString()

            val member = Member(studentNumber.toInt(), name, email, password, birth, information, phoneNumber)
//            member.token = ApplicationClass.sharedPreferences.getString("token")!!

            if (viewModel.validateInputs(member, confirmPassword, tempUri)) {
                // 이메일 중복 확인
                lifecycleScope.launch {
                    val isEmailDuplicated =
                        withContext(Dispatchers.Default) {
                            checkDuplicateEmail(email)
                        }

                    if (!isEmailDuplicated) {
                        // 이메일이 중복되는 경우
                        Log.d(TAG, "onSignupButtonClick: 중복된 이메일이 존재합니다.")
                        Toast.makeText(requireContext(), "이미 등록된 이메일입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 이미지를 서버로 업로드하는 로직 호출
                        Log.d(TAG, "setupViews: 이미지 null이니? : $tempUri")

                        // 이메일이 중복되지 않는 경우
                        // 회원가입 로직을 처리하고, 뷰를 변경하거나 다른 작업을 수행할 수 있습니다.
                        // viewModel.signup(name, email, password, birth, information)
                        Log.d(TAG, "onSignupButtonClick: 사용 가능한 이메일입니다.")

                        // 뷰모델의 회원가입 메서드를 호출합니다.
                        viewModel.signup(member)
                        Log.d(TAG, "setupViews: 회원가입 되었습니다.")
                        Toast.makeText(requireContext(), "회원가입 되었습니다.", Toast.LENGTH_SHORT).show()

                        // 반 배정
                        val tempId = viewModel.signedMem(email,generationCode, regionCode, classCode)
                        Log.d(TAG, "setupViews: $tempId, $generationCode, $regionCode, $classCode")

                        uploadProfileImage(email, tempUri!!)

                        findNavController().navigateUp()

                    }
                }
            } else {
                // 입력 값이 유효하지 않을 경우, 필요한 에러 메시지를 표시하거나 처리해줍니다.
                Log.d(TAG, "setupViews: 똑바로 다 입력하세요 ~~")
                Toast.makeText(requireContext(), "모든 항목을 정확히 입력하세요. \n이메일, 전화번호(11자리) 등", Toast.LENGTH_SHORT).show()

            }
        }

        // 프로필 이미지 변경.
        binding.fragmentJoinImageviewProfile.setOnClickListener {
//            openGalleryForImage()
            checkAndRequestStoragePermission()
        }
    }

    // information spinner adapter
    private fun setSpinnerDefaultValues() {
        val defaultText = "-" // '-'로 설정하고 싶은 default 텍스트

        // '기수' Spinner의 default 값 설정
        val nthAdapter = binding.spinnerNth.adapter as? ArrayAdapter<String>
        nthAdapter?.let {
            val defaultPosition = it.getPosition(defaultText)
            binding.spinnerNth.setSelection(defaultPosition)
        }

        // '지역' Spinner의 default 값 설정
        val regionAdapter = binding.spinnerRegion.adapter as? ArrayAdapter<String>
        regionAdapter?.let {
            val defaultPosition = it.getPosition(defaultText)
            binding.spinnerRegion.setSelection(defaultPosition)
        }

        // '반' Spinner의 default 값 설정
        val classNumAdapter = binding.spinnerClassNum.adapter as? ArrayAdapter<String>
        classNumAdapter?.let {
            val defaultPosition = it.getPosition(defaultText)
            binding.spinnerClassNum.setSelection(defaultPosition)
        }
    }
    private fun setSpinnerAdapters() {
        val nthOptions = listOf("-", "9", "10") // 기수 옵션들을 리스트로 설정해주세요
        val regionOptions = listOf("-", "서울", "구미", "대전", "부울경", "광주") // 지역 옵션들을 리스트로 설정해주세요
        val classNumOptions = listOf("-", "1", "2", "3", "4", "5", "6", "7", "8","9","10") // 반 옵션들을 리스트로 설정해주세요

        val nthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nthOptions)
        val regionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, regionOptions)
        val classNumAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classNumOptions)

        binding.spinnerNth.adapter = nthAdapter
        binding.spinnerRegion.adapter = regionAdapter
        binding.spinnerClassNum.adapter = classNumAdapter

        binding.spinnerNth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Map selected option to corresponding value
                generationCode = if (position == 0) 0 else position
//                viewModel.updateGenerationCode(nthValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Map selected option to corresponding value
                regionCode = if (position == 0) 0 else position
//                viewModel.updateRegionCode(regionValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerClassNum.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Map selected option to corresponding value
                classCode = if (position == 0) 0 else position
//                viewModel.updateClassCode(classNumValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // 생년월일을 선택하는 달력 다이얼로그를 보여주는 메서드입니다.
    private fun showDatePickerDialog(){
        // 현재 날짜를 기본으로 설정합니다.
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        // DatePickerDialog를 생성하고 보여줍니다.
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // 날짜가 선택되었을 때 처리할 로직을 여기에 작성합니다.
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.fragmentJoinEditTVBirth.text = Editable.Factory.getInstance().newEditable(selectedDate)
                tempDate = selectedDate
                Log.d(TAG, "showDatePickerDialog: $selectedDate")
                Log.d(TAG, "showDatePickerDialog: ${binding.fragmentJoinEditTVBirth.text.toString()}")
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        // 이미지를 선택하는 요청을 `ActivityResultLauncher`로 보냅니다.
        imagePickerLauncher.launch(intent)
    }

    // Modify checkDuplicateNickname to checkDuplicateEmail
    private suspend fun checkDuplicateEmail(email: String): Boolean {
        return try {
            // RetrofitUtil을 사용하여 서버에 이메일 중복 확인 요청
            RetrofitUtil.memberService.emailCheck(email)
        } catch (e: Exception) {
            // 예외 처리 로직
            Log.d(TAG, "checkDuplicateEmail: 오류 발생")
            Log.d(TAG, "checkDuplicateEmail: $e")
            // 예외 발생 시에도 false 값을 반환하여 이메일 중복으로 처리
            false
        }
    }

    private fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
        val file: File = getFileFromUri(context, uri) ?: return null
        // 파일을 가져오지 못한 경우 처리할 로직
        val requestFile: RequestBody = createRequestBodyFromFile(file)
        Log.d(TAG, "createMultipartFromUri: ${file.name}")
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

    private suspend fun uploadProfileImage(email: String, imageUri: Uri) {
        val profileImagePart = createMultipartFromUri(requireContext(), imageUri)
        val emailRequestBody = RequestBody.create(okhttp3.MultipartBody.FORM, email)

        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            Log.d(TAG, "uploadProfileImage: $emailRequestBody, ${profileImagePart.toString()}")
            val job = lifecycleScope.launch(Dispatchers.IO) {
                try {
                    Log.d(TAG, "uploadProfileImage: 이미지 확인--------")
                    Log.d(TAG, "uploadProfileImage: $email 로 $profileImagePart 보낸다")

                    // 서버에 프로필 이미지 업로드 요청
                    val response = memberService.uploadProfileImage(emailRequestBody, profileImagePart!!)
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

            job.join() // 코루틴이 끝까지 실행될 때까지 대기
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

    @Deprecated("Deprecated in Java")
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
}
