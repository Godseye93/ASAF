package com.d103.asaf.ui.library.student

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentLibraryUseReturnBinding
import com.d103.asaf.ui.library.QRCodeScannerDialog
import com.d103.asaf.ui.op.OpFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryUseReturnFragment : BaseFragment<FragmentLibraryUseReturnBinding>(
    FragmentLibraryUseReturnBinding::bind, R.layout.fragment_library_use_return) {
    companion object {
        private const val RETURN = "return"

        fun instance(returnInfo: Book): LibraryUseReturnFragment {
            val fragment = LibraryUseReturnFragment()
            val args = Bundle()
            args.putParcelable(RETURN, returnInfo)
            fragment.arguments = args
            return fragment
        }
    }

    private var viewModel:LibraryUseFragmentViewModel = LibraryUseFragment.parentViewModel!!
    private var returnInfo:Book = Book()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments로부터 리스트를 가져와서 변수에 할당합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            returnInfo = requireArguments().getParcelable(RETURN,Book::class.java) ?: Book()
        } else {
            returnInfo = requireArguments().getParcelable(RETURN) ?: Book()
        }
        returnInfo.borrowState = false
        Log.d("도서정보", "onCreate: $returnInfo")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            materialButtonNo.setOnClickListener {
                findNavController().navigate(R.id.action_libraryUseReturnFragment_to_libraryUseFragment)
            }
            materialButtonYes.setOnClickListener {
                returnInfo.borrowState = false
                lifecycleScope.launch{
                    returnBook(returnInfo.id, returnInfo)
                }
                findNavController().navigate(R.id.action_libraryUseReturnFragment_to_libraryUseFragment)
            }
            fragmentLibraryUserReturnTextviewTitle.text = returnInfo.bookName
            fragmentLibraryUserTextviewAuthor.text = returnInfo.author

        }
    }

    private suspend fun returnBook(bookId: Int, returnInfo: Book) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitUtil.libraryService.updateDrawBook(bookId, returnInfo)
            }
            if (response.isSuccessful) {
                if(response.body() != null) {
                    Toast.makeText(requireContext(), "반납이 완료 됐습니다.", Toast.LENGTH_SHORT).show()
                    viewModel.loadRemote()
                }
                else {
                    Toast.makeText(requireContext(), "이미 반납된 도서입니다.", Toast.LENGTH_SHORT).show()
                }
                requireActivity().onBackPressedDispatcher.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "이미 반납된 도서입니다.", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
                Log.d("학생도서", "도서 반납 네트워크 오류")
            }
        } catch (e: Exception) {
            Log.e("학생도서", "도서 반납 오류: ${e.message}", e)
        }
    }
}