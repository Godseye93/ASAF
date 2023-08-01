import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.futured.donut.DonutSection
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentMoneyBinding
import com.d103.asaf.ui.op.LockerFragment
import com.d103.asaf.ui.op.OpFragmentViewModel
import com.d103.asaf.ui.op.adapter.LockerAdapter
import com.d103.asaf.ui.op.adapter.MoneyAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URL

private const val PATH = "/data/data/com.ssafy.imagecomb/files/"
class MoneyFragment :
    BaseFragment<FragmentMoneyBinding>(FragmentMoneyBinding::bind, R.layout.fragment_money) {
    companion object {
        private const val SIGN = "sign"

        fun instance(sign: MutableList<DocSign>): MoneyFragment {
            val fragment = MoneyFragment()
            val args = Bundle()
            args.putParcelableArrayList(SIGN, ArrayList(sign))
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: OpFragmentViewModel by viewModels()
    private lateinit var adapter: MoneyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.signs.collect { newList ->
                adapter.submitList(newList)
            }
        }

        adapter = MoneyAdapter()
        binding.fragmentMoneyRecyclerview.adapter = adapter
        // 이미지 합치기
        binding.fragmentMoneyImageComb.setOnClickListener {
            val picturesDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val absolutePath = picturesDirectory.absolutePath
            lifecycleScope.launch {
                createCombinedImageFromUrls(viewModel.signUrls.value, absolutePath, 100)
            }
        }
    }

    // 이미지 주소 리스트로부터 이미지 파일 생성
    suspend fun createCombinedImageFromUrls(imageUrls: List<String>, filePath: String, spacing: Int) {
        val bitmapList = imageUrls.mapNotNull { getBitmapFromUrl(it) }

        // 비트맵들을 수직으로 합치기 (간격 추가)
        val combinedBitmap = combineBitmapsVertically(bitmapList, spacing)

        // 합쳐진 비트맵을 이미지 파일로 저장
        combinedBitmap?.let {
            saveBitmapToFile(it, filePath)
            sendEmail(filePath)
            // 전송 후 삭제
            deleteFile(filePath)
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
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    // 삭제 함수
    fun deleteFile(filePath: String): Boolean {
        val file = File(filePath)
        return file.delete()
    }

    // 저장 후 메일로 보내 주는 코드 추가
    private fun sendEmail(path: String) {
        MaildroidX.Builder()
            .smtp("smtp.mailtrap.live")
            .smtpUsername("api")
            .smtpPassword("0647ceab68282d673bdd53a351635833")
            .port("587")
            .type(MaildroidXType.HTML)
            .to("kieanupark@gmail.com")
            .from("mailtrap@asaf.live")
            .subject("hello")
            .body("body")
            .attachment(path)
            .isStartTLSEnabled(true)
            .mail()

        Log.d("메일", "sendEmail: 보냄")
    }
}
