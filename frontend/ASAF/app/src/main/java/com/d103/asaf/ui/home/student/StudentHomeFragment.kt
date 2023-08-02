package com.d103.asaf.ui.home.student

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.SharedViewModel
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentStudentHomeBinding

private const val TAG = "StudentHomeFragment_cjw ASAF"
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentHomeFragment  : BaseFragment<FragmentStudentHomeBinding>(FragmentStudentHomeBinding::bind, R.layout.fragment_student_home) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val stuHomeFragmentViewModel: StudentHomeFragmentViewModel by viewModels()

    // 애니메이션 부분
    private lateinit var cardView1: CardView
    private lateinit var cardView2: CardView
    private lateinit var buttonFlip: Button
    private var isFirstCardVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_student_home, container, false)

        cardView1 = rootView.findViewById(R.id.fragment_student_home_cardView_front)
        cardView2 = rootView.findViewById(R.id.fragment_student_home_cardView_back)
        buttonFlip = rootView.findViewById(R.id.buttonFlip)

        // Set initial visibility
        cardView1.visibility = View.VISIBLE
        cardView2.visibility = View.GONE

        // Set cameraDistance for the card views
        val scale = resources.displayMetrics.density
        cardView1.cameraDistance = 8000 * scale
        cardView2.cameraDistance = 8000 * scale

        buttonFlip.setOnClickListener {
            flipCards()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        binding.fragmentStuHomeImagebuttonUserinfo.setOnClickListener {
//            findNavController().navigate(R.id.navigation_setting)
//        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun flipCards() {
        val currentCard = if (isFirstCardVisible) cardView1 else cardView2
        val nextCard = if (isFirstCardVisible) cardView2 else cardView1

        val objectAnimatorFirst =
            ObjectAnimator.ofFloat(currentCard, View.ROTATION_Y, 0f, 90f)
        objectAnimatorFirst.duration = 500
        objectAnimatorFirst.start()

        objectAnimatorFirst.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                // Hide the current card after the first animation
                currentCard.visibility = View.INVISIBLE
                // Show the next card before the second animation
                nextCard.visibility = View.VISIBLE

                val objectAnimatorSecond =
                    ObjectAnimator.ofFloat(nextCard, View.ROTATION_Y, -90f, 0f)
                objectAnimatorSecond.duration = 500
                objectAnimatorSecond.start()

                isFirstCardVisible = !isFirstCardVisible
            }
        })
    }
}