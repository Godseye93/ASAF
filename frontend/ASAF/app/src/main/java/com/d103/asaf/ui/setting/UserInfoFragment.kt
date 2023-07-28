package com.d103.asaf.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.d103.asaf.databinding.FragmentSettingUserinfoBinding

class UserInfoFragment: Fragment() {
    private var _binding: FragmentSettingUserinfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserInfoFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingUserinfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(UserInfoFragmentViewModel::class.java)

        // Replace "user@example.com" with the actual user's email
        /////////////////////////////////////////////////////////// 저장된 사용자 이메일 가져와서 넣어줌.
        viewModel.getUserInfo("user@example.com")

        // Observe changes in user information
        viewModel.userInfo.observe(viewLifecycleOwner) { member ->
            // Set the retrieved user information to EditText views
//            binding.fragmentSettingUserinfoImageviewProfile.setImageURI(uri)      // 프로필 사진 수정 필요 ~~
            binding.fragmentSettingUserinfoEditTVName.setText(member.memberName)
            binding.fragmentSettingUserinfoEditTVEmail.setText(member.memberEmail)
//            binding.fragmentSettingUserinfoEditTVPass.setText(member.memberPassword)
//            binding.fragmentSettingUserinfoEditTVPassConfirm.setText(member.memberPassword)
            binding.fragmentSettingUserinfoEditTVBirth.setText(member.birthDate)
//            binding.fragmentSettingUserinfoSpinnerNth         // 기수 지역 반 수정
        }

        binding.fragmentSettingUserinfoButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰 바인딩 해제
        _binding = null
    }
}