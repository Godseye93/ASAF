package com.d103.asaf.ui.library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import com.d103.asaf.common.model.dto.Book
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Hashtable

private val TAG = "LibraryFragmentViewModel"
class LibraryFragmentViewModel: ViewModel() {
    // <!---------------------------- 공통변수 ------------------------------->
    var curClass = MutableStateFlow(0)

    // 반 리스트
    private val _classes = MutableStateFlow<List<Int>>(listOf(2, 3, 4))
    val classes = _classes

    // <!---------------------------- 전체리스트 ------------------------------->
    private val _books = MutableStateFlow(MutableList(25) { Book(bookName = "이거 어디까지 올라가는 거에요?")})
    val books = _books

    // <!---------------------------- 대출한 리스트 ------------------------------->
    private val _myBooks = MutableStateFlow(mutableListOf(Book(bookName = "find this"),Book(bookName = "what this")))
    val myBooks = _myBooks

    private fun loadCommon() {
        curClass.value = _classes.value[0]
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