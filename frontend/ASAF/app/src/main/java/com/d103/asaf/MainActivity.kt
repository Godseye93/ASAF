package com.d103.asaf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.d103.asaf.common.model.dto.Accounts_user
import com.d103.asaf.databinding.ActivityMainBinding
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView
import java.sql.Date

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate){

    private lateinit var user : Accounts_user
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(com.airbnb.lottie.R.id.action_bar));
        // user 정보 가지고 오기
        // 임시 데이터
        user = Accounts_user(2, "Test", "Test@test.com", "test",
            Date(System.currentTimeMillis()) ,
            "010-1234-5678", "test", "123", "1","프로")
        // user 정보 가지고 오고 난 후 activity 나누기

        when(user.authority){
            "교육생" -> {
                binding.bottomNaviPro.visibility = View.GONE
                binding.bottomNaviStudent.visibility = View.VISIBLE
            }
            "프로" -> {
                binding.bottomNaviPro.visibility = View.VISIBLE
                binding.bottomNaviStudent.visibility = View.GONE

                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                val navController = navHostFragment.navController
                val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
                val bottomNavigationView = findViewById<MorphBottomNavigationView>(R.id.bottom_navi_pro)
                bottomNavigationView.setupWithNavController(navController)

//                val bottomNavView = findViewById<MorphBottomNavigationView>(R.id.bottomNavigationView)
//                navController = Navigation.findNavController(this, R.id.nav_graph)
//
//                NavigationUI.setupWithNavController(bottomNavView, navController)




//                val navView: MorphBottomNavigationView =  binding.bottomNaviPro
//
//                val navController = findNavController(R.id.nav_host_fragment_activity_main)
//                // Passing each menu ID as a set of Ids because each
//                // menu should be considered as top level destinations.
//                val appBarConfiguration = AppBarConfiguration(setOf(
//                    R.id.navigation_op, R.id.navigation_schedule, R.id.navigation_pro_home, R.id.navigation_library_management, R.id.navigation_bus))
//                setupActionBarWithNavController(navController, appBarConfiguration)
//                navView.setupWithNavController(navController)




            }
        }



    }
}