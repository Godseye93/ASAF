import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.databinding.FragmentMoneyBinding
import com.d103.asaf.ui.op.OpFragment
import com.d103.asaf.ui.op.OpFragmentViewModel
import com.d103.asaf.ui.op.adapter.MoneyAdapter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


private const val PATH = "/data/data/com.ssafy.imagecomb/files/"
class MoneyFragment :
    BaseFragment<FragmentMoneyBinding>(FragmentMoneyBinding::bind, R.layout.fragment_money) {
    companion object {
        private const val SIGN = "sign"

        fun instance(sign: MutableList<DocSign>, parentViewModel: OpFragmentViewModel): MoneyFragment {
            val fragment = MoneyFragment()
            fragment.viewModel = parentViewModel
            val args = Bundle()
            args.putParcelableArrayList(SIGN, ArrayList(sign))
            fragment.arguments = args
            return fragment
        }
    }

    private var viewModel: OpFragmentViewModel = OpFragment.parentViewModel!!
    private lateinit var adapter: MoneyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // requestExternalStoragePermission()

        CoroutineScope(Dispatchers.Main).launch {
            if(isAdded){
                viewModel.signs.collect { newList ->
                    Log.d("사인불러오기", "onViewCreated: ${viewModel.signs.value}")
                    adapter.submitList(newList)
                }
            }
        }

        adapter = MoneyAdapter()
        binding.fragmentMoneyRecyclerview.adapter = adapter
        // 이미지 합치기
        binding.fragmentMoneyImageComb.setOnClickListener {
            val picturesDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val absolutePath = "/data/data/com.d103.asaf"
            lifecycleScope.launch {
                createCombinedImageFromUrls(viewModel.signUrls.value, "$absolutePath/combinded.png", 100)
            }
        }
    }

    // 이미지 주소 리스트로부터 이미지 파일 생성
    suspend fun createCombinedImageFromUrls(imageUrls: List<String>, filePath: String, spacing: Int) {
        val bitmapList = imageUrls.mapNotNull { getBitmapFromUrl("http://i9d103.p.ssafy.io/$it") }

        // 비트맵들을 수직으로 합치기 (간격 추가)
        val combinedBitmap = combineBitmapsVertically(bitmapList, spacing)

        // 합쳐진 비트맵을 이미지 파일로 저장
        combinedBitmap?.let {
            saveBitmapToFile(it, filePath)
        }
    }

    suspend fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    // 비트맵들을 수직으로 합치기
    fun combineBitmapsVertically(bitmapList: List<Bitmap>, spacing: Int): Bitmap? {
        if (bitmapList.isEmpty()) return null

        // 비트맵들을 수직으로 합친 새로운 비트맵 생성
        val totalHeight = bitmapList.sumOf { it.height } + (spacing * (bitmapList.size - 1))
        val combinedBitmap = Bitmap.createBitmap(bitmapList[0].width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(combinedBitmap)

        // 비트맵들을 캔버스에 수직으로 그리기
        var top = 0
        for (bitmap in bitmapList) {
            canvas.drawBitmap(bitmap, 0f, top.toFloat(), null)
            top += bitmap.height + spacing // 간격 추가
        }

        return combinedBitmap
    }

    // 비트맵을 이미지 파일로 저장
    fun saveBitmapToFile(bitmap: Bitmap, filePath: String) {
        try {
            val file = File(filePath)
            if(file.exists()) deleteFile(filePath)
            Log.d("서명합치기", "saveBitmapToFile: $filePath $file")
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()
            sendEmail(filePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    // 삭제 함수
    fun deleteFile(filePath: String): Boolean {
        Log.d("서명합치기", "deleteFile: 파일있음")
        val file = File(filePath)
        return file.delete()
    }

    // 저장 후 메일로 보내 주는 코드 추가
    private fun sendEmail(path: String) {
        MaildroidX.Builder()
            .smtp("live.smtp.mailtrap.io")
            .smtpUsername("api")
            .smtpPassword("0647ceab68282d673bdd53a351635833")
            .port("587")
            .type(MaildroidXType.HTML)
            .to("kieanupark@gmail.com")
            .from("mailtrap@asaf.live")
            .subject("합쳐진 서명")
            .body("합쳐진 서명 파일 입니다.")
            .attachment(path)
            .isStartTLSEnabled(true)
            .mail()

        Log.d("메일", "sendEmail: 보냄")
    }
    // 권한 요청을 위한 메서드 호출
//    private fun requestExternalStoragePermission() {
//        TedPermission.create()
//            .setPermissionListener(permissionListener)
//            .setDeniedMessage("권한이 거부되었습니다. 설정에서 권한을 허용해주세요.")
//            .setPermissions(
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            )
//            .check()
//    }
//
//    // 권한 요청 결과 처리
//    private val permissionListener: PermissionListener = object : PermissionListener {
//        override fun onPermissionGranted() {
//            // 권한이 부여된 경우 파일 접근 및 작업 수행
//        }
//
//        override fun onPermissionDenied(deniedPermissions: List<String>) {
//            Toast.makeText(context, "저장소 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }
}
