package com.d103.asaf.ui.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.databinding.ItemBookBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// String  -> BookDto로 변경 필요
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
                bookItemReturn.text = getDate(3)
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

    private fun getDate(loanPeriod: Int): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, loanPeriod)
        return dateFormat.format(currentDate.time)
    }
}