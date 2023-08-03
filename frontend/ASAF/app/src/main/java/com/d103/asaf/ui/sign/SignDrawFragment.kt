package com.d103.asaf.ui.sign


import android.os.Bundle
import android.view.View
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentSignDrawBinding

class SignDrawFragment : BaseFragment<FragmentSignDrawBinding>(FragmentSignDrawBinding::bind, R.layout.fragment_sign_draw) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val draw = binding.signPaint

        binding.resetBtn.setOnClickListener {
            draw.reset()
        }
    }
}