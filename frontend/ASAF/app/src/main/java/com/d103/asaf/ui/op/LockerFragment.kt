package com.d103.asaf.ui.op

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentLockerBinding
import com.d103.asaf.ui.op.adapter.LockerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    private var  lockers: MutableList<Int> = MutableList(80) { 0 }
    private lateinit var adapter: LockerAdapter
    lateinit var viewModel: OpFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments로부터 리스트를 가져와서 변수에 할당합니다.
        lockers = requireArguments().getIntegerArrayList(LOCKER) ?: mutableListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted  {
            viewModel.lockers.collect { newLocker ->
                adapter.submitList(viewModel.setLockers(newLocker))// 업데이트
            }
        }

        adapter = LockerAdapter()
        binding.fragmentLockerRecyclerview.adapter = adapter

        binding.lockerRandom.setOnClickListener {
            Log.d("사물함랜덤배치", "onViewCreated: ddddd")
            lockers.shuffle()
            adapter.submitList(viewModel.setLockers(lockers))
        }

        binding.lockerComplete.setOnClickListener {
            postLockers()
        }
    }

    // 서버에서 유저 id로 조회하여 최초로 사물함 정보가 들어가 있는 상태라면 update로 처리해야함
    private fun postLockers() {
        // POST List<docLockers>
        CoroutineScope(Dispatchers.IO).launch {
            if(!RetrofitUtil.opService.postLockers(viewModel.docLockers))
                Toast.makeText(context,"사물함 업데이트 네트워크 오류", Toast.LENGTH_SHORT).show()
        }
    }
}