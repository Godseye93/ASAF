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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.d103.asaf.R
import com.d103.asaf.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
import com.d103.asaf.MainActivity
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentScheduleBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "LoginFragment_cjw"
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login) {
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel: LoginFragmentViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()


//        (requireActivity() as MainActivity).hideBottomNavigationBarFromFragment()

        //        sharedPreference에서 있으면 바로 화면 넘어가기
        if(ApplicationClass.sharedPreferences.getString("email")?.isNotEmpty() == true){
            Log.d(TAG, "onViewCreated: ${ApplicationClass.sharedPreferences.getString("email")}")

//            val action = LoginFragmentDirections.actionLoginFragmentToMapFragment()
//            Navigation.findNavController(binding.root).navigate(action)
            findNavController().navigate(R.id.action_loginFragment_to_ProhomeFragment)
//            (requireActivity() as MainActivity).showBottomNavigationBarFromFragment()
//            (requireActivity() as MainActivity).hideBottomNavigationBarFromFragment()
        }

        
        // MainActivity의 hideBottomNavigationBarFromFragment() 메서드를 호출하여 바텀 네비게이션 바를 숨김
//        (activity as? MainActivity)?.hideBottomNavigationBarFromFragment()
//        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navi_student).visibility = View.GONE
//        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navi_pro).visibility = View.GONE
    }


    private fun setupViews() {
        binding.fragmentLoginButtonLogin.setOnClickListener {
            val email = binding.fragmentLoginEditTvId.text.toString()
            val password = binding.fragmentLoginEditTvPass.text.toString()
            viewModel.login(email, password)

            observeLoginResult()
            ApplicationClass.sharedPreferences.addUserByEmailAndPwd(email, password)

//            findNavController().navigate(R.id.action_loginFragment_to_ProhomeFragment)

            // MainActivity의 hideBottomNavigationBarFromFragment() 메서드를 호출하여 바텀 네비게이션 바를 숨김
//            (activity as? MainActivity)?.showBottomNavigationBarFromFragment()
        }

        // 비밀번호 찾기 클릭 시
        binding.fragmentLoginTextviewForgetpass.setOnClickListener {
            // 화면 이동
            findNavController().navigate(R.id.findpwd_fragment)
        }

        // 회원가입 버튼 클릭 시
        binding.fragmentLoginButtonJoin.setOnClickListener {
            // 화면 전환 처리 (Navigation Component 사용)
            findNavController().navigate(R.id.join_fragment)
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { loginResult ->
            // 로그인 결과를 처리하는 로직 구현
            if (loginResult) {
                // 로그인 성공 시, 처리할 작업
                Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 로그인 실패 시, 처리할 작업
//                Toast.makeText(context, "ID 혹은 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { isLoginSuccess ->
            if (isLoginSuccess) {
                Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                // 로그인 성공 시 ProHomeFragment로 이동



                findNavController().navigate(R.id.action_loginFragment_to_ProhomeFragment)
            } else {
                // 로그인 실패 시 Toast 메시지 표시
                Toast.makeText(requireContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }



}
