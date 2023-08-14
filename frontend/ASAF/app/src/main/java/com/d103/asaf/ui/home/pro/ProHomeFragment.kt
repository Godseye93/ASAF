package com.d103.asaf.ui.home.pro

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.futured.donut.DonutSection
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.databinding.FragmentProHomeBinding
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

private const val TAG = "ProHomeFragment ASAF"

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProHomeFragment : BaseFragment<FragmentProHomeBinding>(FragmentProHomeBinding::bind, R.layout.fragment_pro_home) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val proHomeFragmentViewModel: ProHomeFragmentViewModel by viewModels()
    private var selectedStudentList : MutableList<Member> = mutableListOf()
    private var attendedPercent = 0f
    private lateinit var adapter : UserInfoAdapter
    private var currentClass  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "초기값 : ${sharedViewModel.classInfoList.value}")

        init()

        binding.fragmentProHomeNotiButton.setOnClickListener {
            if(!selectedStudentList.isEmpty()){

                //지각 알림 보내기
                var writer = ApplicationClass.sharedPreferences.getInt("id")
                val pushNotiList = mutableListOf<Noti>()

                for(student in selectedStudentList){
                    Log.d(TAG, "라이터: $writer")
                    val title = "지각 알림"
                    val content = "지각 하셨습니다. 출결관련 문의사항은 담당 프로에게 연락바람니다."
                    val noti = Noti()
                    noti.content = content
                    noti.title = title
                    if (writer != null) {
                        noti.sender = writer + 1
                    }


                    noti.receiver = student.id
                    Log.d(TAG, "학생 아이디: ${student.id}")
                    pushNotiList.add(noti)
                }
                Log.d(TAG, "몇 명?: ${pushNotiList.size} ")
                proHomeFragmentViewModel.pushMessage(pushNotiList)

                Toast.makeText(requireContext(), "${selectedStudentList[0].memberName} 포함 총 ${selectedStudentList.size }명에게 알림을 전송했습니다.", Toast.LENGTH_SHORT).show()
                selectedStudentList.clear()
                init()

            }
            else{
                Log.d(TAG, "비어 있음")
            }
        }

        binding.fragmentProHomeSettingButton.setOnClickListener{
            findNavController().navigate(R.id.navigation_setting)
        }

        // 뒤로가기 버튼 처리
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (ApplicationClass.sharedPreferences.getString("memberEmail").isNullOrEmpty()) {
                    // 로그인 정보가 없는 경우, 로그인 화면으로 이동
                    findNavController().navigate(R.id.action_ProHomeFragment_to_loginFragment)

                    // 앱 종료
//                    requireActivity().finish()
                } else {
                    // 뒤로가기 동작 수행
                    isEnabled = false
                    requireActivity().onBackPressed()
                    // 앱 종료
                    requireActivity().finish()
                }
            }
        })

    }


    fun init(){


        sharedViewModel.classInfoList.observe(viewLifecycleOwner){
            // 반 정보 받아 오기
            sharedViewModel.classInfoList.value?.let { initstudentList(it.get(currentClass)) }
            Log.d(TAG, "반 정보:  ${it.size}")

            // 반 갯수 별로 toggleButton 변경
            when(it.size){
                1 -> {
                    binding.fragmentProHomeCustomtoggleButton.moneyText.visibility = View.GONE
                    binding.fragmentProHomeCustomtoggleButton.lockerText.visibility = View.GONE
                    binding.fragmentProHomeCustomtoggleButton.seatText.setText("${it.get(0)?.classCode} 반")
                }
                2 -> {
                    binding.fragmentProHomeCustomtoggleButton.moneyText.visibility = View.GONE
                    binding.fragmentProHomeCustomtoggleButton.lockerText.visibility = View.VISIBLE
                    binding.fragmentProHomeCustomtoggleButton.seatText.setText("${it.get(0)?.classCode} 반")
                    binding.fragmentProHomeCustomtoggleButton.lockerText.setText("${it.get(1)?.classCode} 반")

                }
                3 -> {
                    binding.fragmentProHomeCustomtoggleButton.seatText.setText("${it.get(0)?.classCode} 반")
                    binding.fragmentProHomeCustomtoggleButton.lockerText.setText("${it.get(1)?.classCode} 반")
                    binding.fragmentProHomeCustomtoggleButton.moneyText.setText("${it.get(2)?.classCode} 반")

                }

                0 -> {
                    Toast.makeText(requireContext(), "반 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()

                }
            }
            binding.fragmentProHomeCustomtoggleButton.setFirstButtonClickListener {
                currentClass = 0
                selectedStudentList.clear()
                sharedViewModel.classInfoList.value?.let { initstudentList(it.get(0)) }
                progressBarUpdate()

            }
            binding.fragmentProHomeCustomtoggleButton.setSecondButtonClickListener {
                currentClass = 1
                selectedStudentList.clear()
                sharedViewModel.classInfoList.value?.let { initstudentList(it.get(1)) }
                progressBarUpdate()

            }
            binding.fragmentProHomeCustomtoggleButton.setThirdButtonClickListener {
                currentClass = 2
                selectedStudentList.clear()
                sharedViewModel.classInfoList.value?.let { initstudentList(it.get(2)) }
                progressBarUpdate()
            }

        }



    }




    fun progressBarUpdate() {
        binding.fragmentProHomeArcProgressBar.progressText.text = "출석현황"
        val section = DonutSection(
            name = "1",
            color = Color.BLUE,
            amount = attendedPercent
        )
        binding.fragmentProHomeArcProgressBar.arcProgressBar.submitData(listOf(section))
        binding.fragmentProHomeArcProgressBar.progressRate.text = "$attendedPercent%"
    }
    fun initstudentList(classInfo : Classinfo){
        //해당 반 학생 출석 불러오기


        Log.d(TAG, "속성 값들 : ${classInfo.classCode}, ${classInfo.regionCode}, ${classInfo.generationCode}")
        proHomeFragmentViewModel.getStudentsInfo(classInfo.classCode, classInfo.regionCode, classInfo.generationCode)

        adapter = UserInfoAdapter(requireContext())

        binding.fragmentProHomeRecyclerView.adapter = adapter

        proHomeFragmentViewModel.studentInfoList.observe(viewLifecycleOwner) {
            var attendedCount  = 0f
            Log.d(TAG, "학생들: $it")
            adapter.submitList(it)
            for(student : Member in it){
                if(student.attended == "입실"){
                    attendedCount++;
                }

            }
            attendedPercent = (attendedCount/it.size.toFloat() * 100).toInt().toFloat()
            progressBarUpdate()

        }
        adapter.itemClickListener = object : UserInfoAdapter.ItemClickListener{


            override fun onClick(view: View, position: Int, data: Member, checked: Boolean) {
                if(checked){
                    selectedStudentList.add(data)
                }
                else{
                    selectedStudentList.remove(data)
                }

            }

        }

    }


    fun convertLongToSqlTime(time: Long): Time {
        return Time(time)
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}