package com.d103.asaf.ui.home.student

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcB
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentStudentHomeBinding
import androidx.biometric.BiometricPrompt
import java.util.Locale

private const val TAG = "StudentHomeFragment_cjw ASAF"
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentHomeFragment  : BaseFragment<FragmentStudentHomeBinding>(FragmentStudentHomeBinding::bind, R.layout.fragment_student_home) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val stuHomeFragmentViewModel: StudentHomeFragmentViewModel by viewModels()

    // 지문
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    // Add this property at the top of the fragment
    private var nfcPendingIntent: PendingIntent? = null

    // 애니메이션 부분
    private lateinit var cardView1: CardView
    private lateinit var cardView2: CardView
    private lateinit var buttonFlip: Button
    private var isFirstCardVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (requireActivity() as MainActivity).showStudentBottomNavigationBar()

        initView()

//        // Set up NFC PendingIntent
//        val nfcIntent = Intent(requireContext(), javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        nfcPendingIntent = PendingIntent.getActivity(
//            requireContext(),
//            0,
//            nfcIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        // 알림? 진동? 페이지
        binding.fragmentStuHomeImagebuttonNotification.setOnClickListener {

        }
        // 설정 페이지
        binding.fragmentStuHomeImagebuttonUserinfo.setOnClickListener {
            findNavController().navigate(R.id.navigation_setting)
        }
        // 지문 인식 -> nfc 태그 생성
