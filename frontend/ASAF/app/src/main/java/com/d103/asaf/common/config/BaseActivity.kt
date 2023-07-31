package com.d103.asaf.common.config
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.d103.asaf.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// 액티비티의 기본을 작성, 뷰 바인딩 활용
abstract class BaseActivity<B : ViewBinding>(private val inflate: (LayoutInflater) -> B) :
    AppCompatActivity() {
    protected lateinit var binding: B
        private set


    // 뷰 바인딩 객체를 받아서 inflate해서 화면을 만들어줌.
    // 즉 매번 onCreate에서 setContentView를 하지 않아도 됨.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
    }

    // 로딩 다이얼로그, 즉 로딩창을 띄워줌.
    // 네트워크가 시작될 때 사용자가 무작정 기다리게 하지 않기 위해 작성.


    // 토스트를 쉽게 띄울 수 있게 해줌.
    fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 바텀 네비게이션 바를 숨기는 메서드를 protected로 변경
    fun hideBottomNavigationBar() {
        val bottomNavigationViewPro = findViewById<BottomNavigationView>(R.id.bottom_navi_pro)
        val bottomNavgiationViewStu = findViewById<BottomNavigationView>(R.id.bottom_navi_student)
        bottomNavigationViewPro.visibility = View.GONE
        bottomNavgiationViewStu.visibility = View.GONE
    }
    fun showBottomNavigationBar() {
        val bottomNavigationViewPro = findViewById<BottomNavigationView>(R.id.bottom_navi_pro)
        val bottomNavgiationViewStu = findViewById<BottomNavigationView>(R.id.bottom_navi_student)

        bottomNavigationViewPro.visibility = View.VISIBLE
        bottomNavgiationViewStu.visibility = View.VISIBLE


    }

    fun showProBottomNavigationBar() {
        val bottomNavigationViewPro = findViewById<BottomNavigationView>(R.id.bottom_navi_pro)
        val bottomNavgiationViewStu = findViewById<BottomNavigationView>(R.id.bottom_navi_student)

        bottomNavigationViewPro.visibility = View.VISIBLE
        bottomNavgiationViewStu.visibility = View.GONE

    }

    fun showStudentBottomNavigationBar() {
        val bottomNavigationViewPro = findViewById<BottomNavigationView>(R.id.bottom_navi_pro)
        val bottomNavgiationViewStu = findViewById<BottomNavigationView>(R.id.bottom_navi_student)

        bottomNavigationViewPro.visibility = View.GONE
        bottomNavgiationViewStu.visibility = View.VISIBLE
    }
}