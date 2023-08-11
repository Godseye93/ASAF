package com.d103.asaf.ui.library.student

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d103.asaf.R
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentLibraryUseDrawBinding
import com.d103.asaf.ui.library.LibraryFragmentViewModel
import com.d103.asaf.ui.library.QRCodeScannerDialog
import com.ssafy.template.util.SharedPreferencesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LibraryUseDrawFragment : BaseFragment<FragmentLibraryUseDrawBinding>(FragmentLibraryUseDrawBinding::bind, R.layout.fragment_library_use_draw) {
    companion object {
        private const val DRAW = "draw"

        fun instance(drawInfo: List<String>): LibraryUseDrawFragment {
            val fragment = LibraryUseDrawFragment()
            val args = Bundle()
            args.putStringArrayList(DRAW, ArrayList(drawInfo))
            fragment.arguments = args
            return fragment
        }
    }

    private var drawInfo: MutableList<String> = mutableListOf()
    private val viewModel: LibraryUseFragmentViewModel by viewModels()
    private val today = todayToString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments로부터 리스트를 가져와서 변수에 할당합니다.
        drawInfo = requireArguments().getStringArrayList(DRAW) ?: mutableListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("도서빌리기인포", "onViewCreated: $drawInfo")
        binding.apply {
            fragmentLibraryUserDrawTextviewTitle.text = drawInfo[0]
            if(drawInfo.size > 1) fragmentLibraryUserDrawTextviewAuthor.text = drawInfo[1]
            fragmentLibraryUserDrawTextviewIdText.text = ApplicationClass.sharedPreferences.getInt("student_number").toString()
            fragmentLibraryUserDrawTextviewDrawdateText.text = today.substring(2,10)
            fragmentLibraryUserDrawDrawdayDropdown.dataList.addAll(viewModel.days.value)
            fragmentLibraryUserDrawDrawdayDropdown.dataList.removeAt(2)
            fragmentLibraryUserDrawDrawdayDropdown.dropdownText.text = "3"
            fragmentLibraryUserDrawDrawdayDropdown.dropdownTextPost.text = "일"
            fragmentLibraryUserDrawTextviewDrawerText.text = ApplicationClass.sharedPreferences.getString("memberName")
            // put 버튼
            bookDrawBtn.setOnClickListener{
                val addDay = fragmentLibraryUserDrawDrawdayDropdown.dropdownText.text.toString().toInt()
                val userId = ApplicationClass.sharedPreferences.getInt("id")
                val userName = ApplicationClass.sharedPreferences.getString("memberName")
                val bookId = drawInfo[3].toInt()
                lifecycleScope.launch {

                    val book = Book(id = bookId, borrowDate = addDaysToCurrentDate(0), returnDate = addDaysToCurrentDate(addDay),
                                    borrowState = true, borrower = userName, userId = userId)
                    updateDrawBook(bookId, book)
                }
            }
        }
    }

    private fun todayToString(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        return formatter.format(calendar.time)
    }

    private fun addDaysToCurrentDate(day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, day)
        return calendar.timeInMillis
    }

    private suspend fun updateDrawBook(bookId: Int, book: Book) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitUtil.libraryService.updateDrawBook(bookId,book)
            }
            if (response.isSuccessful) {
                if(response.body() != null) {
                    Toast.makeText(requireContext(), "대출이 완료 됐습니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(requireContext(), "이미 대출 중인 도서입니다.", Toast.LENGTH_SHORT).show()
                }
                requireActivity().onBackPressedDispatcher.onBackPressed()
//                val dialogFragment = parentFragmentManager.findFragmentById(R.id.dialog_framelayout) as QRCodeScannerDialog
//                dialogFragment.dismissDialog()
            } else {
                Toast.makeText(requireContext(), "이미 대출 중인 도서입니다.", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
                Log.d("학생도서", "도서 대출 네트워크 오류 ${response.body()}")
            }
        } catch (e: Exception) {
            Log.e("학생도서", "도서 대출 오류: ${e.message}", e)
        }
    }
}