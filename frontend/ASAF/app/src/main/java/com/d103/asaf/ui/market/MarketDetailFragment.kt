package com.d103.asaf.ui.market

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.MarketImage
import com.d103.asaf.databinding.FragmentMarketDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MarketDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MarketDetailFragment : BaseFragment<FragmentMarketDetailBinding>(FragmentMarketDetailBinding::bind, R.layout.fragment_market_detail) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val viewModel : MarketDetailFragmentViweModel by viewModels()
    private lateinit var adapter : MarketPhotoRegisterAdapter


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

    // 날짜 형식 변환

    fun convertLongToDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

    fun init(){

        //adpater 설정

        if(viewModel.marketImageList.value.isNullOrEmpty()){
            viewModel.setNullImageList()
        }
        else{
            adapter = MarketPhotoRegisterAdapter(viewModel.marketImageList.value!!, requireContext())
            binding.fragmentMarketDetailRecyclerview.adapter = adapter
        }





        if(viewModel.marketDetail.value?.userId == ApplicationClass.sharedPreferences.getInt("id")) {
            binding.fragmentMarketDetailButtonLayout.visibility = View.VISIBLE
        }
        // 수정 버튼 클릭 시
        binding.fragmentMarketDetailUpdateButton.setOnClickListener {

        }
        // 삭제 버튼 클릭 시
        binding.fragmentMarketDetailDeleteButton.setOnClickListener {

        }
        viewModel.marketDetail.observe(viewLifecycleOwner){
            binding.fragmentMarketDetailContent.text = it.content
            binding.fragmentMarketDetailRegisterTime.text = convertLongToDate(it.registerTime)
            binding.fragmentMarketDetailTitleView.text = it.title
            Glide
                .with(requireContext())
                .load(it.profileImage)
                .into(binding.fragmentMarketDetailProfile)
            binding.fragmentMarketDetailUserName.text = it.userName

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MarketDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MarketDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}