//        setupBiometricAuthentication()
        binding.buttonNFC.setOnClickListener {
            if(checkBiometricAvailability()) authenticateWithFingerprint()
        }

        // 뒤로가기 버튼 처리
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (ApplicationClass.sharedPreferences.getString("memberEmail").isNullOrEmpty()) {
                    // 로그인 정보가 없는 경우, 로그인 화면으로 이동
                    findNavController().navigate(R.id.action_StudentHomeFragment_to_loginFragment)

                    // 앱 종료
//                    requireActivity().finish()
                } else {
                    // 뒤로가기 동작 수행
                    isEnabled = false
                    requireActivity().onBackPressed()
                    // 앱 종료
                    requireActivity().finish()
                }
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun flipCards() {
        val currentCard = if (isFirstCardVisible) cardView1 else cardView2
        val nextCard = if (isFirstCardVisible) cardView2 else cardView1

        val objectAnimatorFirst =
            ObjectAnimator.ofFloat(currentCard, View.ROTATION_Y, 0f, 90f)
        objectAnimatorFirst.duration = 500
        objectAnimatorFirst.start()

        objectAnimatorFirst.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                // Hide the current card after the first animation
                currentCard.visibility = View.INVISIBLE
                // Show the next card before the second animation
                nextCard.visibility = View.VISIBLE

                val objectAnimatorSecond =
                    ObjectAnimator.ofFloat(nextCard, View.ROTATION_Y, -90f, 0f)
                objectAnimatorSecond.duration = 500
                objectAnimatorSecond.start()

                isFirstCardVisible = !isFirstCardVisible
            }
        })
    }

    fun initView(){

        cardView1 = binding.fragmentStudentHomeCardViewFront
        cardView2 = binding.fragmentStudentHomeCardViewBack
        buttonFlip = binding.buttonNFC

        // Set initial visibility
        cardView1.visibility = View.VISIBLE
        cardView2.visibility = View.GONE

        // Set cameraDistance for the card views
        val scale = resources.displayMetrics.density
        cardView1.cameraDistance = 8000 * scale
        cardView2.cameraDistance = 8000 * scale

        cardView1.setOnClickListener {
            flipCards()
        }
        cardView2.setOnClickListener{
            flipCards()
        }
    }

    // 지문 인식
    private fun authenticateWithFingerprint() {
        val executor = ContextCompat.getMainExecutor(requireContext())

        val biometricPrompt = BiometricPrompt(
            requireActivity(),
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        requireContext(),
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    ).show()

//                    // 태그 데이터 생성
//                    val tagData = "95:02:E6:F1"
//
//                    // NFC 태그 생성
//                    val nfcMessage = createNfcMessage(tagData)
//
//                    // NFC 태그 전송
//                    val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
//                    if (nfcAdapter != null && nfcAdapter.isEnabled) {
//                        val nfcTechList = arrayOf(arrayOf(IsoDep::class.java.name), arrayOf(NfcB::class.java.name))
//                        val nfcTechIntent = Intent(requireContext(), javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                        val nfcTechPendingIntent = PendingIntent.getActivity(
//                            requireContext(),
//                            0,
//                            nfcTechIntent,
//                            PendingIntent.FLAG_IMMUTABLE
//                        )
//                        nfcAdapter.enableForegroundDispatch(requireActivity(), nfcTechPendingIntent, null, nfcTechList)
//
//                        // 이제 사용자가 NFC 기기에 태그를 찍으면 nfcMessage가 전송됩니다.
//                    } else {
//                        Toast.makeText(
//                            requireContext(),
//                            "NFC가 활성화되지 않았거나 지원되지 않습니다.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문 인식") // 다이얼로그 타이틀 설정
            .setDescription("지문을 사용하여 인증합니다.") // 다이얼로그 설명 설정
            .setNegativeButtonText("취소") // 취소 버튼 텍스트 설정
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkBiometricAvailability(): Boolean {
        val biometricManager = BiometricManager.from(requireContext())
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(requireContext(), "생체 인식을 지원하는 하드웨어가 없습니다.", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(requireContext(), "생체 인식 하드웨어를 사용할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(requireContext(), "생체 인식이 등록되어 있지 않습니다.", Toast.LENGTH_SHORT)
                    .show()
                showBiometricEnrollmentSettings()
                false
            }
            else -> {
                Toast.makeText(requireContext(), "생체 인식을 사용할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
                false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showBiometricEnrollmentSettings() {
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG)
        }
        if (enrollIntent.resolveActivity(requireContext().packageManager) != null) {
            startBiometricEnrollmentResult.launch(enrollIntent)
        } else {
            Toast.makeText(requireContext(), "생체 인식 등록 화면을 열 수 없습니다.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val startBiometricEnrollmentResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // 사용자가 지문 인식 등록에 성공적으로 완료한 경우
                Toast.makeText(requireContext(), "지문 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 사용자가 지문 인식 등록을 취소하거나 실패한 경우
                Toast.makeText(requireContext(), "지문 등록을 취소하였거나 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

//    private fun createNfcMessage(tagData: String): NdefMessage {
//        val textRecord = NdefRecord.createTextRecord(null, tagData)
//        return NdefMessage(arrayOf(textRecord))
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
//        nfcAdapter?.enableForegroundDispatch(requireActivity(), nfcPendingIntent, null, null)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
//        nfcAdapter?.disableForegroundDispatch(requireActivity())
//    }

    // Implement the onNewIntent method to handle NFC tag discovery
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        // Handle NFC tag discovery here
//        // Extract the NFC tag data and perform your desired actions
//
//        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
//            val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) ?: return
//
//            // Check if the tag supports IsoDep technology
//            val isoDep = IsoDep.get(tag)
//            if (isoDep != null) {
//                // Tag supports IsoDep, you can now communicate with the tag using IsoDep technology
//                // Do your desired actions with the tag
//            } else {
//                // Tag does not support IsoDep technology
//                // Check if the tag supports NfcB technology
//                val nfcB = NfcB.get(tag)
//                if (nfcB != null) {
//                    // Tag supports NfcB, you can now communicate with the tag using NfcB technology
//                    // Do your desired actions with the tag
//                } else {
//                    // Tag does not support IsoDep or NfcB technology
//                    // Handle the case where the tag technology is not supported
//                }
//            }
//        }
//    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }
}