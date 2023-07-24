package com.d103.asaf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.d103.asaf.common.config.BaseActivity
import com.d103.asaf.common.model.dto.Accounts_user
import com.d103.asaf.databinding.ActivityMainBinding
import java.sql.Date

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate){

    private lateinit var user : Accounts_user
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        // user 정보 가지고 오기
        // 임시 데이터
        user = Accounts_user(2, "Test", "Test@test.com", "test",
            Date(System.currentTimeMillis()) ,
            "010-1234-5678", "test", "123", "1","학생")
        // user 정보 가지고 오고 난 후 activity 나누기

        when(user.authority){
            "교육생" -> {
                binding.bottomNaviPro.visibility = View.GONE
                binding.bottomNaviStudent.visibility = View.VISIBLE
            }
            "프로" -> {
                binding.bottomNaviPro.visibility = View.VISIBLE
                binding.bottomNaviStudent.visibility = View.GONE
            }
        }
    }
}