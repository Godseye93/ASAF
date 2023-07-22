package com.d103.asaf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class DropdownAdapter(private val dropdownList: MutableList<String>,
                      private val currentText: TextView, private val recycler: RecyclerView) : RecyclerView.Adapter<DropdownAdapter.ViewHolder>() {

    // 뷰 홀더 클래스
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.dropdown_textview_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dropdown_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dropdownList[position]

        holder.textView.setOnClickListener {
            recycler.isVisible = false
            dropdownList.add(currentText.text.toString())
            // when textView is Clicked then dropdown_textview_text is changed textView.text
            currentText.text = holder.textView.text

            dropdownList.remove(currentText.text.toString())
        }
    }

    override fun getItemCount(): Int {
        return dropdownList.size
    }
}
