package com.d103.asaf.ui.market

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d103.asaf.MainActivity
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.databinding.FragmentMarketBinding
import com.d103.asaf.ui.home.pro.UserInfoAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarketFragment : BaseFragment<FragmentMarketBinding>(FragmentMarketBinding::bind, R.layout.fragment_market) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: MarketAdpater
    private val testList = mutableListOf<Market>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()




    }

    fun init(){
        // 테스트 데이터
        test()

        // 어댑터 연결
        adapter =  MarketAdpater(requireContext())

        binding.fragmentMarketRecyclerview.adapter = adapter

        adapter.itemClickListener = object : MarketAdpater.ItemClickListener{
            override fun onClick(view: View, position: Int, data: Market) {
                Toast.makeText(requireContext(), "클릭함", Toast.LENGTH_SHORT).show()
            }


        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentMarketRecyclerview.layoutManager = layoutManager
        adapter.submitList(testList)


        // FAB 연결
        binding.fragmentMarketRegisterBtn.setOnClickListener {

            findNavController().navigate(R.id.action_marketFragment_to_marketFragmentRegister)
            (requireActivity() as MainActivity).hideBottomNavigationBarFromFragment()
        }

    }
    fun test(){
        val testImg1 = "https://images.velog.io/images/sdb016/post/47181c7c-1156-4182-a638-e0ad0b03a3d3/test.png"
        val test1 = Market(1, 1691131921, "LONG TEST TITLELONG TEST TITLELONG TEST TITLELONG TEST TITLE LONG TEST TITLE1", "TEST CONTENT", 1, testImg1, "TESTER 1 ")
        val testImg2 = "https://us.123rf.com/450wm/alexwhite/alexwhite1512/alexwhite151204975/49784187-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%95%84%EC%9D%B4%EC%BD%98.jpg"
        val test2 = Market(1, 1691132081, "TEST TITLE2", "TEST CONTENT", 2, testImg2, "TESTER 2 ")
        val testImg3 = "https://png.pngtree.com/png-vector/20190411/ourmid/pngtree-vector-business-men-icon-png-image_925963.jpg"
        val test3 = Market(1, 1690872720000, "TEST TITLE3", "TEST CONTENT", 3, testImg3, "TESTER 3 ")
        val testImg4= "https://cdn-icons-png.flaticon.com/512/3349/3349798.png"
        val test4 = Market(1, 1672555920000, "TEST TITLE4", "TEST CONTENT", 4, testImg4, "TESTER 4 ")
        val testImg5 = "https://cdn.icon-icons.com/icons2/1893/PNG/512/scientistavatar_120780.png"
        val test5 = Market(1, 1677999120, "TEST TITLE5", "TEST CONTENT", 5, testImg5, "TESTER 5 ")


        testList.add(test1)
        testList.add(test2)
        testList.add(test3)
        testList.add(test4)
        testList.add(test5)
        testList.add(test1)
        testList.add(test2)
        testList.add(test3)
        testList.add(test4)
        testList.add(test5)
        testList.add(test1)
        testList.add(test2)
        testList.add(test3)
        testList.add(test4)
        testList.add(test5)
        testList.add(test1)
        testList.add(test2)
        testList.add(test3)
        testList.add(test4)
        testList.add(test5)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MarketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MarketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}