package com.d103.asaf.ui.library.student

import androidx.fragment.app.viewModels
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentLibraryUseReturnBinding

class LibraryUseReturnFragment : BaseFragment<FragmentLibraryUseReturnBinding>(FragmentLibraryUseReturnBinding::bind, R.layout.fragment_library_use_return) {
    private val viewModel: LibraryUseFragmentViewModel by viewModels()
}