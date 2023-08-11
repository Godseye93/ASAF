package com.d103.asaf.ui.login

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.databinding.FragmentFindpwdBinding
import java.util.Calendar

class FindpwdFragment : Fragment() {
    private lateinit var binding: FragmentFindpwdBinding
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindpwdBinding.inflate(inflater, container, false)

        setupViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViews() {

        setSpinnerAdapters()

        binding.fragmentFindpwdEditTVBirth.setOnClickListener {
            showDatePickerDialog()
        }
        binding.fragmentFindpwdLayoutBirth.setOnClickListener{
            showDatePickerDialog()
        }

        binding.fragmentFindpwdButtonFindpwd.setOnClickListener {
            val name = binding.fragmentFindpwdEditTVName.text.toString()
            val email = binding.fragmentFindpwdEditTVEmail.text.toString()
            val birth = binding.fragmentFindpwdEditTVBirth.text.toString()
            val information = "${binding.spinnerNth.selectedItem}${binding.spinnerRegion.selectedItem}${binding.spinnerClassNum.selectedItem}"


            // ViewModel에 비밀번호 찾기 요청 보내기
            viewModel.findPassword(name, email, birth, information)
            observeViewModel()
        }

        binding.fragmentFindpwdButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        viewModel.passwordFindResult.observe(viewLifecycleOwner, Observer { passwordFindResult ->
            if (passwordFindResult) {
                Toast.makeText(context, "등록된 이메일로 비밀번호가 전송되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "일치하는 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })
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
                binding.fragmentFindpwdEditTVBirth.text = Editable.Factory.getInstance().newEditable(selectedDate)
//                Log.d(TAG, "showDatePickerDialog: $selectedDate")
//                Log.d(TAG, "showDatePickerDialog: ${binding.fragmentJoinEditTVBirth.text.toString()}")
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

}