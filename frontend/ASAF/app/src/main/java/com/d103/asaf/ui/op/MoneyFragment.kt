
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentMoneyBinding

class MoneyFragment : BaseFragment<FragmentMoneyBinding>(FragmentMoneyBinding::bind, R.layout.fragment_money) {
    companion object {
        private const val MONEY = "money"
    }
}