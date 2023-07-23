package com.d103.asaf.ui.op

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.databinding.FragmentSeatBinding

class SeatFragment : BaseFragment<FragmentSeatBinding>(FragmentSeatBinding::bind, R.layout.fragment_seat) {
    private lateinit var targetImageView: ImageView
    private var startX = 0
    private var startY = 0
    private var offsetX = 0
    private var offsetY = 0
    private var targetViewIndex = 20
    private var position : MutableList<Int> = mutableListOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        targetImageView = binding.item1

        // 위치(index) - 그리드레이아웃(element) 리스트
        for(i in 0 .. 15) position.add(i)

        // recycler view로 아이템마다 setOnTouchListener 달아야함 리스트 크기는 최대 16고정
        targetImageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = targetImageView.x.toInt()
                    startY = targetImageView.y.toInt()
                    offsetX = event.rawX.toInt() - startX
                    offsetY = event.rawY.toInt() - startY
                    targetViewIndex = calculateNewIndex(startX + targetImageView.width / 2, startY + targetImageView.height / 2)
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX.toInt() - offsetX
                    val newY = event.rawY.toInt() - offsetY

                    // 바운더리 제한
                    val maxX = (targetImageView.parent as View).width - targetImageView.width/2
                    val maxY = (targetImageView.parent as View).height - targetImageView.height/2

                    // 새로운 위치를 바운더리 내에 유지하도록 조정
                    // -1을 안하면 합이 바운더리를 넘어가서 인덱스가 계산됨 (몫이 1이되는 걸 방지)
                    val constrainedX = newX.coerceIn(-(targetImageView.width/2-1), maxX-1)
                    val constrainedY = newY.coerceIn(-(targetImageView.height/2-1), maxY-1)

                    moveImageViewToPosition(targetImageView, constrainedX, constrainedY)

                    // Log.d("이동", "${newX}, ${newY}, ${maxX}, ${maxY} " )
                }
                // 시작위치가 좌측상단이므로 중간좌표에서 시작한 것처럼 width/2 , height/2 보정
                MotionEvent.ACTION_UP -> {
                    val newIndex = calculateNewIndex(targetImageView.x.toInt() + targetImageView.width/2
                        , targetImageView.y.toInt() + targetImageView.height/2)
                    Log.d("스왑전", "$targetViewIndex, $newIndex")
                    swapImageViewPosition(targetImageView, newIndex)
                }
            }
            true
        }
    }

    private fun moveImageViewToPosition(cur: ImageView, newX: Int, newY: Int) {
        cur.animate()
            .x(newX.toFloat())
            .y(newY.toFloat())
            .setDuration(0)
            .start()
    }

    private fun calculateNewIndex(x: Int, y: Int): Int {
        val columnWidth = targetImageView.width
        val rowHeight = targetImageView.height
        val columnIndex = x / columnWidth
        val rowIndex = y / rowHeight
        Log.d("스왑계산", "계산 : ${rowIndex * 4 + columnIndex}")
        return rowIndex * 4 + columnIndex
    }

    private fun moveImageViewToGridPosition(cur: ImageView, newIndex: Int) {
        val columnWidth = targetImageView.width
        val rowHeight = targetImageView.height
        val columnIndex = newIndex % 4
        val rowIndex = newIndex / 4
        val newX = columnIndex * columnWidth
        val newY = rowIndex * rowHeight
        moveImageViewToPosition(cur, newX, newY)
    }

    private fun swapImageViewPosition(cur: ImageView, nextIndex: Int) {
        val parentView = cur.parent as ViewGroup
        val curIndex = targetViewIndex // 15 현재위치
        val nxtIndex = nextIndex // 0 나중위치

        val org = parentView.getChildAt(position[curIndex]) as ImageView // 0번 이미지뷰
        val next = parentView.getChildAt(position[nxtIndex]) as ImageView // 15번 이미지뷰

        moveImageViewToGridPosition(org, nxtIndex)
        moveImageViewToGridPosition(next, curIndex)
        // 위치정보 변경
        position.swap(curIndex, nextIndex)
    }

    fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }
}