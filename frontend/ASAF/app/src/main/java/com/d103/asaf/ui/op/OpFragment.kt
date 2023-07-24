package com.d103.asaf.ui.op

import MoneyFragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentOpBinding


class OpFragment : BaseFragment<FragmentOpBinding>(FragmentOpBinding::bind, R.layout.fragment_op) {
    private val viewModel: OpFragmentViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSeat()
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
}