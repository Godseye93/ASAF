package com.d103.asaf.ui.op

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.GridLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import com.d103.asaf.R
import com.d103.asaf.common.component.SeatView
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentSeatBinding

class SeatFragment() : BaseFragment<FragmentSeatBinding>(FragmentSeatBinding::bind, R.layout.fragment_seat) {
    private lateinit var targetView: SeatView
    private var startX = 0
    private var startY = 0
    private var offsetX = 0
    private var offsetY = 0
    private val num = 5
    private var targetViewIndex = 20 // 초기화 될 거라 의미 없음
    private var position: MutableList<Int> = mutableListOf() // 초기화 될 거라 의미 없음
    private var seat: MutableList<Int> = mutableListOf() // 초기화 될 거라 의미 없음
    private var reversePosition = (0..24).toMutableList()
    private lateinit var gridLayout: GridLayout
    private var seatNum = 0;
    companion object {
        private const val POSITION = "position"
        private const val SEAT = "seat"
        // Factory method to create an instance of SeatFragment with position list.
        fun instance(position: MutableList<Int>,seat: MutableList<Int>): SeatFragment {
            val fragment = SeatFragment()
            val args = Bundle()
            args.putIntegerArrayList(POSITION, ArrayList(position))
            args.putIntegerArrayList(SEAT, ArrayList(seat))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments로부터 position 리스트를 가져와서 변수에 할당합니다.
        position = requireArguments().getIntegerArrayList(POSITION) ?: mutableListOf()
        seat = requireArguments().getIntegerArrayList(SEAT) ?: mutableListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridLayout = binding.gridLayout
        targetView = binding.item1 // 아무 아이템이나 같은 크기이므로 넣어주면 됨 사이즈 계산에만 사용
        seatNum = seat.size

        binding.apply {
            seatAdd.setOnClickListener {
                seatNumberInput.visibility = View.VISIBLE
                seatAdd.visibility = View.INVISIBLE
                switchToEditText()
            }
            // position 정보를 seatNum 크기 만큼만 보내기 서버에서 n건을 수정해야함
            seatComplete.setOnClickListener {
                // position.subList(0,seatNum)
            }

            seatNumberInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val text = s.toString()
                    seatNum = try {
                        text.toInt()
                    } catch (e: NumberFormatException) {
                        0
                    }
                    if(seatNum >= 25) seatNum = 25
                }
            })
        }
        // ViewTreeObserver를 이용하여 targetView의 크기를 측정
        targetView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                targetView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setSeat()
                Log.d("포지션", "$position")
                Log.d("리버스포지션", "$reversePosition")
            }
        })
    }

    fun setSeat() {
        // targetView의 크기가 측정된 후에 다른 작업 수행
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.chair, null)
        for (i in 0 until gridLayout.childCount) {
            val childView = gridLayout.getChildAt(i)
            if (childView is SeatView) {
                childView.seatText.text = i.toString()
                setTouchListener(childView)
                if (i < seatNum) childView.seatImage.setImageDrawable(drawable)
                setViewPosition(childView, position[i])
                reversePosition[position[i]] = i
            }
        }
    }

    fun clearSeat() {
        reversePosition = (0..24).toMutableList()
        position = (0..24).toMutableList()
        seat = (0 until seatNum).toMutableList()
        setSeat()
    }

    private fun setViewPosition(curView:SeatView, newIndex: Int) {
        val columnWidth = targetView.width
        val rowHeight = targetView.height
        val columnIndex = newIndex % num
        val rowIndex = newIndex / num
        val newX = columnIndex * columnWidth
        val newY = rowIndex * rowHeight
        curView.x = newX.toFloat()
        curView.y = newY.toFloat()
    }

    private fun moveImageViewToPosition(cur: SeatView, newX: Int, newY: Int) {
        cur.animate()
            .x(newX.toFloat())
            .y(newY.toFloat())
            .setDuration(0)
            .start()
    }

    private fun calculateNewIndex(x: Int, y: Int): Int {
        val columnWidth = targetView.width
        val rowHeight = targetView.height
        val columnIndex = x / columnWidth
        val rowIndex = y / rowHeight
        Log.d("스왑계산", "계산 : ${rowIndex * num + columnIndex}")
        return rowIndex * num + columnIndex
    }

    private fun moveImageViewToGridPosition(cur: SeatView, newIndex: Int) {
        val columnWidth = targetView.width
        val rowHeight = targetView.height
        val columnIndex = newIndex % num
        val rowIndex = newIndex / num
        val newX = columnIndex * columnWidth
        val newY = rowIndex * rowHeight
        moveImageViewToPosition(cur, newX, newY)
    }

    private fun swapImageViewPosition(cur: SeatView, nextIndex: Int) {
        val parentView = cur.parent as ViewGroup
        val curIndex = targetViewIndex // 15 현재위치
        val nxtIndex = nextIndex // 0 나중위치

        val org = parentView.getChildAt(reversePosition[curIndex]) as SeatView // 0번 이미지뷰
        val next = parentView.getChildAt(reversePosition[nxtIndex]) as SeatView // 15번 이미지뷰

        moveImageViewToGridPosition(org, nxtIndex)
        moveImageViewToGridPosition(next, curIndex)
        // 위치정보 변경
        reversePosition.swap(curIndex, nextIndex)
    }

    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(target: SeatView) {
        target.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = target.x.toInt()
                    startY = target.y.toInt()
                    offsetX = event.rawX.toInt() - startX
                    offsetY = event.rawY.toInt() - startY
                    targetViewIndex = calculateNewIndex(startX + target.width / 2, startY + target.height / 2)
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX.toInt() - offsetX
                    val newY = event.rawY.toInt() - offsetY

                    // 바운더리 제한
                    val maxX = (target.parent as View).width - target.width / 2
                    val maxY = (target.parent as View).height - target.height / 2

                    // 새로운 위치를 바운더리 내에 유지하도록 조정
                    // -1을 안하면 합이 바운더리를 넘어가서 인덱스가 계산됨 (몫이 1이되는 걸 방지)
                    val constrainedX = newX.coerceIn(-(target.width / 2 - 1), maxX - 1)
                    val constrainedY = newY.coerceIn(-(target.height / 2 - 1), maxY - 1)

                    moveImageViewToPosition(target, constrainedX, constrainedY)

                    // Log.d("이동", "${newX}, ${newY}, ${maxX}, ${maxY} " )
                }
                // 시작위치가 좌측상단이므로 중간좌표에서 시작한 것처럼 width/2 -5 , height/2 -5 보정
                MotionEvent.ACTION_UP -> {
                    val newIndex = calculateNewIndex(target.x.toInt() + target.width / 2 - 5
                        , target.y.toInt() + target.height / 2 - 5)
                    Log.d("스왑전", "$targetViewIndex, $newIndex")
                    swapImageViewPosition(target, newIndex)
                }
            }
            true
        }
    }

    private fun switchToEditText() {
        binding.apply {
            seatAdd.visibility = View.INVISIBLE
            seatNumberInput.visibility = View.VISIBLE
            seatNumberInput.requestFocus()
            seatNumberInput.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    clearSeat()
                    setSeat()
                }
                false
            }

            seatNumberInput.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                    clearSeat()
                    setSeat()
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.seatNumberInput.windowToken, 0)
        binding.seatAdd.visibility = View.VISIBLE
        binding.seatNumberInput.visibility = View.INVISIBLE
    }

}