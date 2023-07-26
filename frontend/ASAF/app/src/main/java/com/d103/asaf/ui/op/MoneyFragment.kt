import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentMoneyBinding
import com.d103.asaf.ui.op.LockerFragment
import com.d103.asaf.ui.op.OpFragmentViewModel
import com.d103.asaf.ui.op.adapter.LockerAdapter
import com.d103.asaf.ui.op.adapter.MoneyAdapter

class MoneyFragment :
    BaseFragment<FragmentMoneyBinding>(FragmentMoneyBinding::bind, R.layout.fragment_money) {
    companion object {
        private const val MONEY = "money"

        fun instance(money: MutableList<Int>): MoneyFragment {
            val fragment = MoneyFragment()
            val args = Bundle()
            args.putIntegerArrayList(MoneyFragment.MONEY, ArrayList(money))
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: OpFragmentViewModel by viewModels()
    private lateinit var adapter: MoneyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MoneyAdapter()
        binding.fragmentMoneyRecyclerview.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.moneys.collect { newList ->
                adapter.submitList(newList)
            }
        }
    }
}
