package com.d103.asaf.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentScheduleBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to

 * create an instance of this fragment.
 */


private const val TAG = "ScheduleFragment"


class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::bind, R.layout.fragment_schedule), NotiUpdateListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val sharedViewModel : SharedViewModel  by activityViewModels()
     val viewModel : ScheduleFragmentViewModel by viewModels()
    private var selectedDate: CalendarDay = CalendarDay.today()
    private var notiList  =  mutableListOf<Noti>()
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var adapter : NotiInfoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notiList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        binding.notiRegisterButton.setOnClickListener {


//            val action = ScheduleFragmentDirections.actionScheduleFragmentToNotiRegisterFragment(selectedDate)
            if(selectedDate != null){
                Log.d(TAG, "onViewCreated selectedDate: $selectedDate ")
                findNavController().navigate(R.id.action_scheduleFragment_to_notiRegisterFragment)
                (requireActivity() as MainActivity).hideBottomNavigationBarFromFragment()
            }
            else{
                Log.d(TAG, "onViewCreated: ????")
            }

        }

        // 캘린더 초기화
        initCalendar()

        // Override the default back button behavior
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate to ProHomeFragment
                findNavController().navigate(R.id.action_scheduleFragment_to_ProHomeFragment)
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScheduleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initCalendar() {


        // 오늘 날짜로 초기 선택
        binding.calendarView.setSelectedDate(CalendarDay.today())
        binding.fragmentScheduleDateView.text = "${selectedDate.year}년 ${selectedDate.month + 1} 월 ${selectedDate.day} 일"
        sharedViewModel.selectedDate = "${selectedDate.year}년 ${selectedDate.month + 1} 월 ${selectedDate.day} 일"
        sharedViewModel.year = selectedDate.year
        sharedViewModel.month = selectedDate.month
        sharedViewModel.day = selectedDate.day

        viewModel.getMeesage(ApplicationClass.sharedPreferences.getInt("id")!!, selectedDate.date.time)



        // 달력에 주간 요일 및 월간 형식 설정 (월, 화, 수, 목... / 1월,2월, 3월 ...)
        binding.calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        binding.calendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

        //글짜 스타일 정의
        binding.calendarView.setDateTextAppearance(R.style.AppTheme)
        binding.calendarView.setWeekDayTextAppearance(R.style.AppTheme)
        binding.calendarView.setHeaderTextAppearance(R.style.AppTheme)


        adapter = NotiInfoAdapter(requireContext(), this)
        binding.fragmentScheduleRecyclerview.adapter = adapter
        binding.fragmentScheduleRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        // 스와이프
        val itemTouchHelperCallback = ItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.fragmentScheduleRecyclerview)

//        adapter.submitList(notiList)


        binding.calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {

                selectedDate = binding.calendarView.selectedDate
                Log.d("selectedDate", selectedDate.year.toString())

                Log.d(TAG, "캘린더 선택 시간 : ${selectedDate.date.time}")
                
                sharedViewModel.year = selectedDate.year
                sharedViewModel.month = selectedDate.month
                sharedViewModel.day = selectedDate.day
                //이거 바꿔야함
                viewModel.getMeesage(ApplicationClass.sharedPreferences.getInt("id")!!, selectedDate.date.time)

                sharedViewModel.selectedDate = "${selectedDate.year}년 ${selectedDate.month + 1} 월 ${selectedDate.day} 일"
                binding.fragmentScheduleDateView.text = "${selectedDate.year}년 ${selectedDate.month + 1} 월 ${selectedDate.day} 일"
//                adapter.submitList(viewModel.notiList.value)
            }
        })





    }
    fun deleteMessage(id  : Int){
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val response = RetrofitUtil.notiService.deleteNoti(id)
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody!!){
                        for(noti in notiList){
                            if(noti.id == id){
                                notiList.remove(noti)
                                break;
                            }
                        }
                    }
                }
            }
        }catch (e : Exception){
            Log.d("NOTI DELETE ERROR", "deleteMessage: ${e.message}")
        }
    }

    override fun onNotiUpdate(noti: Noti) {
        Log.d(TAG, "dd: $noti")

        viewModel.updateNoti(noti)
        viewModel.getMeesage(ApplicationClass.sharedPreferences.getInt("id")!!, selectedDate.date.time)
    }


}