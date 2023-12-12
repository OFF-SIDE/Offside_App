package com.example.off_side_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.off_side_app.databinding.ActivityGroundMainBinding
import com.example.off_side_app.databinding.ActivityUserReservationCheckBinding

class UserReservationCheckActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserReservationCheckBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_reservation_check)
    }
}