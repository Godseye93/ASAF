package com.d103.asaf.ui.op

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentOpBinding
import kotlinx.coroutines.CoroutineScope


class OpFragment : BaseFragment<FragmentOpBinding>(FragmentOpBinding::bind, R.layout.fragment_op) {
    private val viewModel: OpFragmentViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.postDelayed({
            childFragmentManager.beginTransaction()
                .replace(binding.frameLayout.id,SeatFragment())
                .commit()
        }, 100)
    }
}