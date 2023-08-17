package com.d103.asaf.ui.op

import MoneyFragment
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import app.futured.donut.DonutSection
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.databinding.FragmentOpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar


class OpFragment : BaseFragment<FragmentOpBinding>(FragmentOpBinding::bind, R.layout.fragment_op) {
    companion object {
        var parentViewModel : OpFragmentViewModel? = null
    }

    private val viewModel: OpFragmentViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel = viewModel

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.signProgress.collect {
                Log.d("서명진행률", "onViewCreated: ${viewModel.signs.value.size} ${viewModel.students.value.size}")
                if(viewModel.students.value.size != 0) {
                    viewModel.attendedPercent.value =  (viewModel.signs.value.size.toFloat()/viewModel.students.value.size) * 100f
                } else {
                    viewModel.attendedPercent.value = 0f
                }
            }
        }

        initSeat()
        initMonth()
        initClass()
        initClickListener()
        lifecycleScope.launch {
            viewModel.attendedPercent.collect {
                Log.d("서명진행률", "onViewCreated: 불림 $it")
                progressBarUpdate()
            }
        }

        // Override the default back button behavior
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate to StudentHomeFragment
                findNavController().navigate(R.id.action_opFragment_to_ProHomeFragment)
            }
        })
    }

    private fun initSeat() {
        binding.fragmentOpDropdownMonth.visibility = View.GONE
        handler.post{
            binding.fragmentOpImageviewFront.visibility = View.VISIBLE
            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id,SeatFragment.instance(viewModel.position.value, viewModel.seat.value, viewModel))
                .commit()
        }
    }

    private fun initClickListener() {
        binding.fragmentOpTogglebuttonToggle.setFirstButtonClickListener {
            binding.fragmentOpImageviewArcprogressbar.visibility = View.INVISIBLE
            binding.fragmentOpImageviewLogo.visibility = View.VISIBLE
            initSeat()
        }
        binding.fragmentOpTogglebuttonToggle.setSecondButtonClickListener {
            binding.fragmentOpImageviewFront.visibility = View.INVISIBLE
            binding.fragmentOpImageviewLogo.visibility = View.VISIBLE
            binding.fragmentOpImageviewArcprogressbar.visibility = View.INVISIBLE
            binding.fragmentOpDropdownMonth.visibility = View.GONE

            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id,LockerFragment.instance(viewModel.lockers.value,viewModel))
                .commit()
        }
        binding.fragmentOpTogglebuttonToggle.setThirdButtonClickListener {
            binding.fragmentOpImageviewFront.visibility = View.INVISIBLE
            binding.fragmentOpImageviewLogo.visibility = View.INVISIBLE
            binding.fragmentOpDropdownMonth.visibility = View.VISIBLE
            binding.fragmentOpImageviewArcprogressbar.visibility = View.VISIBLE
            // 서명 진행률 주입
            viewModel.attendedPercent.value = if(viewModel.students.value.size != 0) {
                (viewModel.signs.value.size.toFloat()/viewModel.students.value.size) * 100f
            } else 0f

            childFragmentManager.beginTransaction()
                .replace(binding.fragmentOpFramelayoutSeat.id, MoneyFragment.instance(viewModel.signs.value, viewModel))
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
            if(viewModel.classSurfaces.value.size > 0 )fragmentOpDropdownClass.dropdownText.text = viewModel.classSurfaces.value[0].toString()
            fragmentOpDropdownClass.dropdownTextPost.text = "반"
            // 객체가 바뀌면 안됨.. 요소를 변경해줘야 변화 인식됨
            fragmentOpDropdownClass.dataList.addAll(viewModel.classSurfaces.value)
            if(fragmentOpDropdownClass.dataList.size > 0)fragmentOpDropdownClass.dataList.removeAt(0)
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
            viewModel.curClass.value =
                Classinfo(viewModel.curClass.value.classNum,
                s.toString().toInt(), viewModel.curClass.value.regionCode,
                viewModel.curClass.value.generationCode,viewModel.curClass.value.userId)
        }
    }

    fun progressBarUpdate() {
        binding.fragmentOpImageviewArcprogressbar.progressText.text = "서명 현황"
        val section = DonutSection(
            name = "signPercent",
            color = Color.BLUE,
            amount = viewModel.attendedPercent.value
        )
        binding.fragmentOpImageviewArcprogressbar.arcProgressBar.submitData(listOf(section))
        binding.fragmentOpImageviewArcprogressbar.progressRate.text = "${viewModel.attendedPercent.value}%"
    }


}