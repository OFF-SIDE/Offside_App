package com.example.off_side_app

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.off_side_app.databinding.ActivityMainBinding
import com.example.off_side_app.databinding.ActivityRefereeBinding
import com.example.off_side_app.network.ReserveApi
import com.example.off_side_app.network.ReserveConnectionApi
import com.example.off_side_app.network.ReserveViewModel
import com.example.off_side_app.ui.GroundViewModel
import com.example.off_side_app.ui.LoadingDialog
import com.example.off_side_app.ui.RefereeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RefereeActivity : AppCompatActivity() {

    private val buttons: List<Button> by lazy {
        List(13) { index ->
            findViewById<Button>(
                resources.getIdentifier(
                    "hour${1000 + 100 * index}_btn3",
                    "id",
                    packageName
                )
            )
        }
    }

    private val binding by lazy{
        ActivityRefereeBinding.inflate(layoutInflater)
    }

    private val viewModel2: ReserveViewModel by viewModels()

    private val ReserveConnectionApi = ReserveConnectionApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[RefereeViewModel::class.java]

        val currentId = intent.getIntExtra("id", -1)

        if(currentId != -1){
            // 기존 구장의 경우 기존 정보로 텍스트 채우기
            viewModel.getRefereeDetailData(currentId)  // 오늘 날짜로
            val dialog = LoadingDialog(this@RefereeActivity)

            dialog.show()

            viewModel.detail.observe(this) { notice ->
                val refereeInfo = notice
                try {
                    // 1. 이미지
                    Glide.with(binding.pictureImageView)
                        .load(refereeInfo.image)
                        .circleCrop()
                        .error(R.drawable.baseline_error_24)
                        .into(binding.pictureImageView)

                    // 2. 이름

                    binding.userNameText.setText(refereeInfo.name)
                    var phone = StringBuilder(refereeInfo.contactPhone)
                    phone.insert(3, "-").insert(8, "-")
                    binding.userPhoneText.setText(phone.toString())
                    binding.userCommentText.setText(refereeInfo.comment)
                    var price = refereeInfo.price!!.toString()
                    price = "$price 원/h"
                    binding.userPriceText.setText(price)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
                dialog.dismiss()
            }
        }

        Log.d(
            "RefereeActivity",
            "$currentId"
        )

        val currentDate = Date()

        // SimpleDateFormat을 사용하여 원하는 형식으로 날짜를 포맷팅
        val dateFormat = SimpleDateFormat("yyMMdd", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        viewModel2.fetchDataAndChangeButtonBackground_r(
            refereeId = currentId,
            date = formattedDate,
            onSuccess = {
                // 성공 시 reserveData를 사용하는 예시
                viewModel2.refereeTimeData.observe(
                    this@RefereeActivity
                ) { availableTime ->
                    // reservationList에 접근하는 부분 예시
                    Log.d(
                        "RefereeActivity",
                        "Referee Time Observer Called: $availableTime"
                    )

                    //UI를 업데이트하는 로직을 추가할 수 있음
                    updateUI(availableTime, currentId, formattedDate)
                }
            },
            onError = { exception ->
                // 실패 시 처리
                Log.e("refereeActivity", "Error: $exception")
            }
        )

        val dateFormat2 = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
        val formattedDate2 = dateFormat2.format(currentDate)
        binding.daySelectBtn.text = "$formattedDate2"

        binding.daySelectBtn.setOnClickListener {

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val selectedDate = "${year - 2000}${month + 1}${day}"
                binding.daySelectBtn.text = "${year - 2000}/${month + 1}/${day}"
                viewModel2.fetchDataAndChangeButtonBackground_r(
                    refereeId = currentId,
                    date = selectedDate,
                    onSuccess = {
                        // 성공 시 reserveData를 사용하는 예시
                        viewModel2.refereeTimeData.observe(
                            this@RefereeActivity
                        ) { availableTime ->
                            // reservationList에 접근하는 부분 예시
                            Log.d(
                                "RefereeActivity",
                                "Referee Time Observer Called: $availableTime"
                            )

                            //UI를 업데이트하는 로직을 추가할 수 있음
                                updateUI(availableTime, currentId, selectedDate)
                        }
                    },
                    onError = { exception ->
                        // 실패 시 처리
                        Log.e("refereeActivity", "Error: $exception")
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


    private fun updateUI(availableTime: List<String>,refereeId: Int, date: String) {
        Log.d("refereeActivity", "Updating UI - Reservation List: $availableTime")
        for (index in buttons.indices) {
            val button = buttons[index]
            val time = (1000 + index * 100).toString()

            when {
                time in availableTime -> {
                    // 해당 시간대의 버튼의 배경을 흰색으로 변경
                    button.setBackgroundResource(R.drawable.buttonshape)
                    // 적절한 리소스 ID로 변경
                    button.setOnClickListener {
                        getrefereeDialog(refereeId, date, time)
                    }
                }
                else -> {
                    // 해당 시간대가 없는 경우, 버튼의 배경을 검정색
                    button.setBackgroundResource(R.drawable.buttonshape3)
                     // 적절한 리소스 ID로 변경
                    button.isClickable = false
                }
            }
        }
    }

    private fun getrefereeDialog(refereeId: Int, date: String, time: String){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.referee_booking_dialog, null)

        // EditText 참조
        val usernameEdit: EditText = mDialogView.findViewById(R.id.usernameEdit)
        val userpnumEdit: EditText = mDialogView.findViewById(R.id.userpnumEdit)
        val userinfoEdit: EditText = mDialogView.findViewById(R.id.userinfoEdit)


        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()

        val bookingButton = mDialogView.findViewById<Button>(R.id.userbooking_btn1)
        bookingButton.setOnClickListener {
            val username = usernameEdit.text.toString()
            val userpnum = userpnumEdit.text.toString()
            val comment = userinfoEdit.text.toString()
            if (username.isNotEmpty() && userpnum.isNotEmpty() && comment.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.Main) {
                    try {
                        ReserveConnectionApi.createRefereeData(
                            refereeId = refereeId,
                            userName = username,
                            userPhone = userpnum,
                            date = date,
                            time = time,
                            comment = comment
                        )
                    }catch (e: Exception) {
                        // 네트워크 호출이나 비동기 작업에서 예외 처리
                        e.printStackTrace()
                        Toast.makeText(this@RefereeActivity, "예약 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } finally {
                        // 예약 생성 성공 여부와 상관없이 대화상자 닫기
                        mAlertDialog.dismiss()
                        viewModel2.fetchDataAndChangeButtonBackground_r(
                            refereeId = refereeId,
                            date = date,
                            onSuccess = {
                                // 성공 시 reserveData를 사용하는 예시
                                viewModel2.refereeTimeData.observe(
                                    this@RefereeActivity
                                ) { availableTime ->
                                    // reservationList에 접근하는 부분 예시
                                    Log.d(
                                        "RefereeActivity",
                                        "Referee Time Observer Called: $availableTime"
                                    )

                                    //UI를 업데이트하는 로직을 추가할 수 있음
                                    updateUI(availableTime, refereeId, date)
                                }
                            },
                            onError = { exception ->
                                // 실패 시 처리
                                Log.e("refereeActivity", "Error: $exception")
                            }
                        )
                    }
                }
            } else {
                Toast.makeText(this, "전화번호와 이름, 주의 사항을을 입력하세요.", Toast.LENGTH_SHORT).show()
                // 사용자에게 이름과 전화번호를 입력하라는 메시지를 보여줄 수 있음
            }
            mAlertDialog.dismiss()


        }

        val noButton = mDialogView.findViewById<Button>(R.id.userbooking_btn3)
        noButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
}