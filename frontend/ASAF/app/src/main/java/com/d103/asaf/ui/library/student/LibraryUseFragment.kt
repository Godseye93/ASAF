package com.d103.asaf.ui.library.student

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.databinding.FragmentLibraryUseBinding
import com.d103.asaf.ui.library.QRCodeScannerDialog
import com.d103.asaf.ui.library.adapter.BookAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class LibraryUseFragment : BaseFragment<FragmentLibraryUseBinding>(FragmentLibraryUseBinding::bind, R.layout.fragment_library_use) {
    private val viewModel: LibraryUseFragmentViewModel by viewModels()
    private var books: MutableList<Book> = mutableListOf()
    private lateinit var adapter: BookAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initView()
    }

    private fun initList() {
        lifecycleScope.launch {
            viewModel.myDraws.collect {
                books = viewModel.myDraws.value
            }
        }
        adapter = BookAdapter()
        binding.fragmentLibraryUserRecyclerview.adapter = adapter
        adapter.submitList(books)
    }

    private fun initView() {
        binding.apply {
            bookUserToggleButton.seatText.text = "대출 현황"
            bookUserToggleButton.lockerText.text = "전체 도서 목록"
            bookUserToggleButton.moneyText.visibility = View.GONE

            bookUserToggleButton.setFirstButtonClickListener {
                fragmentLibraryUserTextviewSecond.text = "반납일"
                fragmentLibraryUserTextviewThird.text = "반납하기"
                books = viewModel.myDraws.value
                adapter.submitList(books)
            }

            bookUserToggleButton.setSecondButtonClickListener {
                fragmentLibraryUserTextviewSecond.text = "저자"
                fragmentLibraryUserTextviewThird.text = "수량"
                books = viewModel.books.value
                adapter.submitList(books)
            }

            fragmentLibraryUserSearchBar.setSearchClickListener {
                fragmentLibraryUserSearchBar.searchEditText.text.clear()
            }

            fragmentLibraryUserSearchBar.searchEditText.addTextChangedListener(searchWatcher)

            fragmentLibraryUserRecyclerview.isVisible = true

            fragmentLibraryUserFabDrawbook.setOnClickListener {
                // 카메라 찍는 fragment로 이동
                showQRCodeScannerDialog()
            }
        }
    }

    private val searchWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // 텍스트가 변경되기 전에 호출됩니다.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            bookSearch(s.toString())
        }

        override fun afterTextChanged(s: Editable?){

        }
    }

    private fun bookSearch(title: String) {
        val filteredBooks = books.filter { book -> book.bookName.contains(title) }
        adapter.submitList(filteredBooks)
    }

    // DB에 저장할 때 Date 타입
    private fun getDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 3)
        return calendar.time
    }

    // 텍스트에 사용할 때 String 타입
    private fun setDate(loanPeriod: Int): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, loanPeriod)
        return dateFormat.format(currentDate.time)
    }

    // 바코드 스캐너 호출
    private fun showQRCodeScannerDialog() {
        val dialogFragment = QRCodeScannerDialog.newInstance()
        dialogFragment.show(childFragmentManager, "QRCodeScannerDialog")
    }


}