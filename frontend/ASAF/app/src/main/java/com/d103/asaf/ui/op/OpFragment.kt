package com.d103.asaf.ui.op

import MoneyFragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentOpBinding
import java.util.Calendar


class OpFragment : BaseFragment<FragmentOpBinding>(FragmentOpBinding::bind, R.layout.fragment_op) {
    private val viewModel: OpFragmentViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSeat()
        initMonth()
        initClass()
        initClickListener()
    }

    private fun initSeat() {
        binding.fragmentOpDropdownMonth.visibility = View.GONE
        handler.postDelayed({
            binding.fragmentOpImageviewFront.visibility = View.VISIBLE
            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id,SeatFragment.instance(viewModel.position.value, viewModel.seat.value))
                .commit()
        }, 100)
    }

    private fun initClickListener() {
        binding.fragmentOpTogglebuttonToggle.setFirstButtonClickListener {
            binding.fragmentOpImageviewArcprogressbar.visibility = View.INVISIBLE
            initSeat()
        }
        binding.fragmentOpTogglebuttonToggle.setSecondButtonClickListener {
            binding.fragmentOpImageviewFront.visibility = View.INVISIBLE
            binding.fragmentOpImageviewArcprogressbar.visibility = View.INVISIBLE
            binding.fragmentOpDropdownMonth.visibility = View.GONE

            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id,LockerFragment.instance(viewModel.lockers.value))
                .commit()
        }
        binding.fragmentOpTogglebuttonToggle.setThirdButtonClickListener {
            binding.fragmentOpImageviewFront.visibility = View.INVISIBLE
            binding.fragmentOpDropdownMonth.visibility = View.VISIBLE
            binding.fragmentOpImageviewArcprogressbar.visibility = View.VISIBLE

            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id, MoneyFragment())
                .commit()
        }
    }

    // observe or collect로 변경 필요 -> 변수가 변경되면 처리 할 작업을 구현한다는 뜻
    // LiveData 는 외부에서 값을 할당받을때 MutableLiveData는 내부에서 값을 post로 할당할 때 사용
    private fun initMonth() {
        val calendar = Calendar.getInstance()
        binding.apply {
            fragmentOpDropdownMonth.dropdownText.addTextChangedListener(monthWatcher)
            fragmentOpDropdownMonth.dropdownText.text = viewModel.months.value[calendar.get(Calendar.MONTH)].toString()
            fragmentOpDropdownMonth.dropdownTextPost.text = "월"

            // 객체가 바뀌면 안됨.. 요소를 변경해줘야 변화 인식됨
            fragmentOpDropdownMonth.dataList.addAll(viewModel.months.value)
            fragmentOpDropdownMonth.dataList.removeAt(calendar.get(Calendar.MONTH))
        }
    }

    private fun initClass() {
        binding.apply {
            fragmentOpDropdownClass.dropdownText.addTextChangedListener(classWatcher)
            fragmentOpDropdownClass.dropdownText.text = viewModel.classes.value[0].toString()
            fragmentOpDropdownClass.dropdownTextPost.text = "반"
            // 객체가 바뀌면 안됨.. 요소를 변경해줘야 변화 인식됨
            fragmentOpDropdownClass.dataList.addAll(viewModel.classes.value)
            fragmentOpDropdownClass.dataList.removeAt(0)
            // 프로그레스바 텍스트 크기 변경
            fragmentOpImageviewArcprogressbar.progressRate.textSize = 30f
            fragmentOpImageviewArcprogressbar.progressText.textSize = 15f
        }
    }

    private val monthWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // 텍스트가 변경되기 전에 호출됩니다.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // 텍스트가 변경될 때 호출됩니다.
        }

        override fun afterTextChanged(s: Editable?) {
            // 텍스트가 변경된 후에 호출됩니다.
            viewModel.curMonth.value = s.toString().toInt()
        }
    }

    private val classWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // 텍스트가 변경되기 전에 호출됩니다.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // 텍스트가 변경될 때 호출됩니다.
        }

        override fun afterTextChanged(s: Editable?) {
            // 텍스트가 변경된 후에 호출됩니다.
            viewModel.curClass.value = s.toString().toInt()
        }
    }
}