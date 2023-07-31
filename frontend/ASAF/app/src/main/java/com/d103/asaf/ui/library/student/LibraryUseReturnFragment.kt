package com.d103.asaf.ui.library.student

import android.os.Bundle
import android.view.View
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentLibraryUseReturnBinding

class LibraryUseReturnFragment : BaseFragment<FragmentLibraryUseReturnBinding>(
    FragmentLibraryUseReturnBinding::bind, R.layout.fragment_library_use_return) {
    companion object {
        private const val RETURN = "return"

        fun instance(drawInfo: List<String>): LibraryUseDrawFragment {
            val fragment = LibraryUseDrawFragment()
            val args = Bundle()
            args.putStringArrayList(RETURN, ArrayList(drawInfo))
            fragment.arguments = args
            return fragment
        }
    }

    private var returnInfo: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments로부터 리스트를 가져와서 변수에 할당합니다.
        returnInfo = requireArguments().getStringArrayList(RETURN) ?: mutableListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}