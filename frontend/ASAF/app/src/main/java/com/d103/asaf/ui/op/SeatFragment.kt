package com.d103.asaf.ui.op

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.d103.asaf.R
import com.d103.asaf.common.component.SeatView
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.databinding.FragmentSeatBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

// 1학기는 학생정보
// 2학기는 자리정보가 입력 / 저장 한다는 가정인데 두개의 데이터가 다르므로 한 번에 넣을 수가 없다.
// 자리정보의 name이 1학기는 학생이름 , 2학기는 팀이름이면 상관없음
class SeatFragment() :
    BaseFragment<FragmentSeatBinding>(FragmentSeatBinding::bind, R.layout.fragment_seat) {
    private lateinit var targetView: SeatView
    private var startX = 0
    private var startY = 0
    private var offsetX = 0
    private var offsetY = 0
    private lateinit var imm: InputMethodManager
    private val num = 5
    private var targetViewIndex = 20 // 초기화 될 거라 의미 없음
    private var position: MutableList<Int> = mutableListOf() // 초기화 될 거라 의미 없음
    private var seat: MutableList<Int> = mutableListOf() // 초기화 될 거라 의미 없음
    private var reversePosition = (0..24).toMutableList()
    private lateinit var gridLayout: GridLayout
    private var seatNum = 0;
    private var viewModel: OpFragmentViewModel = OpFragment.parentViewModel!!

    companion object {
        private const val POSITION = "position"
        private const val SEAT = "seat"

        // Factory method to create an instance of SeatFragment with position list.
        fun instance(
            position: MutableList<Int>,
            seat: MutableList<Int>,
            parentViewModel: OpFragmentViewModel
        ): SeatFragment {
            val fragment = SeatFragment()
            fragment.viewModel = parentViewModel
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
        imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("포지션 불러오기", "onViewCreated: ${viewModel.position.value}")
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.docSeat.collect { newSeat ->
                if (isAdded) {
//                    seat = viewModel.seat.value
                    seat = viewModel.docSeat.value.map { it.seatNum }.toMutableList()
                    seatNum = seat.size
                    loadSeat() // 업데이트
                    Log.d("자리 프래그먼트", "onViewCreated: ${viewModel.seat.value} 업데이트 됨")
                    Log.d("자리 프래그먼트", "docSeat: ${viewModel.docSeat} 업데이트 됨")
                }
            }
        }

        gridLayout = binding.gridLayout
        targetView = binding.item1 // 아무 아이템이나 같은 크기이므로 넣어주면 됨 사이즈 계산에만 사용
        seatNum = seat.size
        //배치 완료 버튼 기능 넣어주기
        completeRemote()

        binding.apply {
            seatAdd.setOnClickListener {
                seatNumberInput.visibility = View.VISIBLE
                seatAdd.visibility = View.INVISIBLE
                seatRandom.visibility = View.GONE
                gridLayout.visibility = View.INVISIBLE
                switchToEditText()
                completeLocal()
                // 변경 후 변경된 정보 가져와서 UI업데이트
                viewModel.callRealSeats()
            }
            // position 정보를 seatNum 크기 만큼만 보내기 서버에서 n건을 수정해야함
            seatComplete.setOnClickListener {
                lifecycleScope.launch {
                    try {
                        RetrofitUtil.opService.postSeats(viewModel.setSeats(position, seatNum))
                    }
                    catch (e:Exception) {
                        Toast.makeText(requireActivity(),"네트워크 연결 없음",Toast.LENGTH_SHORT).show()
                    }
                    // completeRemote()
                }
            }

            seatRandom.setOnClickListener {
                seatRandomAll.isVisible = !seatRandomAll.isVisible
                seatRandomPart.isVisible = !seatRandomPart.isVisible
            }

            seatRandomAll.setOnClickListener {
                allRandom()
                seatRandomAll.visibility = View.INVISIBLE
                seatRandomPart.visibility = View.INVISIBLE
                Log.d("랜덤올", "onViewCreated: $position")
            }

            seatRandomPart.setOnClickListener {
                seatRandom()
                seatRandomAll.visibility = View.INVISIBLE
                seatRandomPart.visibility = View.INVISIBLE
                Log.d("랜덤자리", "onViewCreated: $position")
            }

            seatNumberInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val text = s.toString()
                    seatNum = try {
                        text.toInt()
                    } catch (e: NumberFormatException) {
                        0
                    } catch (e: SocketTimeoutException) {
                        Toast.makeText(requireContext(), "네트워크 에러로 곧 종료됩니다.", Toast.LENGTH_SHORT)
                            .show()
                        0
                    }
                    if (seatNum >= viewModel.docSeat.value.size) seatNum = viewModel.docSeat.value.size
//                    if (seatNum >= viewModel.students.value.size) seatNum = viewModel.students.value.size
                }
            })
        }
        // ViewTreeObserver를 이용하여 targetView의 크기를 측정
        targetView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                targetView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if(isAdded) setSeat()
                Log.d("포지션", "$position")
                Log.d("리버스포지션", "$reversePosition")
            }
        })
    }

    private fun allRandom() {
        position.shuffle()
        setSeat()
    }

    private fun seatRandom() {
        val subPosition = position.subList(0,seatNum)
        val shuffled = subPosition.shuffled()

        for(i in 0 until seatNum) position[i] = shuffled[i]
        if(isAdded) setSeat()
    }

    fun setSeat() {
        Log.d("싯넘", "setSeat: $seatNum")
        // targetView의 크기가 측정된 후에 다른 작업 수행
        val occupy = ResourcesCompat.getDrawable(resources, R.drawable.chair, null)
        val vacant = ResourcesCompat.getDrawable(resources, R.drawable.ssafy_logo, null)

        for (i in 0 until gridLayout.childCount) {
            val childView = gridLayout.getChildAt(i)
            if (childView is SeatView) {
                setTouchListener(childView)
                if (i < seatNum) {
                    childView.seatImage.setImageDrawable(occupy)
                    childView.seatText.text = viewModel.docSeat.value[i].name // 이름을 넣는 부분
                } else {
                    childView.seatImage.setImageDrawable(vacant)
                    childView.seatText.text = ""
                }
                setViewPosition(childView, position[i])
                reversePosition[position[i]] = i
            }
        }
    }

    fun setInitialSeat() {
        Log.d("싯넘", "setSeat: $seatNum")
        // targetView의 크기가 측정된 후에 다른 작업 수행
        val occupy = ResourcesCompat.getDrawable(resources, R.drawable.chair, null)
        val vacant = ResourcesCompat.getDrawable(resources, R.drawable.ssafy_logo, null)

        for (i in 0 until gridLayout.childCount) {
            val childView = gridLayout.getChildAt(i)
            if (childView is SeatView) {
                setTouchListener(childView)
                if (i < seatNum) {
                    childView.seatImage.setImageDrawable(occupy)
                    childView.seatText.text = viewModel.docSeat.value[i].name // 이름을 넣는 부분
                } else {
                    childView.seatImage.setImageDrawable(vacant)
                    childView.seatText.text = ""
                }
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

    private fun loadSeat() {
        val fin = seat.size
        val remainingNumbers = position.filterNot { it in seat }
        // 차례대로 불러온 자리 채워넣기
        for (i in 0 until fin) position[i] = seat[i]
        // 나머지 자리 (0~24중) 들어가지 않은 숫자를 이미지 뷰에 넣기
        for (i in fin until 25) position[i] = remainingNumbers[i - fin]
        setSeat()
    }

    private fun setViewPosition(curView: SeatView, newIndex: Int) {
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
        position.swap(reversePosition[curIndex], reversePosition[nxtIndex])
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
                    targetViewIndex =
                        calculateNewIndex(startX + target.width / 2, startY + target.height / 2)
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
                    val newIndex = calculateNewIndex(
                        target.x.toInt() + target.width / 2 - 5,
                        target.y.toInt() + target.height / 2 - 5
                    )
                    Log.d("스왑전", "$targetViewIndex, $newIndex")
                    swapImageViewPosition(target, newIndex)
                    Log.d("자리변경후", "setTouchListener: $position")
                    Log.d("자리변경후리버스", "setTouchListener: $reversePosition")
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
            imm.showSoftInput(seatNumberInput, InputMethodManager.SHOW_IMPLICIT)
            seatNumberInput.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    clearSeat()
                    completeRemote()
                }
                false
            }

            seatNumberInput.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard()
                    clearSeat()
                    completeRemote()
                }
            }
        }
    }

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(binding.seatNumberInput.windowToken, 0)
        binding.apply {
            seatAdd.visibility = View.VISIBLE
            seatRandom.visibility = View.VISIBLE
            seatNumberInput.visibility = View.INVISIBLE
            gridLayout.visibility = View.VISIBLE
        }
    }

    private fun completeLocal() {
        Log.d("포스트전시트", "completeLocal: 로컬")
        binding.seatComplete.setOnClickListener {
            hideKeyboard()
            clearSeat()
        }
    }

    private fun completeRemote() {
        binding.seatComplete.setOnClickListener {
            hideKeyboard()
            clearSeat()
            Log.d("포스트시트", "postSeats: 보내기전 ${viewModel.docSeat.value}")
            // 변경된 정보를 POST 해준다.
            lifecycleScope.launch{
                postSeats()
            }
        }
    }

    // 서버에서 유저 id로 조회하여 최초로 자리 정보가 들어가 있는 상태라면 update로 처리해야함
//    private fun postSeats() {
//        for (i in 0 until seatNum) {
//            viewModel.docSeat[i].seatNum = seat[i]
//        }
//        // POST List<docSeat>
//        CoroutineScope(Dispatchers.IO).launch {
//            if (!RetrofitUtil.opService.postSeats(viewModel.docSeat))
//                Toast.makeText(context, "자리 업데이트 네트워크 오류", Toast.LENGTH_SHORT).show()
//        }
//    }

    private suspend fun postSeats() {
        for (i in 0 until seatNum) {
            viewModel.docSeat.value[i].seatNum = seat[i]
        }
        try {
            val response = withContext(Dispatchers.IO) {
//                RetrofitUtil.opService.postSeats(viewModel.setSeats(position, seatNum))
                RetrofitUtil.opService.postSeats(viewModel.docSeat.value.subList(0, seatNum))
            }
            if (response.isSuccessful) {
                // 변경 후 변경된 정보 가져와서 UI업데이트
                viewModel.callRealSeats()
            } else {
                Toast.makeText(context, "자리 업데이트 네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("자리 변경", "자리 오류: ${e.message}", e)
        }
    }
}