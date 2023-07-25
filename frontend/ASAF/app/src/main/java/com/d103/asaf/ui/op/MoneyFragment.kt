
import android.os.Bundle
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentMoneyBinding
import com.d103.asaf.ui.op.LockerFragment

class MoneyFragment : BaseFragment<FragmentMoneyBinding>(FragmentMoneyBinding::bind, R.layout.fragment_money) {
    companion object {
        private const val MONEY = "money"
    }
}