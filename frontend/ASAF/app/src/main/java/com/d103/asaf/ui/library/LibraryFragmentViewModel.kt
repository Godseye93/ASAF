package com.d103.asaf.ui.library

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.DocLocker
import com.d103.asaf.common.model.dto.DocSeat
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.util.RetrofitUtil
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Hashtable

private val TAG = "LibraryFragmentViewModel"
class LibraryFragmentViewModel: ViewModel() {
    // <!---------------------------- 공통변수 ------------------------------->
    var curClass = MutableStateFlow(Classinfo(0,0,0,0,0))

    // 진짜 반 리스트
    private var _classInfoes = mutableListOf<Classinfo>()
    val classInfoes = _classInfoes

    // 반 id 리스트
//    private var _classes = MutableStateFlow(mutableListOf<Int>())
//    val classes = _classes

    // 반 리스트
    private var _classSurfaces = MutableStateFlow(mutableListOf<Int>())
    val classSurfaces = _classSurfaces

    // <!---------------------------- 전체리스트 ------------------------------->
    private var _books = MutableStateFlow(MutableList(25) { Book(bookName = "이거 어디까지 올라가는 거에요?")})
    val books = _books

    // <!---------------------------- 대출한 리스트 ------------------------------->
    private var _returns = MutableStateFlow(mutableListOf(Book(bookName = "find this"),Book(bookName = "what this")))
    val returns = _returns

    init {
        loadFirst()
    }

    // <!---------------------------- 공통 배치 함수 ------------------------------->
    // 관리하는 반 정보를 가장 먼저 가져와야함
    private fun loadFirst() {
        _classInfoes = ApplicationClass.mainClassInfo

        // 첫번째 반을 최초 반으로 설정
        loadCommon()
    }

    private fun loadRemote() {
        // 전체 도서 정보
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.libraryService.getBooks(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode)
                }
                if (response.isSuccessful) {
                    _books.value = response.body() ?: mutableListOf<Book>()
                } else {
                    Log.d(TAG, "도서 가져오기 네트워크 오류")
                }
            } catch (e: Exception) {
                Log.d(TAG, " 도서 가져오기 네트워크 오류")
            }
        }
        // 대출 도서 정보
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.libraryService.getDraws(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode)
                }
                if (response.isSuccessful) {
                    _returns.value = response.body() ?: mutableListOf<Book>()
                } else {
                    Log.d(TAG, "대출 도서 가져오기 네트워크 오류")
                }
            } catch (e: Exception) {
                Log.d(TAG, " 대출 도서 가져오기 네트워크 오류")
            }
        }
    }

    private fun loadCommon() {
        // classinfoes를 classes로 가공
        loadClasses()
        curClass.value = ApplicationClass.mainClassInfo[0]

        // 현재 반설정이 완료되면 collect 리스너를 달아준다.
        initCollect()
    }

    private fun initCollect() {
        CoroutineScope(Dispatchers.IO).launch {
            curClass.collect { newClass ->
                // GET해서 가져온 정보 업데이트 (반 도서 대출정보/ 반 도서 전체정보)
                // loadRemote()
            }
        }
        loadRemote()
    }

    private fun loadClasses() {
        _classSurfaces.value = _classInfoes.map{it.classCode}.toMutableList()
//        _classes.value = _classInfoes.map{it.classNum}.toMutableList()
    }

    // QR코드
    fun generateQRCode(title: String, author: String, publisher: String): Bitmap? {
        // Create a QR Code Writer
        val writer = MultiFormatWriter()

        // Set the barcode format
        val format = BarcodeFormat.QR_CODE

        // Set the barcode size
        val width = 500
        val height = 500

        // Set the barcode content
        val content = "$title $author $publisher"

        // Set the barcode hints
        val hints = Hashtable<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

        try {
            // Encode the barcode
            val bitMatrix = writer.encode(content, format, width, height, hints)

            // Create a Bitmap from the bit matrix
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (i in 0 until width) {
                for (j in 0 until height) {
                    bitmap.setPixel(i, j, if (bitMatrix[i, j]) Color.BLACK else Color.WHITE)
                }
            }
            // Return the Bitmap
            return bitmap
        } catch (e: WriterException) {
            Log.e(TAG, "Error generating QR Code", e)
            return null
        }
    }

    fun saveQRCode(bitmap: Bitmap?, path: String) {
        try {
            // Create a FileOutputStream to write the Bitmap to the specified path
            val outputStream = FileOutputStream(path)

            // Write the Bitmap to the FileOutputStream
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            // Close the FileOutputStream
            outputStream.close()
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "Error saving QR Code", e)
        } catch (e: IOException) {
            Log.e(TAG, "Error saving QR Code", e)
        }
    }
}