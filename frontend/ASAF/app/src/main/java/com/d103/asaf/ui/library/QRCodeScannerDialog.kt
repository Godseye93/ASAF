package com.d103.asaf.ui.library

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.d103.asaf.databinding.DialogQrcodeScannerBinding
import com.d103.asaf.ui.library.student.LibraryUseDrawFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory

class QRCodeScannerDialog : DialogFragment() {
    private lateinit var binding: DialogQrcodeScannerBinding

    companion object {
        fun newInstance(): QRCodeScannerDialog {
            return QRCodeScannerDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogQrcodeScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestCameraPermission()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeScanner.resume()
    }

    private fun requestCameraPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                startCameraScanner()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        TedPermission
            .create()
            .setPermissionListener(permissionListener)
            .setPermissions(Manifest.permission.CAMERA)
            .check()
    }

    private fun startCameraScanner() {
        val formats = listOf(
            BarcodeFormat.QR_CODE // QR 코드만 스캔할 수 있도록 설정합니다.
        )
        binding.barcodeScanner.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        binding.barcodeScanner.initializeFromIntent(requireActivity().intent)
        binding.barcodeScanner.decodeContinuous(callback)
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            // QR 코드가 스캔되면 바코드 결과를 처리하고 원하는 작업을 수행합니다.
            val qrCodeResult = result.text
            // 예를 들어, 다음과 같이 QR 코드 결과를 다른 곳으로 전달할 수 있습니다.
            try {
                val fragment = LibraryUseDrawFragment.instance(qrCodeResult.split("|"))
                // framelayout으로 바꿔야하나? dismiss는 없애야 하나? << 고려하기
                binding.barcodeScanner.isVisible = false
                childFragmentManager.beginTransaction()
                    .replace(binding.dialogFramelayout.id, fragment)
                    .addToBackStack(null)
                    .commit()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "잘못된 QR코드입니다.", Toast.LENGTH_SHORT).show()
            }
//            dismiss()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }
    
    fun dismissDialog() {
        dismiss()
    }
}