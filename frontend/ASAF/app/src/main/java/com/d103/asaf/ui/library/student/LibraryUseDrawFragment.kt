package com.d103.asaf.ui.library.student

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentLibraryUseDrawBinding
import com.d103.asaf.ui.library.LibraryFragmentViewModel
import com.ssafy.template.util.SharedPreferencesUtil

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
            fragmentLibraryUserDrawDrawdayDropdown.dataList.addAll(viewModel.days.value)
            fragmentLibraryUserDrawDrawdayDropdown.dataList.removeAt(2)
            fragmentLibraryUserDrawDrawdayDropdown.dropdownText.text = "3"
            fragmentLibraryUserDrawDrawdayDropdown.dropdownTextPost.text = "일"
            // fragmentLibraryUserDrawTextviewDrawerText.text =
        }
    }
}