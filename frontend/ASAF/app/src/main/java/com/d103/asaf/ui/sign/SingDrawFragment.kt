package com.d103.asaf.ui.sign


import android.os.Bundle
import android.view.View
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentSingDrawBinding

class SingDrawFragment : BaseFragment<FragmentSingDrawBinding>(FragmentSingDrawBinding::bind, R.layout.fragment_sing_draw) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val draw = binding.signPaint

        binding.resetBtn.setOnClickListener {
            draw.reset()
        }
    }
}