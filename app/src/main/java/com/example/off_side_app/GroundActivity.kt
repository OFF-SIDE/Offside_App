package com.example.off_side_app

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.GroundInfoForPost
import com.example.off_side_app.databinding.ActivityGroundBinding
import com.example.off_side_app.network.ReserveConnectionApi
import com.example.off_side_app.network.ReserveViewModel
import com.example.off_side_app.ui.GroundViewModel
import com.example.off_side_app.ui.LoadingDialog

import java.util.Calendar

class GroundActivity : AppCompatActivity() {


    private val buttons: List<Button> by lazy {
        List(13) { index ->
            findViewById<Button>(
                resources.getIdentifier(
                    "hour${1000 + 100 * index}_btn",
                    "id",
                    packageName
                )
            )
        }
    }

    lateinit var binding: ActivityGroundBinding
    private var uri: Uri? = null

    private val viewModel2: ReserveViewModel by viewModels()

    private val ReserveConnectionApi = ReserveConnectionApi()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[GroundViewModel::class.java]

        val currentStadiumId = intent.getIntExtra("stadiumId", -1)

        if(currentStadiumId != -1){
            // 기존 구장의 경우 기존 정보로 텍스트 채우기
            viewModel.getGroundDetailData(currentStadiumId, null)  // 오늘 날짜로
            val dialog = LoadingDialog(this@GroundActivity)

            dialog.show()

            viewModel.detail.observe(this, Observer{ notice ->
                val groundInfo = notice
                try {
                    // 1. 이미지
                    Glide.with(binding.pictureImageView)
                        .load(groundInfo.image)
                        .error(R.drawable.baseline_error_24)
                        .into(binding.pictureImageView)

                    // 2. 이름
                    binding.userNameText.setText(groundInfo.name)
                    var phone = StringBuilder(groundInfo.contactPhone)
                    phone.insert(3, "-").insert(8, "-")
                    binding.userPhoneText.setText(phone.toString())
                    binding.userAddressText.setText(groundInfo.address)
                    binding.userCommentText.setText(groundInfo.comment)
                    var price = groundInfo.price!!.toString()
                    price = "$price 원/h"
                    binding.userPriceText.setText(price)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
                dialog.dismiss()
            })
        }


        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.daySelectBtn.setOnClickListener {

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val selectedDate = "${year - 2000}${month + 1}${day}"
                binding.daySelectBtn.text = "${year - 2000}/${month + 1}/${day}"
                viewModel2.fetchDataAndChangeButtonBackground1(
                    stadiumId = currentStadiumId,
                    date = selectedDate,
                    onSuccess = {
                        // 성공 시 reserveData를 사용하는 예시
                        viewModel2.reservationListData.observe(
                            this@GroundActivity
                        ) { reservationList ->
                            // reservationList에 접근하는 부분 예시
                            Log.d(
                                "ReservationActivity",
                                "Reservation List Observer Called: $reservationList"
                            )

                            // 성공 시 matchingQData를 사용하는 예시
                            viewModel2.matchingQData.observe(
                                this@GroundActivity
                            ) { matchingQData ->
                                // matchingQData에 접근하는 부분 예시
                                Log.d(
                                    "ReservationActivity",
                                    "MatchingQ List Observer Called: $matchingQData"
                                )

                                // 여기서 matchingQData와 reservationList를 사용하여 UI를 업데이트하는 로직을 추가할 수 있음
                                updateUI(
                                    reservationList,
                                    matchingQData,
                                    currentStadiumId,
                                    selectedDate
                                )
                            }
                        }
                    },
                    onError = { exception ->
                        // 실패 시 처리
                        Log.e("ReservationActivity", "Error: $exception")
                    }
                )
            }

                DatePickerDialog(
                    this,
                    data,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()


            }


        }


    private fun updateUI(reservationList: List<String>, matchingQ: List<String>, stadiumId: Int, date: String) {
        Log.d("ReservationActivity", "Updating UI - Reservation List: $reservationList")
        for (index in buttons.indices) {
            val button = buttons[index]
            val time = (1000 + index * 100).toString()

            when {
                time in reservationList -> {
                    // 해당 시간대의 버튼의 배경을 검정색으로 변경
                    button.setBackgroundResource(R.drawable.buttonshape3) // 적절한 리소스 ID로 변경
                    button.setOnClickListener {
                        getconfirmDialog(stadiumId, date, time)
                    }
                }
                else -> {
                    // 해당 시간대가 없는 경우, 버튼의 배경을 흰색으로 변경
                    button.setBackgroundResource(R.drawable.buttonshape) // 적절한 리소스 ID로 변경
                }
            }
        }
    }

    fun checkContentsFull(binding: ActivityGroundBinding, uri: Uri?): Boolean{
        if(binding.userAddressText.text.toString() == "")
            return false
        if(binding.userNameText.text.toString() == "")
            return false
        if(binding.userCommentText.text.toString() == "")
            return false
        if(binding.userPhoneText.text.toString() == "")
            return false
        if(binding.userPriceText.text.toString() == "")
            return false
        return true
    }

    fun getBodyForPost(binding: ActivityGroundBinding, currentPosition: Int, serverUrl: String): GroundInfoForPost {
        var groundInfoForPost = GroundInfoForPost(
            AppDataManager.nearLocations[currentPosition],
            binding.userNameText.text.toString(),
            binding.userPhoneText.text.toString(),
            binding.userAddressText.text.toString(),
            binding.userCommentText.text.toString(),
            binding.userPriceText.text.toString().toInt(),
            serverUrl
        )
        return groundInfoForPost
    }

        fun getconfirmDialog(stadiumId: Int, date: String, time: String) {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.booking_info_dialog, null)

            val reservename = mDialogView.findViewById<TextView>(R.id.reserve_name)
            val reservepnum = mDialogView.findViewById<TextView>(R.id.reserve_pnum)

            viewModel2.fetchDataNameAndPnum(
                stadiumId = stadiumId,
                date = date,
                time = time,
                onSuccess = {
                    // 성공 시 reserveData를 사용하는 예시
                    viewModel2.username.observe(
                        this@GroundActivity
                    ) { username->
                        // reservationList에 접근하는 부분 예시
                        Log.d(
                            "GroundActivity",
                            "username Observer Called: $username"
                        )

                        // 성공 시 matchingQData를 사용하는 예시
                        viewModel2.userphone.observe(
                            this@GroundActivity
                        ) { userphone ->
                            // matchingQData에 접근하는 부분 예시
                            Log.d(
                                "GroundActivity",
                                "userphone Observer Called: $userphone"
                            )

                            // 여기서 matchingQData와 reservationList를 사용하여 UI를 업데이트하는 로직을 추가할 수 있음
                            reservename?.text = username
                            reservepnum?.text = userphone
                        }
                    }
                },
                onError = { exception ->
                    // 실패 시 처리
                    Log.e("ReservationActivity", "Error: $exception")
                }
            )

            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("예약 정보")

            val mAlertDialog = mBuilder.show()

            val okButton = mDialogView.findViewById<Button>(R.id.booking_btn1)
            okButton.setOnClickListener {
                mAlertDialog.dismiss()
            }

            val noButton = mDialogView.findViewById<Button>(R.id.booking_btn2)
            noButton.setOnClickListener {

            }
        }

        private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                // 갤러리에서 선택한 이미지의 Uri를 저장
                uri = result.data!!.data
                Glide.with(this)
                    .load(uri)
                    .into(binding.pictureImageView)
            }
        }
}








