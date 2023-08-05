package com.d103.asaf.common.component.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.d103.asaf.R
import com.d103.asaf.common.component.Dropdown

class DropdownAdapter(private val dropdownList: MutableList<Int>,
                      private val dropdown:Dropdown) : RecyclerView.Adapter<DropdownAdapter.ViewHolder>() {

    // 뷰 홀더 클래스
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.dropdown_textview_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dropdown, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dropdownList[position].toString()

        holder.textView.setOnClickListener {
            dropdown.isClicked = !dropdown.isClicked
            dropdown.dropdownBtn.setImageResource(R.drawable.dropdown_arrow)
            dropdown.dropdownList.isVisible = false
            dropdownList.add(dropdown.dropdownText.text.toString().toInt())
            // when textView is Clicked then dropdown_textview_text is changed textView.text
            dropdown.dropdownText.text = holder.textView.text

            dropdownList.remove(dropdown.dropdownText.text.toString().toInt())
        }
    }
    override fun getItemCount(): Int {
        return dropdownList.size
    }
}
