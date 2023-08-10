package com.d103.asaf.ui.noti

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.Room.NotiMessage
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentStudentNotiBinding
import com.d103.asaf.ui.schedule.ItemTouchHelperCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentNotiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentNotiFragment : BaseFragment<FragmentStudentNotiBinding> (FragmentStudentNotiBinding::bind, R.layout.fragment_student_noti) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel : StudentNotiFragmentViewModel by viewModels()
    private lateinit var adapter : NotiMessageAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       init()
    }

    fun init(){
        Log.d("시작", "init: 시작")
//        TEST DATA
//        CoroutineScope(Dispatchers.IO).launch {
//            ApplicationClass.notiMessageDatabase.notiMessageDao.saveNotiMessage(NotiMessage(0,"TEST 1", "TEST 1", System.currentTimeMillis(), "TESTER 1 ", ""))
//            ApplicationClass.notiMessageDatabase.notiMessageDao.saveNotiMessage(NotiMessage(0,"TEST 2", "TEST 2", System.currentTimeMillis(), "TESTER 2 ", ""))
//            ApplicationClass.notiMessageDatabase.notiMessageDao.saveNotiMessage(NotiMessage(0,"TEST 3", "TEST 3", System.currentTimeMillis(), "TESTER 3 ", ""))
//            ApplicationClass.notiMessageDatabase.notiMessageDao.saveNotiMessage(NotiMessage(0,"TEST 4", "TEST 4", System.currentTimeMillis(), "TESTER 4 ", ""))
//
//        }


        adapter = NotiMessageAdapter(requireContext(), this)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentStudentNotiRecyclerView.layoutManager = layoutManager
        binding.fragmentStudentNotiRecyclerView.adapter = adapter

        // 스와이프
        val itemTouchHelperCallback = ItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.fragmentStudentNotiRecyclerView)


//        viewModel.getAll()



        viewModel.allNotiMessages.observe(viewLifecycleOwner){
            var sorted = viewModel.allNotiMessages.value?.sortedByDescending {it.sendTime }
            adapter.submitList(sorted)
        }

        binding.fragmentStudentNotiImageviewArrowBack.setOnClickListener {
            findNavController().navigateUp()
            (requireActivity() as MainActivity).showStudentBottomNaviagtionBarFromFragment()
        }
    }

    fun deleteMessage(data  : NotiMessage){
        try{
            CoroutineScope(Dispatchers.IO).launch {
                ApplicationClass.notiMessageDatabase.notiMessageDao.deleteNotiMessage(data)
            }
        }catch (e : Exception){
            Log.d("NOTI DELETE ERROR", "deleteMessage: ${e.message}")
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentNotiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}