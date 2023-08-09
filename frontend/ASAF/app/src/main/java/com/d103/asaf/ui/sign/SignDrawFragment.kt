package com.d103.asaf.ui.sign


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentSignDrawBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.template.util.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignDrawFragment : BaseFragment<FragmentSignDrawBinding>(FragmentSignDrawBinding::bind, R.layout.fragment_sign_draw) {
    companion object {
        var draw : DrawSign? = null
        var regionCode = 0
        var regionName = ""
        var myClass: Classinfo? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            getRegionCode()
        }
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

        // Override the default back button behavior
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate to StudentHomeFragment
                findNavController().navigate(R.id.action_signDrawFragment_to_StudentHomeFragment)
            }
        })
    }

    private suspend fun getRegionCode() {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitUtil.attendenceService.getClassInfo(ApplicationClass.sharedPreferences.getInt("id"))
            }
            if (response.isSuccessful) {
                myClass = response.body()?.get(0)
                regionCode = myClass?.regionCode ?: 0
                getRegion(regionCode)
            } else {
                Toast.makeText(context, "지역 가져오기 네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("지역", "지역 오류: ${e.message}", e)
        }
    }

    private suspend fun getRegion(rCode: Int) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitUtil.attendenceService.getRegionName(rCode)
            }
            if (response.isSuccessful) {
                regionName = response.body() ?: ""
            } else {
                Toast.makeText(context, "지역 이름 가져오기 네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("지역", "지역이름 오류: ${e.message}", e)
        }
    }
}