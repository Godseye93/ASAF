import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.futured.donut.DonutSection
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentMoneyBinding
import com.d103.asaf.ui.op.LockerFragment
import com.d103.asaf.ui.op.OpFragmentViewModel
import com.d103.asaf.ui.op.adapter.LockerAdapter
import com.d103.asaf.ui.op.adapter.MoneyAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoneyFragment :
    BaseFragment<FragmentMoneyBinding>(FragmentMoneyBinding::bind, R.layout.fragment_money) {
    companion object {
        private const val SIGN = "sign"

        fun instance(sign: MutableList<DocSign>): MoneyFragment {
            val fragment = MoneyFragment()
            val args = Bundle()
            args.putParcelableArrayList(SIGN, ArrayList(sign))
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: OpFragmentViewModel by viewModels()
    private lateinit var adapter: MoneyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.signs.collect { newList ->
                adapter.submitList(newList)
            }
        }

        adapter = MoneyAdapter()
        binding.fragmentMoneyRecyclerview.adapter = adapter
    }
}
