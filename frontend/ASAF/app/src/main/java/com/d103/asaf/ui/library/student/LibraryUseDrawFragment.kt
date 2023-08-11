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

        binding.apply {
            fragmentLibraryUserDrawTextviewTitle.text = drawInfo[0]
            fragmentLibraryUserDrawTextviewAuthor.text = drawInfo[1]
            fragmentLibraryUserDrawTextviewIdText.text = ApplicationClass.sharedPreferences.getInt("student_number").toString()
            fragmentLibraryUserDrawTextviewDrawdateText.text = today.substring(2,10)
            fragmentLibraryUserDrawDrawdayDropdown.dataList.addAll(viewModel.days.value)
            fragmentLibraryUserDrawDrawdayDropdown.dataList.removeAt(2)
            fragmentLibraryUserDrawDrawdayDropdown.dropdownText.text = "3"
            fragmentLibraryUserDrawDrawdayDropdown.dropdownTextPost.text = "일"
            fragmentLibraryUserDrawTextviewDrawerText.text = ApplicationClass.sharedPreferences.getString("memberName")
            // put 버튼
            bookDrawBtn.setOnClickListener{
                // book 정보를 body로 줄필요가 없다! id만 주면 id에 해당하는 책의 상태만 확인해서
                // 대출여부를 true로 바꾸면 된다.
                // 또한 이미 true(대출 중인) 상태라면 boolean으로 false를 반환하면 된다.
                lifecycleScope.launch {
                    //val userId = ApplicationClass.sharedPreferences.getInt("id")
                    updateBook(drawInfo[3].toInt())
                }
            }
        }
    }

    private fun todayToString(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        return formatter.format(calendar.time)
    }

    private suspend fun updateBook(bookId: Int) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitUtil.libraryService.updateBook(bookId)
            }
            if (response.isSuccessful) {
                if(response.body() == false) {
                    Toast.makeText(requireContext(), "이미 대출 중인 도서입니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(requireContext(), "대출이 완료 됐습니다.", Toast.LENGTH_SHORT).show()
                    val dialogFragment = parentFragmentManager.findFragmentById(R.id.dialog_framelayout) as QRCodeScannerDialog
                    dialogFragment.dismissDialog()
                }
            } else {
                Log.d("학생도서", "도서 대출 네트워크 오류")
            }
        } catch (e: Exception) {
            Log.e("학생도서", "도서 대출 오류: ${e.message}", e)
        }
    }
}