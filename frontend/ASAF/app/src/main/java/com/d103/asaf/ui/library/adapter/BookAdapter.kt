package com.d103.asaf.ui.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.R
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.common.util.MyFirebaseMessagingService
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.ItemBookBinding
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.RemoteMessageCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// QR -> 책제목/작가/출판사
// String  -> BookDto로 변경 필요 -> 이 때 BookDto는 도서 DTO + 대출 DTO 정보를 가진 1개의 거대한 DTO로 만들 것임
class BookAdapter() : androidx.recyclerview.widget.ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {
    var isDraw = false
    private val fcmService = MyFirebaseMessagingService()
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
                if(isDraw) {
                    bookItemDrawer.text = book.borrower
                    bookItemReturn.text = dateToString(book.returnDate)
                } else {
                    bookItemDrawer.text = book.author
                    bookItemReturn.text = "${book.bookNameCount-book.trueBorrowStateCount} / ${book.bookNameCount}"
                }

                bookItemReturnSend.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.bookReturn))
                // SharedPreference user가 학생이라면 버튼 글자를 변경
                // 비콘 거리 안에 있을 때 버튼 visible 활성화
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
        val noti = Noti()
        noti.title = "도서 반납 요청"
        noti.content = "빌려간 도서의 반납일이 만료됐습니다.\n 반납 부탁드립니다."
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitUtil.notiService.pushMessage(listOf(noti))
        }
    }

    private fun sendReturn(book: Book) {
        // 반납하기
        // fragment를 이동 LibraryUserReturnFragment (반납)
        // RetrofitUtil.libraryService.postReturn(book)
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