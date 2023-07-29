package com.d103.asaf.ui.op

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentLockerBinding
import com.d103.asaf.ui.op.adapter.LockerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LockerFragment : BaseFragment<FragmentLockerBinding>(FragmentLockerBinding::bind, R.layout.fragment_locker) {
    companion object {
        private const val LOCKER = "locker"

        // Int를 LockerDto로 변경 필요
        fun instance(locker: MutableList<Int>): LockerFragment {
            val fragment = LockerFragment()
            val args = Bundle()
            args.putIntegerArrayList(LOCKER, ArrayList(locker))
            fragment.arguments = args
            return fragment
        }
    }

    private var lockers: MutableList<Int> = mutableListOf()
    private lateinit var adapter: LockerAdapter
    private val viewModel: OpFragmentViewModel by viewModels()

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
            lockers.shuffle()
            adapter.submitList(viewModel.setLockers(lockers))
        }
    }
}