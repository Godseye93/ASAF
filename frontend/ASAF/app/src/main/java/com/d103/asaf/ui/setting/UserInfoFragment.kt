package com.d103.asaf.ui.setting

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.d103.asaf.common.util.RetrofitUtil.Companion.memberService
import com.d103.asaf.databinding.FragmentSettingUserinfoBinding
import com.d103.asaf.ui.join.JoinFragmentViewModel
import com.d103.asaf.ui.sign.SignDrawFragment.Companion.regionCode
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

private const val TAG = "UserInfoFragment_cjw_asaf"
class UserInfoFragment: Fragment() {
    private var _binding: FragmentSettingUserinfoBinding? = null
    private val binding get() = _binding!!
    private val userInfoViewModel: UserInfoFragmentViewModel by viewModels()
    private var tempMember : Member = Member()

    var generationCode : Int = 0
    var regionCode : Int = 0
    var classCode : Int = 0

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

        val tempEmail = ApplicationClass.sharedPreferences.getString("memberEmail")
        lifecycleScope.launch{
            tempMember = userInfoViewModel.getUserInfo(tempEmail!!,)
            Log.d(TAG, "onViewCreated____: $tempMember")
        }

        if(ApplicationClass.sharedPreferences.getString("authority") != "교육생"){
            binding.fragmentSettingUserinfoLayoutInformation.visibility = View.GONE
        }else{
            binding.fragmentSettingUserinfoLayoutInformation.visibility = View.VISIBLE
        }

        initInfo()

        setSpinnerDefaultValues()

