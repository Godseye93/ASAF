package com.d103.asaf.ui.home.student

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcB
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentStudentHomeBinding
import androidx.biometric.BiometricPrompt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import java.nio.charset.Charset
import java.util.Arrays

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
    private lateinit var nfcDialog: AlertDialog


    // 애니메이션 부분
    private lateinit var cardView1: CardView
    private lateinit var cardView2: CardView
    private lateinit var buttonFlip: ImageView
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

        initView()

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
        // information 문자열 split.
        var Nth: Int? = null
        var region: String? = null
        var classNum: Int? = null
        val info = parseInput(sharedViewModel.logInUser.memberInfo)
        if(info != null){
            // 기수, 지역, 반
            Nth = info.first
            region = info.second
            classNum = info.third
        }
        binding.apply {
//            fragmentStudentHomeCardViewFrontImage.setImageURI(sharedViewModel.logInUser.profileImage.toUri())
            fragmentStudentHomeCardViewFrontCardView1FrontName.text = sharedViewModel.logInUser.memberName
            fragmentStudentHomeCardViewFrontCardView1FrontNum.text = sharedViewModel.logInUser.studentNumber.toString()
            fragmentStudentHomeCardViewFrontTextviewNth.text = Nth.toString()
            fragmentStudentHomeCardViewFrontTextviewRegion.text = region
            fragmentStudentHomeCardViewFrontTextviewClass.text = classNum.toString()
        }

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

        biometricPrompt = BiometricPrompt(
            requireActivity(),
            executor,
            callback
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문 인식") // 다이얼로그 타이틀 설정
            .setDescription("지문을 사용하여 인증합니다.") // 다이얼로그 설명 설정
            .setNegativeButtonText("취소") // 취소 버튼 텍스트 설정
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private val callback = object : BiometricPrompt.AuthenticationCallback() {
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

            // tagData 생성
            val tagData = "95:02:E6:F1"

            // NFC 메시지 생성 및 전송
            sendNFCMessage(tagData)
            Log.d(TAG, "onAuthenticationSucceeded: $tagData")

            // 가로 모드로 전환
//            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            // NFC 다이얼로그 띄우기
            showNfcDialog()

        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(
                requireContext(),
                "Authentication failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun sendNFCMessage(tagData: String) {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled) {
                Toast.makeText(requireContext(), "NFC를 활성화해주세요.", Toast.LENGTH_SHORT).show()
                return
            }

            // Convert the tagData to bytes in ISO 14443-4 format
            val isoDepData = buildIsoDepData(tagData)

            // Check if NfcB is supported
            val techList = arrayOf(arrayOf(NfcB::class.java.name))

            // Get the NFC tag from the activity's intent
            val tagFromIntent = requireActivity().intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            if (tagFromIntent != null) {
                // NfcB tag
                val nfcBTag = NfcB.get(tagFromIntent)
                if (nfcBTag != null) {
                    try {
                        nfcBTag.connect()
                        nfcBTag.transceive(isoDepData)
                        nfcBTag.close()

                        // Handle successful communication with NfcB tag
                        // TODO: NfcB 통신이 성공적으로 수행된 후의 동작 처리
                        Toast.makeText(requireContext(), "NfcB 통신 성공", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        // Handle communication error
                        // TODO: NfcB 통신 중 오류가 발생한 경우의 처리
                        Toast.makeText(requireContext(), "NfcB 통신 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "NfcB를 지원하지 않는 카드 또는 기기입니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "NFC 태그를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "NFC를 지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNfcDialog() {

        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_nfc_card, null)
        builder.setView(view)
        nfcDialog = builder.create()
        nfcDialog.setCancelable(true)

        // 다이얼로그 닫기 버튼 처리
        val nfcCard = view.findViewById<ConstraintLayout>(R.id.fragment_nfc_card)
        nfcDialog.setOnCancelListener {
            nfcDialog.dismiss()
            disableNFC()
        }
        nfcCard.setOnClickListener {
            nfcDialog.dismiss()
            disableNFC()
        }

        nfcDialog.show()
    }

    private fun disableNFC() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        if (nfcAdapter != null && nfcAdapter.isEnabled) {
            nfcAdapter.disableForegroundDispatch(requireActivity())
            nfcAdapter.setNdefPushMessage(null, requireActivity(), requireActivity())
        }
    }

    // 태그 데이터를 ISO 14443-4 형식으로 변환하는 도우미 함수
    private fun buildIsoDepData(tagData: String): ByteArray {
        // ISO 14443-4 형식에는 어플리케이션 식별자 (AID)와 시리얼 번호가 포함되어야 합니다.
        val aid = "A0000002471001" // 어플리케이션에 맞는 AID로 변경해야 합니다.

        // ISO 14443-4 형식으로 AID와 시리얼 번호를 연결합니다.
        val isoDepData = "$aid$tagData".toByteArray(Charset.forName("UTF-8"))

        return isoDepData
    }

    // 태그가 주어진 기술을 지원하는지 확인하는 도우미 함수
    private fun tagTechListContains(techList: Array<String>, targetTechList: Array<Array<String>>): Boolean {
        for (tech in techList) {
            for (targetTech in targetTechList) {
                if (Arrays.equals(techList, targetTech)) {
                    return true
                }
            }
        }
        return false
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

    private fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }

    // information 정보 문자열 split.
    fun parseInput(input: String): Triple<Int, String, Int>? {
        // 정규식을 사용하여 입력 문자열을 구분합니다.
        val regex = """(\d+)(\D+)(\d+)""".toRegex()
        val matchResult = regex.find(input)

        // 정규식이 매칭되지 않으면 null을 반환합니다.
        if (matchResult == null || matchResult.groupValues.size != 4) {
            return null
        }

        // 매칭된 그룹에서 각각의 값을 추출합니다.
        val Nth = matchResult.groupValues[1].toInt()
        val region = matchResult.groupValues[2]
        val classNum = matchResult.groupValues[3].toInt()

        return Triple(Nth, region, classNum)
    }
}