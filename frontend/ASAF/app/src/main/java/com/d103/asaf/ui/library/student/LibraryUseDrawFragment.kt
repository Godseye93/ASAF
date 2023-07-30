package com.d103.asaf.ui.library.student

import android.os.Bundle
import android.view.View
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentLibraryUseDrawBinding
import com.d103.asaf.ui.op.LockerFragment

class LibraryUseDrawFragment : BaseFragment<FragmentLibraryUseDrawBinding>(FragmentLibraryUseDrawBinding::bind, R.layout.fragment_library_use_draw) {
    companion object {
        private const val DRAW = "draw"

        // Int를 LockerDto로 변경 필요
        fun instance(drawInfo: List<String>): LockerFragment {
            val fragment = LockerFragment()
            val args = Bundle()
            args.putStringArrayList(DRAW, ArrayList(drawInfo))
            fragment.arguments = args
            return fragment
        }
    }

    private var drawInfo: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments로부터 리스트를 가져와서 변수에 할당합니다.
        drawInfo = requireArguments().getStringArrayList(DRAW) ?: mutableListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}