package com.d103.asaf.ui.sign


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentSignDrawBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.template.util.SharedPreferencesUtil

class SignDrawFragment : BaseFragment<FragmentSignDrawBinding>(FragmentSignDrawBinding::bind, R.layout.fragment_sign_draw) {
    companion object {
        var draw : DrawSign? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        draw = binding.signPaint

        binding.apply{
            resetBtn.setOnClickListener {
                draw?.reset()
            }
            signNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_signDrawFragment_to_signNextFragment)
            }
            signload.setOnClickListener {
                Log.d("사인로드", "onViewCreated: 불러오기")
                draw?.setSign(ApplicationClass.sharedPreferences.loadPoints(),"SignDrawFragment")
            }
            signsave.setOnClickListener {
                Log.d("사인저장", "onViewCreated: 세이브")
                ApplicationClass.sharedPreferences.savePoints(draw?.getSign() ?: listOf())
            }
        }
    }
}