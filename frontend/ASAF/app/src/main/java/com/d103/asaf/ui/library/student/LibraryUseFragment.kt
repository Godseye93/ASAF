package com.d103.asaf.ui.library.student

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d103.asaf.R
import com.d103.asaf.common.config.BaseFragment
import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.databinding.FragmentLibraryUseBinding
import com.d103.asaf.ui.library.CheckPermission
import com.d103.asaf.ui.library.Constants
import com.d103.asaf.ui.library.Constants.BEACON_DISTANCE
import com.d103.asaf.ui.library.Constants.RUNTIME_PERMISSIONS
import com.d103.asaf.ui.library.QRCodeScannerDialog
import com.d103.asaf.ui.library.adapter.BookAdapter
import com.d103.asaf.ui.library.adapter.NavigationListener
import com.d103.asaf.ui.op.OpFragmentViewModel
import com.d103.asaf.ui.sign.SignFragment
import kotlinx.coroutines.launch
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
private const val TAG = "교육생 도서관"
class LibraryUseFragment : BaseFragment<FragmentLibraryUseBinding>(FragmentLibraryUseBinding::bind, R.layout.fragment_library_use),
    NavigationListener {

    companion object {
        var parentViewModel : LibraryUseFragmentViewModel? = null
    }

    private val viewModel: LibraryUseFragmentViewModel by viewModels()
    private var books: MutableList<Book> = mutableListOf()
    private var adapter = BookAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel = viewModel
        initList()
        initView()
        initBeacon()

        // Override the default back button behavior
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate to StudentHomeFragment
                findNavController().navigate(R.id.action_libraryUseFragment_to_StudentHomeFragment)
            }
        })
    }

    private fun initList() {
        Log.d(TAG, "initList: ${viewModel.isFirst} ${adapter.isDraw}")
        lifecycleScope.launch {
            viewModel.myDraws.collect {
                books = viewModel.myDraws.value
                adapter.isDraw = true
                adapter.submitList(books)
                if(isAdded && view != null) requireView().invalidate()
            }
        }
        lifecycleScope.launch {
            viewModel.books.collect {
                if(viewModel.isFirst == false && adapter.isDraw == false ) {
                    books = it
                    adapter.submitList(books)
                    if(isAdded && view != null) requireView().invalidate()
                    binding.fragmentLibraryUserRecyclerview.scrollToPosition(0)
                }
            }
        }

        adapter.isDraw = true
        binding.fragmentLibraryUserRecyclerview.adapter = adapter
        adapter.submitList(books)
        if(isAdded && view != null) requireView().invalidate()
    }

    private fun initView() {
        binding.apply {
            bookUserToggleButton.seatText.text = "대출 현황"
            bookUserToggleButton.lockerText.text = "전체 도서 목록"
            fragmentLibraryUserTextviewSecond.text = "반납일"
            fragmentLibraryUserTextviewThird.text = "반납하기"
            bookUserToggleButton.moneyText.visibility = View.GONE

            bookUserToggleButton.setFirstButtonClickListener {
                Log.d(TAG, "initView: 눌림")
                adapter.isDraw = true
                fragmentLibraryUserTextviewSecond.text = "반납일"
                fragmentLibraryUserTextviewThird.text = "반납하기"
                books = viewModel.myDraws.value
                Log.d(TAG, "나의대출현황: ${books.size}")
                adapter.submitList(books)
                if(isAdded && view != null) requireView().invalidate()
                binding.fragmentLibraryUserRecyclerview.scrollToPosition(0)
            }

            bookUserToggleButton.setSecondButtonClickListener {
                adapter.isDraw = false
                fragmentLibraryUserTextviewSecond.text = "저자"
                fragmentLibraryUserTextviewThird.text = "수량"
                books = viewModel.books.value
                Log.d(TAG, "나의전체현황: ${books.size}")
                adapter.submitList(books)
                if(isAdded && view != null) requireView().invalidate()
                binding.fragmentLibraryUserRecyclerview.scrollToPosition(0)
            }

            fragmentLibraryUserSearchBar.setSearchClickListener {
                fragmentLibraryUserSearchBar.searchEditText.text.clear()
            }

            fragmentLibraryUserSearchBar.searchEditText.addTextChangedListener(searchWatcher)

            fragmentLibraryUserRecyclerview.isVisible = true

            fragmentLibraryUserFabDrawbook.setOnClickListener {
                // 카메라 찍는 fragment로 이동
                showQRCodeScannerDialog()
            }
        }
    }

    private val searchWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // 텍스트가 변경되기 전에 호출됩니다.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            bookSearch(s.toString())
        }

        override fun afterTextChanged(s: Editable?){

        }
    }

    private fun bookSearch(title: String) {
        val filteredBooks = books.filter { book -> book.bookName.contains(title) }
        adapter.submitList(filteredBooks)
    }

    // DB에 저장할 때 Date 타입
    private fun getDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 3)
        return calendar.time
    }

    // 텍스트에 사용할 때 String 타입
    private fun setDate(loanPeriod: Int): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, loanPeriod)
        return dateFormat.format(currentDate.time)
    }

    // 바코드 스캐너 호출
    private fun showQRCodeScannerDialog() {
        val dialogFragment = QRCodeScannerDialog.newInstance()
        dialogFragment.show(childFragmentManager, "QRCodeScannerDialog")
    }

    override fun navigateToDestination(book: Book) {
        val fragment = LibraryUseReturnFragment.instance(book)
        findNavController().navigate(R.id.action_libraryUseFragment_to_libraryUseReturnFragment, fragment.arguments)
    }

    // beacon
    private lateinit var beaconManager: BeaconManager
    private lateinit var checkPermission: CheckPermission
    private lateinit var bleScanner: BluetoothLeScanner
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var isDetected = true
    private val region = Constants.REGIONS

    private val requestBluetoothActivationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            bleScanner = bluetoothAdapter.bluetoothLeScanner // 문제의 코드
        } else {
            Toast.makeText(activity, "블루투스 활성화가 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private val bluetoothManager by lazy {
        requireActivity().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    // 비콘탐지
    private fun initBeacon() {
        bluetoothAdapter = bluetoothManager.adapter
        checkPermission = CheckPermission(requireActivity())
        //BeaconManager 지정
        beaconManager = BeaconManager.getInstanceForApplication(requireActivity())
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        bluetoothAdapter = bluetoothManager.adapter
        checkAllPermission()
    }


    // Register a result launcher for permission request
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
            permissions[Manifest.permission.BLUETOOTH_SCAN] == true &&
            permissions[Manifest.permission.BLUETOOTH_ADVERTISE] == true &&
            permissions[Manifest.permission.BLUETOOTH_CONNECT] == true) {
            // Permission granted
            turnOnBluetooth()
            startScan()
        } else {
            Toast.makeText(activity, "권한 요청이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAllPermission() {
        if (!checkPermission()) {
            // Request permissions
            requestPermissions()
        } else {
            // Permissions are already granted
            turnOnBluetooth()
            startScan()
        }
    }

    private fun checkPermission(): Boolean {
        // Check if all permissions are already granted
        val permissionResults = RUNTIME_PERMISSIONS.map {
            ActivityCompat.checkSelfPermission(requireContext(), it)
        }
        return permissionResults.all { it == PackageManager.PERMISSION_GRANTED }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(RUNTIME_PERMISSIONS)
    }

    var rangeNotifier: RangeNotifier? = null
    // beacon 초기화
    private fun startScan() {
        val monitorNotifier: MonitorNotifier = object : MonitorNotifier {
            override fun didEnterRegion(region: Region) {
                Handler(Looper.getMainLooper()).post{
                    if(activity != null) Toast.makeText(activity,"도서관 근처 입니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun didExitRegion(region: Region) { //발견 못함.
            }

            override fun didDetermineStateForRegion(state: Int, region: Region) { //상태변경
            }
        }

        rangeNotifier = RangeNotifier { beacons, _ ->
            beacons?.run{
                if (isNotEmpty()) {
                    forEach { beacon ->
                        // 사정거리 내에 있을 경우 이벤트 표시 다이얼로그 팝업
                        if (beacon.distance <= BEACON_DISTANCE && isDetected == true && adapter.isDraw == true) {
                            Log.d(TAG, "didRangeBeaconsInRegion: distance 이내.")
                            // 정보 안변하는데 위에 걸로 반영돼서 활성화되는지 확인 필요
                            books = viewModel.myDraws.value
                            adapter = BookAdapter(this@LibraryUseFragment)
                            adapter.isDraw = true
                            adapter.nearBy = true // 비콘 근처면 근처라고 변경하고 submitList
                            if(isAdded) binding.fragmentLibraryUserRecyclerview.adapter = adapter
                            adapter.submitList(books)
                            if(rangeNotifier!=null) beaconManager.removeRangeNotifier(rangeNotifier!!)
//                            Handler(Looper.getMainLooper()).postDelayed ({
//                                beaconManager.addRangeNotifier(rangeNotifier!!)
//                            },10000)
                        } else {
                            Log.d(TAG, "didRangeBeaconsInRegion: distance 이외.")
                        }
                        Log.d( TAG,"distance: " + beacon.distance + " id:" + beacon.id1 + "/" + beacon.id2 + "/" + beacon.id3)
                    }
                }
                if (isEmpty()) {
                    if(bluetoothAdapter!!.isEnabled == false) {
                        Toast.makeText(activity, "블루투스 기능을 확인해 주세요.", Toast.LENGTH_SHORT).show()
                        requestBluetoothActivation()
                    }
                    Log.d(TAG, "didRangeBeaconsInRegion: 비컨을 찾을 수 없습니다.")
                }
            }
        }

        // 리전에 비컨이 있는지 없는지..정보를 받는 클래스 지정
        beaconManager.addMonitorNotifier(monitorNotifier) // 지정한 DISTANCE 안에 있는지 확인
        beaconManager.startMonitoring(region)

        //detacting되는 해당 region의 beacon정보를 받는 클래스 지정.
        beaconManager.addRangeNotifier(rangeNotifier!!) // 비컨이 얼마나 떨어진 '거리' 에 있는지 알려줌
        beaconManager.startRangingBeacons(region)
    }

    // 블루투스 기능 꺼져있을 때
    private fun turnOnBluetooth() {
        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            Toast.makeText(activity, "블루투스 기능을 확인해 주세요.", Toast.LENGTH_SHORT).show()
            requestBluetoothActivation()
        } else {
            bleScanner = bluetoothAdapter.bluetoothLeScanner // 문제의 코드
        }
    }

    private fun requestBluetoothActivation() {
        val bleIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestBluetoothActivationLauncher.launch(bleIntent)
    }

    override fun onPause() {
        super.onPause()
        if(rangeNotifier!=null) beaconManager.removeRangeNotifier(rangeNotifier!!)
    }

    override fun onStart() {
        Log.d(TAG, "onStart: ")
        super.onStart()
        isDetected = true
        adapter.isDraw = true
        adapter.nearBy = false
        viewModel.isFirst = true
        if(rangeNotifier!=null) beaconManager.addRangeNotifier(rangeNotifier!!)
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        isDetected = true
        adapter.isDraw = true
        adapter.nearBy = false
        viewModel.isFirst = true
        if(rangeNotifier!=null) beaconManager.addRangeNotifier(rangeNotifier!!)
    }
}