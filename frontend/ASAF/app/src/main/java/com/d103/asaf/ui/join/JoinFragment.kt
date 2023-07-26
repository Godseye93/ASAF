package com.d103.asaf.ui.join

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.databinding.FragmentJoinBinding
import com.d103.asaf.ui.login.LoginFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.selects.select
import java.util.Calendar
import java.util.Date

private const val TAG = "JoinFragment_cjw"
class JoinFragment : Fragment() {

    private lateinit var binding: FragmentJoinBinding
    private val viewModel: JoinFragmentViewModel by viewModels()
//    private lateinit var loginFragment: LoginFragment
    private lateinit var tempDate: String

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

        setupViews()

        // Spinner default 값을 설정하는 메서드를 호출
        setSpinnerDefaultValues()

        // MainActivity의 hideBottomNavigationBarFromFragment() 메서드를 호출하여 바텀 네비게이션 바를 숨김
//        (activity as? MainActivity)?.hideBottomNavigationBarFromFragment()

        return binding.root
    }

    private fun setupViews() {

        // information spinner adapter
        setSpinnerAdapters()

        // 뒤로가기 버튼 클릭 시,
        binding.fragmentJoinButtonBack.setOnClickListener {
            findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
        }
        // 생년월일 클릭 시 달력 표시
        binding.fragmentJoinEditTVBirth.setOnClickListener {
            showDatePickerDialog()
//            val tempDate = showDatePickerDialog()
//            Log.d(TAG, "setupViews: $tempDate")
        }

        binding.fragmentJoinLayoutBirth.setOnClickListener{
            showDatePickerDialog()
//            val tempDate = showDatePickerDialog()
//            Log.d(TAG, "setupViews: $tempDate")
        }

        binding.fragmentJoinButtonSignup.setOnClickListener {
            val name = binding.fragmentJoinEditTVName.text.toString()
            val email = binding.fragmentJoinEditTVEmail.text.toString()
            val password = binding.fragmentJoinEditTVPass.text.toString()
            val confirmPassword = binding.fragmentJoinEditTVPassConfirm.text.toString()
            val birth = binding.fragmentJoinEditTVBirth.text.toString()
            val information = "${binding.spinnerNth.selectedItem}${binding.spinnerRegion.selectedItem}${binding.spinnerClassNum.selectedItem}"

            // information 문자열 split.
            var Nth: Int? = null
            var region: String? = null
            var classNum: Int? = null

            val info = parseInput(information)
            if(info != null){
                // 기수, 지역, 반
                Nth = info.first
                region = info.second
                classNum = info.third
            }

            val member = Member(name, email, password, birth, information)
            if (validateInputs(name, email, password, confirmPassword, birth, information)) {
                // 모든 입력 값이 유효할 경우, 뷰모델의 회원가입 메서드를 호출합니다.
//                viewModel.signup(name, email, password, birth, information)
//                Log.d(TAG, "setupViews: ${tempDate}")
                viewModel.signup( member )
                
                // login fragment로 이동.
                findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
            } else {
                // 입력 값이 유효하지 않을 경우, 필요한 에러 메시지를 표시하거나 처리해줍니다.
            }
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
        val nthOptions = listOf("8", "9", "10") // 기수 옵션들을 리스트로 설정해주세요
        val regionOptions = listOf("광주", "구미", "대전", "부울경", "서울") // 지역 옵션들을 리스트로 설정해주세요
        val classNumOptions = listOf("1", "2", "3", "4", "5", "6", "7", "8","9","10") // 반 옵션들을 리스트로 설정해주세요

        val nthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nthOptions)
        val regionAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, regionOptions)
        val classNumAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classNumOptions)

        binding.spinnerNth.adapter = nthAdapter
        binding.spinnerRegion.adapter = regionAdapter
        binding.spinnerClassNum.adapter = classNumAdapter
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

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        birth: String,
        information: String
    ): Boolean {
        // 입력 값의 유효성을 검사하는 로직을 구현합니다.
        // 여기서는 예시로 항상 true를 반환하도록 처리했습니다.
        // 실제 앱에서는 입력 값의 유효성을 적절히 확인하여 true 또는 false를 반환해야 합니다.
        return true
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

        // fragment_join_editTV_birth를 비활성화합니다.
//        binding.fragmentJoinEditTVBirth.isEnabled = false

//        datePickerDialog.setOnDismissListener {
//            // 달력이 닫힐 때 다시 EditText를 활성화합니다.
//            binding.fragmentJoinEditTVBirth.isEnabled = true
//        }

        datePickerDialog.show()
    }

    private fun hideBottomBar() {
        // MainActivity의 hideBottomNavigationBar() 메서드를 호출하여 사용합니다.
        if (activity is MainActivity) {
            (activity as MainActivity).hideBottomNavigationBarFromFragment()
        }
    }
}
