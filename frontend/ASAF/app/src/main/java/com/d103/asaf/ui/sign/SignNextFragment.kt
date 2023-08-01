package com.d103.asaf.ui.sign

import android.os.Bundle
import android.view.View
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentSignNextBinding
import com.d103.asaf.ui.library.student.LibraryUseDrawFragment
import java.util.Calendar

class SignNextFragment : BaseFragment<FragmentSignNextBinding>(FragmentSignNextBinding::bind, R.layout.fragment_sign_next) {
    private val dataMonth = listOf(1,2,3,4,5,6,7,8,9,10,11,12)
    private val dataDay = listOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31)
    private val calendar = Calendar.getInstance()
    private val currentMonth = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 1을 더해줍니다.
    private val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            fragmentSignNextDropdownFirst.apply {
                dropdownText.text = currentMonth.toString()
                dropdownTextPost.text = "월"
                dataList.addAll(dataMonth)
                dataList.removeAt(currentMonth)
            }
            fragmentSignNextDropdownSecond.apply {
                dropdownText.text = currentDay.toString()
                dropdownTextPost.text = "일"
                dataList.addAll(dataDay)
                dataList.removeAt(currentDay)
            }
            fragmentSignNextDropdownThird.apply {
                dropdownText.text = currentDay.toString()
                dropdownTextPost.text = "일"
                dataList.addAll(dataDay)
                dataList.removeAt(currentDay)
            }
            fragmentSignNextDropdownFourthMonth.apply {
                dropdownText.text = currentMonth.toString()
                dropdownTextPost.text = "월"
                dataList.addAll(dataMonth)
                dataList.removeAt(currentMonth)
            }
            fragmentSignNextDropdownFourthDay.apply {
                dropdownText.text = currentDay.toString()
                dropdownTextPost.text = "일"
                dataList.addAll(dataDay)
                dataList.removeAt(currentDay)
            }
        }
    }
}