package com.d103.asaf.ui.library.pro

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.DialogAddBookBinding
import com.d103.asaf.databinding.FragmentLibraryManagementBinding
import com.d103.asaf.ui.library.LibraryFragmentViewModel
import com.d103.asaf.ui.library.adapter.BookAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
private const val PATH = "/data/data/com.d103.asaf/"
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
                fragmentLibraryTextviewSecond.text = "대출자"
                fragmentLibraryTextviewThird.text = "반납일"
                books = viewModel.myBooks.value
                adapter.submitList(books)
            }

            bookToggleButton.setSecondButtonClickListener {
                fragmentLibraryTextviewSecond.text = "저자"
                fragmentLibraryTextviewThird.text = "수량"
                books = viewModel.books.value
                adapter.submitList(books)
            }

            fragmentLibrarySearchBar.setSearchClickListener {
                fragmentLibrarySearchBar.searchEditText.text.clear()
            }

            fragmentLibrarySearchBar.searchEditText.addTextChangedListener(searchWatcher)

            fragmentLibraryRecyclerview.isVisible = true

            fragmentLibraryFabAddbook.setOnClickListener {
                addBookDialog()
            }
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

        override fun afterTextChanged(s: Editable?){

        }
    }

    private fun bookSearch(title: String) {
        val filteredBooks = books.filter { book -> book.contains(title) }
        adapter.submitList(filteredBooks)
    }

    private fun setDate(loanPeriod: Int): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, loanPeriod)
        return dateFormat.format(currentDate.time)
    }


    fun addBookDialog() {
        val dialogView = DialogAddBookBinding.inflate(layoutInflater,binding.root,false)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView.root)
            .create()

        // 도서 등록 기능 POST
        dialogView.bookAdd.setOnClickListener {
            // 입력받은 정보 DTO로 조립 & POST & dismiss() & createQR % send email
            // val bookDto = ~
            CoroutineScope(Dispatchers.IO).launch {
                val qr = viewModel.generateQRCode("이거 어디까지 올라가는거에요?","괴도 키드","싸피 출판")
                val qrImg = PATH +"${getFileName()}.png"
                viewModel.saveQRCode(qr, qrImg)
                sendEmail(qrImg)
                dialog.dismiss()
            }
        }
        dialogView.bookCancel.setOnClickListener {
            dialog.dismiss()
        }
        // 다이얼로그 키보드 띄워지면 resizing 하는 코드
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Set the dialog's window to not fit system windows
                dialog.window?.setDecorFitsSystemWindows(false)

                // Set the dialog's window to listen for window insets
                ViewCompat.setOnApplyWindowInsetsListener(dialogView.root) { v, insets ->
                    // Get the bottom inset of the system bar
                    val bottomInset = insets.getInsets(WindowInsets.Type.ime()).bottom

                    // Set the dialog's padding to the bottom inset
                    v.setPadding(0, 0, 0, bottomInset)

                    insets
                }
            } else {
                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun getFileName(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        return sdf.format(Date())
    }

    private fun sendEmail(path: String) {
        MaildroidX.Builder()
            .smtp("live.smtp.mailtrap.io")
            .smtpUsername("api")
            .smtpPassword("0647ceab68282d673bdd53a351635833")
            .port("2525")
            .type(MaildroidXType.HTML)
            .to("kieanupark@gmail.com")
            .from("mailtrap@asaf.live")
            .subject("hello")
            .body("body")
            .attachment(path)
            .isStartTLSEnabled(true)
            .mail()

        Log.d("메일", "sendEmail: 보냄")
    }

}