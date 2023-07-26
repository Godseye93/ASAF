package com.d103.asaf.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.d103.asaf.R
import com.d103.asaf.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
import com.d103.asaf.MainActivity

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Bottom Navigation Bar를 숨김
//        hideBottomBar()

        setupViews()
        observeViewModel()

        // MainActivity의 hideBottomNavigationBarFromFragment() 메서드를 호출하여 바텀 네비게이션 바를 숨김
//        (activity as? MainActivity)?.hideBottomNavigationBarFromFragment()

        return binding.root
    }

    private fun setupViews() {
        binding.fragmentLoginButtonLogin.setOnClickListener {
            val email = binding.fragmentLoginEditTvId.text.toString()
            val password = binding.fragmentLoginEditTvPass.text.toString()
            viewModel.login(email, password)

            findNavController().navigate(R.id.action_loginFragment_to_ProhomeFragment)
        }

        binding.fragmentLoginButtonJoin.setOnClickListener {
            // 회원가입 버튼 클릭 시 화면 전환 처리 (Navigation Component 사용)
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

    private fun hideBottomBar() {
        // MainActivity의 hideBottomNavigationBar() 메서드를 호출하여 사용합니다.
        if (activity is MainActivity) {
            (activity as MainActivity).hideBottomNavigationBarFromFragment()
        }
    }

}
