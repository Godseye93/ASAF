package com.d103.asaf.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.d103.asaf.R
import com.d103.asaf.databinding.FragmentSettingBinding
import androidx.navigation.fragment.findNavController
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.util.RetrofitUtil

private const val TAG = "SettingFragment ASAF"
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    // SettingFragmentViewModel 인스턴스 생성
    private val viewModel: SettingFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 예시로 TextView에 "정보"를 설정하는 예시
        binding.fragmentInformationTextviewInformation.text = "정보"

        // '설정' 버튼 클릭 시
        binding.fragmentInformationLayoutSetting.setOnClickListener {
            findNavController().navigate(R.id.fragment_setting_app_setting)
        }

        // '개인 정보 변경' 버튼 클릭 시
        binding.fragmentInformationLayoutPersonalinformation.setOnClickListener {
            findNavController().navigate(R.id.fragment_setting_userinfo)
        }

        // '개인 정보 정책' 버튼 클릭 시
        binding.fragmentInformationLayoutPrivacypolicy.setOnClickListener {
            findNavController().navigate(R.id.fragment_setting_privacy_policy)
        }

        // '만든 사람들' 버튼 클릭 시
        binding.fragmentInformationLayoutDevelopers.setOnClickListener {
            findNavController().navigate(R.id.fragment_setting_developers)
        }

        binding.fragmentSettingImageviewArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.fragmentInformationTextviewLogout.setOnClickListener {
            ApplicationClass.sharedPreferences.deleteUser()
//            findNavController().navigate(R.id.action_navigation_setting_to_login_fragment)
            val navController = findNavController()
            navController.popBackStack(R.id.nav_graph, true);
//            ApplicationClass.sharedPreferences.deleteUser()
            Log.d(TAG, "onViewCreated: ${ApplicationClass.sharedPreferences.getString("memberEmail")}")
            navController.navigate(R.id.login_fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰 바인딩 해제
        _binding = null
    }
}
