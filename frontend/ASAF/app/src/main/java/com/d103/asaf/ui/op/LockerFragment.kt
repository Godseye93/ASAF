package com.d103.asaf.ui.op

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.DocLocker
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentLockerBinding
import com.d103.asaf.ui.op.adapter.LockerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LockerFragment : BaseFragment<FragmentLockerBinding>(FragmentLockerBinding::bind, R.layout.fragment_locker) {
    companion object {
        private const val LOCKER = "locker"

        // Int를 LockerDto로 변경 필요
        fun instance(locker: MutableList<Int>, parentViewModel: OpFragmentViewModel): LockerFragment {
            val fragment = LockerFragment()
            fragment.viewModel = parentViewModel
            val args = Bundle()
            args.putIntegerArrayList(LOCKER, ArrayList(locker))
            fragment.arguments = args
            return fragment
        }
    }

    private var lockers: MutableList<Int> = MutableList(80) { 0 }
    private lateinit var adapter: LockerAdapter
    private var viewModel: OpFragmentViewModel = OpFragment.parentViewModel!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments로부터 리스트를 가져와서 변수에 할당합니다.
        lockers = requireArguments().getIntegerArrayList(LOCKER) ?: mutableListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch  {
            if(isAdded) {
                viewModel.docLockers.collect { newLocker ->
                    Log.d("사물함부르기", "onViewCreated: 불리긴하니")
                    adapter.submitList(viewModel.docLockers.value)// 업데이트
                    Log.d("랜덤사물함전", "onViewCreated: ${viewModel.docLockers.value}")
                }
            }
        }

        adapter = LockerAdapter()
        binding.fragmentLockerRecyclerview.adapter = adapter

        binding.lockerRandom.setOnClickListener {
            val originalList =  viewModel.docLockers.value.toMutableList() // 80개의 사물함 정보
            val sublist = originalList.subList(0, viewModel.realLockerNum) // 학생 수 만큼 번호 자르기
            sublist.shuffle()
            val temp = viewModel.docLockers.value.toMutableList()
            for(i in 0 until viewModel.realLockerNum) temp[i] = sublist[i]
            viewModel.setDocLocker(temp)
        }

        binding.lockerComplete.setOnClickListener {
            lifecycleScope.launch{
                postLockers()
            }
        }
    }

    // 서버에서 유저 id로 조회하여 최초로 사물함 정보가 들어가 있는 상태라면 update로 처리해야함
//    private fun postLockers() {
//        // POST List<docLockers>
//        CoroutineScope(Dispatchers.IO).launch {
//            Log.d("사물함 보내기", "보내기: ${viewModel.docLockers}")
//            if(!RetrofitUtil.opService.postLockers(viewModel.docLockers))
//                Toast.makeText(context,"사물함 업데이트 네트워크 오류", Toast.LENGTH_SHORT).show()
//        }
//    }

    private suspend fun postLockers() {
        Log.d("사물함보내기", "postLockers: ${viewModel.docLockers.value.subList(0,viewModel.realLockerNum)}")
        try {
            val response = withContext(Dispatchers.IO) {
                // docLockers 포지션 정보 변경
                RetrofitUtil.opService.postLockers(viewModel.docLockers.value)
//                RetrofitUtil.opService.postLockers(viewModel.setLockerPositions(viewModel.docLockers.value.subList(0,viewModel.realLockerNum)))
            }
            if (response.isSuccessful) {

            } else {
                Toast.makeText(context,"사물함 업데이트 네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("사물함 변경", "사물함 오류: ${e.message}", e)
        }
    }
}