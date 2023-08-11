package com.d103.asaf.ui.library.adapter

import android.util.Log
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
class BookAdapter(private val navigationListener: NavigationListener?) : androidx.recyclerview.widget.ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {
    var isDraw = false
    var nearBy = false // 비콘 근처인지
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
                if(ApplicationClass.sharedPreferences.getString("authority") == "교육생") {
                    bookItemReturnSend.text = "반납"
                    bookItemReturnSend.isVisible = nearBy && isDatePassed(book.returnDate)

                    bookItemReturnSend.setOnClickListener {
                        sendReturn(book)
                    }
                } else {
                    // Log.d("반납일", "bind: ${book.returnDate}")
                    bookItemReturnSend.isVisible = isDatePassed(book.returnDate)
                    bookItemReturnSend.setOnClickListener {
                        // 알림을 해당 학생에게 보내기
                        sendNotification(binding, book)
                        // 색깔을 회색으로
                        bookItemReturnSend.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.isClicked))
                        // 버튼을 비활성화
                        bookItemReturnSend.isClickable = false
                    }
                }

                bookItemReturnSend.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.bookReturn))

                bookItemTitle.text = book.bookName
                bookItemTitle.isSelected = true
                if(isDraw) {
                    if(ApplicationClass.sharedPreferences.getString("authority") == "교육생") {
                        bookItemDrawer.text = dateToString(book.returnDate)
                        bookItemReturn.text = ""
                    } else {
                        bookItemDrawer.text = book.borrower
                        bookItemReturn.text = dateToString(book.returnDate)
                    }
                } else {
                    bookItemDrawer.text = book.author
                    bookItemReturn.text = "${book.bookNameCount-book.trueBorrowStateCount} / ${book.bookNameCount}"
                    bookItemReturnSend.isVisible = false
                }
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            // 식별자 요소를 비교하는게 맞다
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    // 애플리케이션 icon이 자동으로 삽입됨 -> 아이콘을 변경하자
    private fun sendNotification(binding: ItemBookBinding, curBook: Book) {
        // FCM 메시지 보내기 함수
        Toast.makeText(binding.root.context, "알림을 보냈습니다.", Toast.LENGTH_SHORT).show()
        // ----------------노티 정보를 좀 더 담아야함--------------
        val noti = Noti()
        noti.title = "도서 반납 요청"
        noti.content = "빌려간 도서의 반납일이 만료됐습니다.\n 반납 부탁드립니다."
        noti.notification = true
        noti.sender = ApplicationClass.sharedPreferences.getInt("id")
        noti.receiver = curBook.userId
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitUtil.notiService.pushMessage(listOf(noti))
        }
    }

    private fun sendReturn(book: Book) {
        // 반납하기
        // fragment를 이동 LibraryUserReturnFragment (반납)
        navigationListener?.navigateToDestination(book)
    }

    fun isDatePassed(sdate: Long): Boolean {
        val currentDate = Calendar.getInstance().time
        val date = Date(sdate)
        return date.before(currentDate)
    }

    private fun dateToString(date: Long): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return dateFormat.format(Date(date))
    }
}