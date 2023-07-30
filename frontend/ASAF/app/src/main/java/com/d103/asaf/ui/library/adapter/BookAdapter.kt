package com.d103.asaf.ui.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.R
import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.databinding.ItemBookBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// QR -> 책제목/작가/출판사
// String  -> BookDto로 변경 필요 -> 이 때 BookDto는 도서 DTO + 대출 DTO 정보를 가진 1개의 거대한 DTO로 만들 것임
class BookAdapter : androidx.recyclerview.widget.ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookBinding.inflate(inflater, parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

    inner class BookViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.apply {
                // SharedPreference user가 학생이라면 버튼 글자를 변경
                // bookItemReturnSend.text = "반납"
                bookItemTitle.text = book.bookName
                bookItemTitle.isSelected = true
                bookItemDrawer.text = book.borrower
                bookItemReturn.text = dateToString(book.returnDate)
                bookItemReturnSend.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.bookReturn))
                // SharedPreference user가 학생이라면 버튼 글자를 변경
                // bookItemReturnSend.setOnclickListenr 반납으로 변경한다. 
                // bookItemReturnSend.setOnClickListener{sendReturn(book) // 반납요청후 요청성공하면 해당 항목 리스트에서 지우기 기능 추가 필요}
                bookItemReturnSend.setOnClickListener {
                    // 알림을 해당 학생에게 보내기
                    sendNotification(binding)
                    // 색깔을 회색으로
                    bookItemReturnSend.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.isClicked))
                    // 버튼을 비활성화
                    bookItemReturnSend.isClickable = false
                }
                bookItemReturnSend.isVisible = isDatePassed(book.returnDate)
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            // 식별자 요소를 비교하는게 맞다
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    // 애플리케이션 icon이 자동으로 삽입됨 -> 아이콘을 변경하자
    private fun sendNotification(binding: ItemBookBinding) {
        // FCM 메시지 보내기 함수
        Toast.makeText(binding.root.context, "알림을 보냈습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun sendReturn(book: Book) {
        // 반납요청하기
        // fragment를 이동 (반납)
        // RetrofitUtil.libraryService.postReturn(book)
    }

    private fun sendDraw() {
        // dialog에서 카메라를 띄워서 qr 정보가 읽히면 frgment 이동
        // fragment를 이동 (대출)
    }

    private fun isDatePassed(sdate: Date): Boolean {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        val currentDate = Calendar.getInstance().time
        val date = dateFormat.parse(dateToString(sdate))
        return date?.before(currentDate) ?: false
    }

    private fun dateToString(date: Date): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        return formatter.format(date)
    }
}