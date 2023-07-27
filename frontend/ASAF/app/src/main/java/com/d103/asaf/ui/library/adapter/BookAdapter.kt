package com.d103.asaf.ui.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.R
import com.d103.asaf.databinding.ItemBookBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// QR -> 책제목/작가/출판사
// String  -> BookDto로 변경 필요 -> 이 때 BookDto는 도서 DTO + 대출 DTO 정보를 가진 1개의 거대한 DTO로 만들 것임
class BookAdapter : androidx.recyclerview.widget.ListAdapter<String, BookAdapter.BookViewHolder>(BookDiffCallback()) {
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
        fun bind(book: String) {
            binding.apply {
                bookItemTitle.text = book
                bookItemTitle.isSelected = true
                bookItemDrawer.text = "괴도 키드"
                bookItemReturn.text = "2023/07/26"
                bookItemReturnSend.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.bookReturn))
                bookItemReturnSend.setOnClickListener {
                    // 알림을 해당 학생에게 보내기
                    sendNotification(binding)
                    // 색깔을 회색으로
                    bookItemReturnSend.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.isClicked))
                    // 버튼을 비활성화
                    bookItemReturnSend.isClickable = false
                }
                bookItemReturnSend.isVisible = isDatePassed(bookItemReturn.text.toString())
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            // 식별자 요소를 비교하는게 맞다
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    // 애플리케이션 icon이 자동으로 삽입됨 -> 아이콘을 변경하자
    private fun sendNotification(binding: ItemBookBinding) {
        Toast.makeText(binding.root.context, "알림을 보냈습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun isDatePassed(sdate: String): Boolean {
        if(sdate.equals("")) return false
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        val currentDate = Calendar.getInstance().time
        val date = dateFormat.parse(sdate)
        return date.before(currentDate) ?: false
    }
}