package com.d103.asaf.ui.library.pro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentLibraryManagementBinding
import com.d103.asaf.ui.library.LibraryFragmentViewModel
import com.d103.asaf.ui.library.adapter.BookAdapter
import com.d103.asaf.ui.op.OpFragmentViewModel
import com.d103.asaf.ui.op.adapter.LockerAdapter

// String -> BookDto로 변경 필요
class LibraryManagementFragment : BaseFragment<FragmentLibraryManagementBinding>(FragmentLibraryManagementBinding::bind, R.layout.fragment_library_management) {
    private val viewModel: LibraryFragmentViewModel by viewModels()
    private var books: MutableList<String> = mutableListOf()
    private lateinit var adapter: BookAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initClass()
        initView()
    }

    private fun initList() {
        books = viewModel.myBooks.value
        adapter = BookAdapter()
        binding.fragmentLibraryRecyclerview.adapter = adapter
        adapter.submitList(books)
    }

    private fun initView() {
        binding.apply {
            bookToggleButton.seatText.text = "대출 현황"
            bookToggleButton.lockerText.text = "전체 도서 목록"
            bookToggleButton.moneyText.visibility = View.GONE

            bookToggleButton.setFirstButtonClickListener {
                books = viewModel.myBooks.value
                adapter.submitList(books)
            }

            bookToggleButton.setSecondButtonClickListener {
                books = viewModel.books.value
                adapter.submitList(books)
            }

            fragmentLibrarySearchBar.searchEditText.addTextChangedListener(searchWatcher)

            fragmentLibraryRecyclerview.isVisible = true
        }
    }

    private fun initClass() {
        binding.apply {
            fragmentLibraryDropdownClass.dropdownText.addTextChangedListener(classWatcher)
            fragmentLibraryDropdownClass.dropdownText.text = viewModel.classes.value[0].toString()
            fragmentLibraryDropdownClass.dropdownTextPost.text = "반"
            // 객체가 바뀌면 안됨.. 요소를 변경해줘야 변화 인식됨
            fragmentLibraryDropdownClass.dataList.addAll(viewModel.classes.value)
            fragmentLibraryDropdownClass.dataList.removeAt(0)
        }
    }

    private val classWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // 텍스트가 변경되기 전에 호출됩니다.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            // 텍스트가 변경된 후에 호출됩니다.
            viewModel.curClass.value = s.toString().toInt()
        }
    }

    private val searchWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // 텍스트가 변경되기 전에 호출됩니다.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            bookSearch(s.toString())
        }

        override fun afterTextChanged(s: Editable?)

        }
    }

    private fun bookSearch(title: String) {
        val filteredBooks = books.filter { book -> book.contains(title) }
        adapter.submitList(filteredBooks)
    }
}