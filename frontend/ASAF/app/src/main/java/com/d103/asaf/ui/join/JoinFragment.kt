package com.d103.asaf.ui.join

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.databinding.FragmentJoinBinding
import com.d103.asaf.ui.login.LoginFragment
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class JoinFragment : Fragment() {

    private lateinit var binding: FragmentJoinBinding
    private val viewModel: JoinFragmentViewModel by viewModels()
//    private lateinit var loginFragment: LoginFragment

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
        return binding.root
    }

    private fun setupViews() {
        binding.fragmentJoinButtonBack.setOnClickListener {
            // 뒤로가기 버튼 클릭 시, 필요한 화면 전환 처리를 여기서 해줍니다.
            findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
        }

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
            val information = binding.fragmentJoinEditTVInformation.text.toString()

            if (validateInputs(name, email, password, confirmPassword, birth, information)) {
                // 모든 입력 값이 유효할 경우, 뷰모델의 회원가입 메서드를 호출합니다.
//                viewModel.signup(name, email, password, birth, information)
                viewModel.signup(name, email, password, "", "")
                // 서버 혹은 db에 유저 정보 저장
                
                // login fragment로 이동.
                findNavController().navigate(R.id.action_joinFragment_to_loginFragment)
            } else {
                // 입력 값이 유효하지 않을 경우, 필요한 에러 메시지를 표시하거나 처리해줍니다.
            }
        }
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
    private fun showDatePickerDialog() {
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
                binding.fragmentJoinEditTVBirth.setText(selectedDate)
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
}
