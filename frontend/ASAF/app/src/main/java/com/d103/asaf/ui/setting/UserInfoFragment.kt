package com.d103.asaf.ui.setting

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.api.MemberService
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentSettingUserinfoBinding
import com.ssafy.template.util.SharedPreferencesUtil

private const val TAG = "UserInfoFragment_cjw"
class UserInfoFragment: Fragment() {
    private var _binding: FragmentSettingUserinfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserInfoFragmentViewModel
    private val sharedViewModel: SharedViewModel by viewModels()

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

        // Initialize ViewModel
//        viewModel = ViewModelProvider(this).get(UserInfoFragmentViewModel::class.java)

        // Replace "user@example.com" with the actual user's email
        /////////////////////////////////////////////////////////// 저장된 사용자 이메일 가져와서 넣어줌.
//        viewModel.getUserInfo(ApplicationClass.sharedPreferences.getString("memberEmail")!!)
        Log.d(TAG, "onViewCreated: ${sharedViewModel.logInUser.memberEmail}")
        Log.d(TAG, "onViewCreated: ${ApplicationClass.sharedPreferences.getString("memberEmail")}")
        initInfo()

        //
        binding.fragmentSettingUserinfoButtonUpdate.setOnClickListener {

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

    fun initInfo(){
        binding.fragmentSettingUserinfoEditTVName.setText("${ApplicationClass.sharedPreferences.getString("memberName")}")
        binding.fragmentSettingUserinfoEditTVEmail.setText("${ApplicationClass.sharedPreferences.getString("memberEmail")}")
        binding.fragmentSettingUserinfoEditTVPass.setText("${ApplicationClass.sharedPreferences.getString("memberPassword")}")
        binding.fragmentSettingUserinfoEditTVBirth.setText("${ApplicationClass.sharedPreferences.getString("birth_date")}")
        val imageUrl = "${ApplicationClass.API_URL}member/${ApplicationClass.sharedPreferences.getString("memberEmail")}/profile-image"
        val requestOptions = RequestOptions().transform(CircleCrop())
        Glide.with(this)
            .load(imageUrl)
            .apply(requestOptions)
            .into(binding.fragmentSettingUserinfoImageviewProfile)

//        // information 문자열 split.
//        var Nth: Int? = null
//        var region: String? = null
//        var classNum: Int? = null
//        val info = parseInput(member.memberInfo)
//        if(info != null){
//            // 기수, 지역, 반
//            Nth = info.first
//            region = info.second
//            classNum = info.third
//        }

//        spinnerAdapterNth(Nth.toString())
//        spinnerAdapterRegion(region.toString())
//        spinnerAdapterClassNum(classNum.toString())
    }

    private fun spinnerAdapterNth(nth: String) {
        val nthOptions = listOf("-", "8", "9", "10") // 기수 옵션들을 리스트로 설정해주세요
        val nthAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, nthOptions)
        binding.fragmentSettingUserinfoSpinnerNth.adapter = nthAdapter
        // Set the selection for each spinner based on the provided nth value
        val nthPosition = nthAdapter.getPosition(nth)
        binding.fragmentSettingUserinfoSpinnerNth.setSelection(nthPosition)
    }
    private fun spinnerAdapterRegion(region: String) {
        val regionOptions = listOf("-", "광주", "구미", "대전", "부울경", "서울") // 지역 옵션들을 리스트로 설정해주세요
        val regionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, regionOptions)
        binding.fragmentSettingUserinfoSpinnerRegion.adapter = regionAdapter
        // Set the selection for each spinner based on the provided nth value
        val regionPosition = regionAdapter.getPosition(region)
        binding.fragmentSettingUserinfoSpinnerNth.setSelection(regionPosition)
    }
    private fun spinnerAdapterClassNum(classNum: String) {
        val classNumOptions = listOf("-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10") // 반 옵션들을 리스트로 설정해주세요
        val classNumAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classNumOptions)
        binding.fragmentSettingUserinfoSpinnerClassNum.adapter = classNumAdapter
        // Set the selection for each spinner based on the provided nth value
        val classNumPosition = classNumAdapter.getPosition(classNum)
        binding.fragmentSettingUserinfoSpinnerNth.setSelection(classNumPosition)
    }

    // information 정보 문자열 split.
    fun parseInput(input: String): Triple<Int, String, Int>? {
        // 정규식을 사용하여 입력 문자열을 구분합니다.
        val regex = """(\d+)(\D+)(\d+)""".toRegex()
        val matchResult = regex.find(input)

        // 정규식이 매칭되지 않으면 null을 반환합니다.
        if (matchResult == null || matchResult.groupValues.size != 4) {
            return null
        }

        // 매칭된 그룹에서 각각의 값을 추출합니다.
        val Nth = matchResult.groupValues[1].toInt()
        val region = matchResult.groupValues[2]
        val classNum = matchResult.groupValues[3].toInt()

        return Triple(Nth, region, classNum)
    }
}