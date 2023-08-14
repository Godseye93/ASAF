package com.d103.asaf.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.d103.asaf.R
import com.d103.asaf.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
import com.d103.asaf.MainActivity
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass

import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentScheduleBinding


import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.ui.home.student.StudentHomeFragment
import com.google.android.gms.tasks.OnCompleteListener

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "LoginFragment_cjw"
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: LoginFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            if (token != null) {
                ApplicationClass.sharedPreferences.addFCMToken(token)
            }
//            Log.d(TAG, msg)1111
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        Log.d(TAG, "FCM: ${ApplicationClass.sharedPreferences.getString("token")}")
        setupViews()
        observeViewModel()

//        (requireActivity() as MainActivity).hideBottomNavigationBarFromFragment()

        //        sharedPreference에서 있으면 바로 화면 넘어가기
        if (ApplicationClass.sharedPreferences.getString("memberEmail")?.isNotEmpty() == true) {

            //Shared Preference에 저장된 값이 있으면 이메일 정보로 유저 정보 가져오기
            lifecycleScope.launch {
                try {
                    Log.d(TAG, "onViewCreated: ${ApplicationClass.sharedPreferences.getInt("id")!!}")
                    val response = RetrofitUtil.attendenceService.getClassInfo(
                        ApplicationClass.sharedPreferences.getInt("id")!!
                    )
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d(TAG, "반 정보: ${response.body()}")
                        if (!responseBody.isNullOrEmpty()) {
                            sharedViewModel.postClassInfoList(responseBody)
                            ApplicationClass.mainClassInfo = responseBody
                            Log.d(TAG, "onViewCreated: ddddddd")
                        } else {
                            Log.d(TAG, "onViewCreated: xxxxxx")
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onViewCreated: $e")
                }
            }
            if (ApplicationClass.sharedPreferences.getString("authority") == "교육생") {
                findNavController().navigate(R.id.action_login_fragment_to_StudentHomeFragment)
                findNavController().popBackStack()
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_ProhomeFragment)
                findNavController().popBackStack()
            }
        }else{
//            findNavController().popBackStack()
        }
    }

    private fun setupViews() {
        binding.fragmentLoginButtonLogin.setOnClickListener {
            val email = binding.fragmentLoginEditTvId.text.toString()
            val password = binding.fragmentLoginEditTvPass.text.toString()
            viewModel.login(email, password, ApplicationClass.sharedPreferences.getString("token")!!)
        }

        binding.fragmentLoginTextviewForgetpass.setOnClickListener {
            findNavController().navigate(R.id.findpwd_fragment)
//            findNavController().popBackStack()
        }

        binding.fragmentLoginButtonJoin.setOnClickListener {
            findNavController().navigate(R.id.join_fragment)
//            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { loginResult ->
            if (loginResult != Member()) {
                Toast.makeText(
                    context,
                    "${loginResult.memberName}님, 로그인 되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
//                if(binding.fragmentLoginSwitchAutologin.isChecked){
//                    ApplicationClass.sharedPreferences.addUserByEmailAndPwd(loginResult)
                    ////
//                }


                ApplicationClass.sharedPreferences.addUserByEmailAndPwd(loginResult)
                lifecycleScope.launch{
                    viewModel.addClassInfo(loginResult.memberEmail)
                    Log.d(TAG, "observeViewModel: addClassInfo 수행했고, ApplicationClass에 저장했어요. ")
                }
                ApplicationClass.sharedPreferences.autoLoginIsChecked(binding.fragmentLoginSwitchAutologin.isChecked)

                sharedViewModel.logInUser = loginResult
                Log.d(TAG, "observeViewModel______: ${sharedViewModel.logInUser.memberEmail}")
                Log.d(TAG, "유저: ${sharedViewModel.logInUser}")
                sharedViewModel.getClassInfo(loginResult)

                Log.d(TAG, "담당 반: ${sharedViewModel.classInfoList.value?.size}")
                if (loginResult.authority == "교육생") {

                    findNavController().navigate(R.id.action_login_fragment_to_StudentHomeFragment)
                } else {
                    findNavController().navigate(R.id.action_loginFragment_to_ProhomeFragment)
                }

            } else {
                Toast.makeText(context, "ID 혹은 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
