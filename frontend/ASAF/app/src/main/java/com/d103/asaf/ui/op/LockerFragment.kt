package com.d103.asaf.ui.op

import android.os.Bundle
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentLockerBinding

class LockerFragment : BaseFragment<FragmentLockerBinding>(FragmentLockerBinding::bind, R.layout.fragment_locker) {
    companion object {
        private const val LOCKER = "locker"

        // Factory method to create an instance of SeatFragment with position list.
        fun instance(locker: MutableList<String>): LockerFragment {
            val fragment = LockerFragment()
            val args = Bundle()
            args.putStringArrayList(LOCKER, ArrayList(locker))
            fragment.arguments = args
            return fragment
        }
    }
}