package com.d103.asaf.ui.schedule


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.databinding.FragmentNotiRegisterBinding
import java.util.Calendar
import java.util.Date


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotiRegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotiRegisterFragment : BaseFragment<FragmentNotiRegisterBinding>(FragmentNotiRegisterBinding::bind, R.layout.fragment_noti_register) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val viewModel : ScheduleRegisterFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.dateTextView.text = sharedViewModel.selectedDate

        binding.fragmentNotiRegisterBackButton.setOnClickListener {
            (requireActivity() as MainActivity).showBottomNavigationBarFromFragment()
            (requireActivity() as MainActivity).onBackPressed()

        }

        binding.attendanceChip.setOnClickListener {
            binding.notiCheckBox.isChecked = true
            binding.notiDetailEdittext.setText("9시가 되면 입실 버튼이 비활성화 됩니다. " +
                    " 8:59 까지 입실 완료해주세요")
            binding.notiTitleEdittext.setText("입실 체크 공지")
            binding.notiTime.hour = 8
            binding.notiTime.minute = 50
        }
        binding.checkoutChip.setOnClickListener {
            binding.notiCheckBox.isChecked = true
            binding.notiDetailEdittext.setText("퇴실 버튼 안 누른 사람들 퇴실 버튼 누르러 뭅뭅")
            binding.notiTitleEdittext.setText("퇴실 체크 공지")
            binding.notiTime.hour = 18
            binding.notiTime.minute = 5
        }
        binding.liveChip.setOnClickListener {
            binding.notiCheckBox.isChecked = true
            binding.notiDetailEdittext.setText("[9시 라이브 방송 안내] 9시부터 라이브 방송이 진행될 예정입니다.")
            binding.notiTitleEdittext.setText("퇴실 체크 공지")
            binding.notiTime.hour = 8
            binding.notiTime.minute = 55
        }
        binding.fragmentNotiRegisterButton.setOnClickListener {
            if(binding.notiDetailEdittext.text.toString().isNullOrEmpty()){
                Toast.makeText(requireContext(), "공지 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else if(binding.notiTitleEdittext.text.toString().isNullOrEmpty()){
                Toast.makeText(requireContext(), "공지 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                val noti = createNoti()

                Log.d("년", "년: ${sharedViewModel.year}")
                Log.d("공지시간", "공지 시간: ${noti.sendTime}")
                Log.d("등록시간", "공지 시간: ${noti.registerTime}")




                // 공지 발송 + 저장
                if(binding.notiCheckBox.isChecked){

                }
                // 저장만
                else{


                }

            }
        }

    }

    fun createNoti() : Noti{
        val noti = Noti()

        //시간 설정
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val hour = binding.notiTime.hour
        val minute = binding.notiTime.minute

        calendar.set(year, month, dayOfMonth, hour, minute)

        val selectedTime: Date = calendar.time
        noti.sendTime = selectedTime
        calendar.set(sharedViewModel.year, sharedViewModel.month, sharedViewModel.day)
        noti.registerTime = calendar.time

        // 제목 타이틀 설정
        noti.title = binding.notiTitleEdittext.text.toString()
        noti.content = binding.notiDetailEdittext.text.toString()


        // 작성자
        noti.sender = ApplicationClass.sharedPreferences.getInt("id")!!

        // 공지 설정
        noti.notification = binding.notiCheckBox.isChecked





        return noti
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotiRegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotiRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}