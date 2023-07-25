package com.d103.asaf.ui.op

import MoneyFragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentOpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        handler.postDelayed({
            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id,SeatFragment.instance(viewModel.position))
                .commit()
        }, 100)
    }

    private fun initClickListener() {
        binding.fragmentOpTogglebuttonToggle.setFirstButtonClickListener {
            initSeat()
        }
        binding.fragmentOpTogglebuttonToggle.setSecondButtonClickListener {
            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id,LockerFragment.instance(viewModel.locker))
                .commit()
        }
        binding.fragmentOpTogglebuttonToggle.setThirdButtonClickListener {
            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id, MoneyFragment())
                .commit()
        }
    }

    // observe or collect로 변경 필요 -> 변수가 변경되면 처리 할 작업을 구현한다는 뜻
    // LiveData 는 외부에서 값을 할당받을때 MutableLiveData는 내부에서 값을 post로 할당할 때 사용
    private fun initMonth() {
        val calendar = Calendar.getInstance()
        binding.fragmentOpDropdownMonth.dropdownText.text = viewModel.months.value[calendar.get(Calendar.MONTH)].toString()
        binding.fragmentOpDropdownMonth.dropdownText.text = "월"

        // 객체가 바뀌면 안됨.. 요소를 변경해줘야 변화 인식됨
        binding.fragmentOpDropdownClass.dataList.addAll(viewModel.months.value)
        binding.fragmentOpDropdownClass.dataList.removeAt(calendar.get(Calendar.MONTH))

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.months.collect {
                binding.fragmentOpDropdownMonth.dropdownText.text = it[0].toString()
            }
        }
    }

    private fun initClass() {
        binding.fragmentOpDropdownClass.dropdownText.text = viewModel.classes.value[0].toString()
        binding.fragmentOpDropdownClass.dropdownText.text = "반"
        // 객체가 바뀌면 안됨.. 요소를 변경해줘야 변화 인식됨
        binding.fragmentOpDropdownClass.dataList.addAll(viewModel.classes.value)
        binding.fragmentOpDropdownClass.dataList.removeAt(0)

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.classes.collect {
                binding.fragmentOpDropdownClass.dropdownText.text = it[0].toString()
            }
        }
    }
}