package com.d103.asaf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.d103.asaf.common.config.BaseActivity
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.databinding.ActivityMainBinding
import com.d103.asaf.ui.login.LoginFragmentViewModel
import com.d103.asaf.ui.home.pro.ProHomeFragment
import com.d103.asaf.ui.schedule.NotiRegisterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView
import java.sql.Date

private const val TAG = "MainActivity"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate){

    private lateinit var user : Member
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    var waitTime = 0L
    private val viewModel : SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNaviPro.visibility = View.GONE
        binding.bottomNaviStudent.visibility = View.GONE
        setupNavHost()
        setSupportActionBar(findViewById(com.airbnb.lottie.R.id.action_bar));
        // user 정보 가지고 오기
        // 임시 데이터
        var user = Member(
            1, 123, "testName", "test@test.com",
            "testPass", "testInfo",
            Date(System.currentTimeMillis()).toString(), 123, "010-1234-5478", "123", 2, "프로"
        )
        // user 정보 가지고 오고 난 후 activity 나누기

//        when (user.authority) {
//            "교육생" -> {
//                binding.bottomNaviPro.visibility = View.GONE
//                binding.bottomNaviStudent.visibility = View.VISIBLE
//            }
//
//            "프로" -> {
//                binding.bottomNaviPro.visibility = View.VISIBLE
//                binding.bottomNaviStudent.visibility = View.GONE
//
//                val navHostFragment =
//                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
//                val navController = navHostFragment.navController
////                val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
//                val bottomNavigationView =
//                    findViewById<MorphBottomNavigationView>(R.id.bottom_navi_pro)
//                bottomNavigationView.setupWithNavController(navController)
//
//            }
//        }

    }
    private fun setupNavHost() {
        // NavHostFragment를 가져와서 설정합니다.
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.login_fragment -> hideBottomNavigationBar()
                R.id.ProhomeFragment-> showProBottomNavigationBarFromFragment()
                R.id.scheduleFragment -> showProBottomNavigationBarFromFragment()

            }
        }


        // 없던 부분
        val bottomNavigationView =
            findViewById<MorphBottomNavigationView>(R.id.bottom_navi_pro)
        bottomNavigationView.setupWithNavController(navController)
    }


    // Create a public method to hide the bottom navigation bar.
    fun hideBottomNavigationBarFromFragment() {
        hideBottomNavigationBar()
    }
    fun showBottomNavigationBarFromFragment() {
        showBottomNavigationBar()
    }
    fun showProBottomNavigationBarFromFragment() {
        showProBottomNavigationBar()
    }








}