        //
        binding.fragmentSettingUserinfoButtonUpdate.setOnClickListener {

            val newName = binding.fragmentSettingUserinfoEditTVName.text.toString()
            val newEmail = binding.fragmentSettingUserinfoEditTVEmail.text.toString()
            val originpass = binding.fragmentSettingUserinfoEditTVPassBefore.text.toString()
            val newpass = binding.fragmentSettingUserinfoEditTVPass.text.toString()
            val newpassconfirm = binding.fragmentSettingUserinfoEditTVPassConfirm.text.toString()
            val newBirth = binding.fragmentSettingUserinfoEditTVBirth.text.toString()
            val newPhoneNumber = binding.fragmentSettingUserinfoEditTVPhoneNumber.text.toString()
            val newStudentNumber = binding.fragmentSettingUserinfoEditTVStudentNumber.text.toString()
            val newInformation = "${binding.fragmentSettingUserinfoSpinnerNth.selectedItem}" +
                    "${binding.fragmentSettingUserinfoSpinnerRegion.selectedItem}" +
                    "${binding.fragmentSettingUserinfoSpinnerClassNum.selectedItem}"

            val regionName = when(regionCode) {
                1 -> "서울"
                2 -> "구미"
                3 -> "대전"
                4 -> "부울경"
                5 -> "광주"
                else -> " - "
            }


            if(newName.isBlank() || newEmail.isBlank() || newBirth.isBlank()
                || newPhoneNumber.isBlank() || newStudentNumber.isBlank() || newInformation.isBlank()){
                Toast.makeText(requireContext(), "모든 값을 입력하세요.", Toast.LENGTH_SHORT).show()
            }else{
                if(originpass != tempMember.memberPassword){
                    Toast.makeText(requireContext(), "기존 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
                else{

                    if(newpass != newpassconfirm){
                        Toast.makeText(requireContext(), "새로운 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        if (newName != tempMember.memberName || newEmail != tempMember.memberEmail || newBirth != tempMember.birthDate
                            || newPhoneNumber != tempMember.phoneNumber || newStudentNumber != tempMember.studentNumber.toString()
                            || newInformation != tempMember.memberInfo || generationCode != ApplicationClass.sharedPreferences.getInt("Nth")
                            || regionName != ApplicationClass.sharedPreferences.getString("region")
                            || classCode != ApplicationClass.sharedPreferences.getInt("classCode")) {

                            tempMember.apply {
                                memberName = newName
                                memberPassword = newpass
                                birthDate = newBirth
                                phoneNumber = newPhoneNumber
                                studentNumber = newStudentNumber.toInt()
                                memberInfo = newInformation
                            }

                            // update 실행
                            lifecycleScope.launch{
                                userInfoViewModel.updateUser(tempMember, classCode, regionCode, generationCode)
                                Log.d(TAG, "onViewCreated: update 실행 !!!!! ")
                                Log.d(TAG, "onViewCreated: $tempMember")

                                val tempId = userInfoViewModel.updateInfo(tempMember.id, generationCode, regionCode, classCode)
                                Log.d(TAG, "onViewCreated: $tempId, $generationCode, $regionCode, $classCode ")

                                ApplicationClass.sharedPreferences.deleteUser()
                                ApplicationClass.sharedPreferences.addUserByEmailAndPwd(tempMember)
                                ApplicationClass.sharedPreferences.addUserInfo(generationCode, regionCode.toString(), classCode)
                            }
                            Toast.makeText(requireContext(), "회원 정보가 수정 되었습니다.", Toast.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        } else {
                            // 정보가 수정되지 않았을 경우
                            Toast.makeText(requireContext(), "변경된 값이 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.fragmentSettingUserinfoButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰 바인딩 해제
        _binding = null
    }

    private fun initInfo(){
        binding.fragmentSettingUserinfoEditTVName.setText("${ApplicationClass.sharedPreferences.getString("memberName")}")
        binding.fragmentSettingUserinfoEditTVEmail.setText("${ApplicationClass.sharedPreferences.getString("memberEmail")}")
//        binding.fragmentSettingUserinfoEditTVPass.setText("${ApplicationClass.sharedPreferences.getString("memberPassword")}")
        binding.fragmentSettingUserinfoEditTVBirth.setText("${ApplicationClass.sharedPreferences.getString("birth_date")}")
        binding.fragmentSettingUserinfoEditTVPhoneNumber.setText("${ApplicationClass.sharedPreferences.getString("phone_number")}")
        binding.fragmentSettingUserinfoEditTVStudentNumber.setText("${ApplicationClass.sharedPreferences.getInt("student_number")}")

        setSpinnerAdapters()
    }

    private fun setSpinnerDefaultValues() {
        // Spinner 초기값 설정
        val selectedNth = ApplicationClass.sharedPreferences.getInt("Nth")
        val selectedRegion = ApplicationClass.sharedPreferences.getString("region")
        val selectedClassNum = ApplicationClass.sharedPreferences.getInt("classCode")

        // '기수' Spinner의 default 값 설정
        val nthAdapter = binding.fragmentSettingUserinfoSpinnerNth.adapter as? ArrayAdapter<String>
        nthAdapter?.let {
            val tempNum = selectedNth + 8
            val defaultPosition = it.getPosition(tempNum.toString())
            binding.fragmentSettingUserinfoSpinnerNth.setSelection(defaultPosition)
        }

        // '지역' Spinner의 default 값 설정
        val regionAdapter = binding.fragmentSettingUserinfoSpinnerRegion.adapter as? ArrayAdapter<String>
        regionAdapter?.let {
            val defaultPosition = it.getPosition(selectedRegion)
            binding.fragmentSettingUserinfoSpinnerRegion.setSelection(defaultPosition)
        }

        // '반' Spinner의 default 값 설정
        val classNumAdapter = binding.fragmentSettingUserinfoSpinnerClassNum.adapter as? ArrayAdapter<String>
        classNumAdapter?.let {
            val defaultPosition = it.getPosition(selectedClassNum.toString())
            binding.fragmentSettingUserinfoSpinnerClassNum.setSelection(defaultPosition)
        }
    }
    private fun setSpinnerAdapters() {
        val nthOptions = listOf("-", "9", "10") // 기수 옵션들을 리스트로 설정해주세요
        val regionOptions = listOf("-", "서울", "구미", "대전", "부울경", "광주") // 지역 옵션들을 리스트로 설정해주세요
        val classNumOptions = listOf("-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10") // 반 옵션들을 리스트로 설정해주세요

        val nthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nthOptions)
        val regionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, regionOptions)
        val classNumAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classNumOptions)

        binding.fragmentSettingUserinfoSpinnerNth.adapter = nthAdapter
        binding.fragmentSettingUserinfoSpinnerRegion.adapter = regionAdapter
        binding.fragmentSettingUserinfoSpinnerClassNum.adapter = classNumAdapter

        // Spinner 초기값 설정
//        val selectedNth = ApplicationClass.sharedPreferences.getInt("Nth")
//        val selectedRegion = ApplicationClass.sharedPreferences.getString("region")
//        val selectedClassNum = ApplicationClass.sharedPreferences.getInt("classCode")
//
//        binding.fragmentSettingUserinfoSpinnerNth.setSelection(nthAdapter.getPosition(selectedNth.toString()))
//        binding.fragmentSettingUserinfoSpinnerRegion.setSelection(regionAdapter.getPosition(selectedRegion))
//        binding.fragmentSettingUserinfoSpinnerClassNum.setSelection(classNumAdapter.getPosition(selectedClassNum.toString()))

        binding.fragmentSettingUserinfoSpinnerNth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Map selected option to corresponding value
                generationCode = if (position == 0) 0 else position
//                viewModel.updateGenerationCode(nthValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.fragmentSettingUserinfoSpinnerRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Map selected option to corresponding value
                regionCode = if (position == 0) 0 else position
//                viewModel.updateRegionCode(regionValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.fragmentSettingUserinfoSpinnerClassNum.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Map selected option to corresponding value
                classCode = if (position == 0) 0 else position
//                viewModel.updateClassCode(classNumValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}