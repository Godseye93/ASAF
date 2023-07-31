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
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.ui.home.student.StudentHomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "LoginFragment_cjw"
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginFragmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
//        setupViews()
//        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ApplicationClass.sharedPreferences.getString("memberEmail")?.isNotEmpty() == true) {
            if(ApplicationClass.sharedPreferences.getString("authority") == "stu"){
                findNavController().navigate(R.id.navigation_student_home)
            }
            else{
                findNavController().navigate(R.id.action_loginFragment_to_ProhomeFragment)
            }
        }
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.fragmentLoginButtonLogin.setOnClickListener {
            val email = binding.fragmentLoginEditTvId.text.toString()
            val password = binding.fragmentLoginEditTvPass.text.toString()
            viewModel.login(email, password)
        }

        binding.fragmentLoginTextviewForgetpass.setOnClickListener {
            findNavController().navigate(R.id.findpwd_fragment)
        }

        binding.fragmentLoginButtonJoin.setOnClickListener {
            findNavController().navigate(R.id.join_fragment)
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { loginResult ->
            if (loginResult != Member()) {
                Toast.makeText(context, "${loginResult.memberName}님, 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                ApplicationClass.sharedPreferences.addUserByEmailAndPwd(loginResult)
                sharedViewModel.getClassInfo(loginResult)
                if(loginResult.authority == "stu"){
                    findNavController().navigate(R.id.navigation_student_home)
                }else{
                    findNavController().navigate(R.id.action_loginFragment_to_ProhomeFragment)
                }

            } else {
                Toast.makeText(context, "ID 혹은 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
