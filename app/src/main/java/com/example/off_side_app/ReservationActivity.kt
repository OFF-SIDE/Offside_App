package com.example.off_side_app

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.databinding.ActivityGroundBinding
import com.example.off_side_app.databinding.ActivityReservationBinding
import com.example.off_side_app.network.ReservationRequest
import com.example.off_side_app.network.ReserveConnectionApi
import com.example.off_side_app.ui.GroundViewModel
import com.example.off_side_app.ui.LoadingDialog
import com.example.off_side_app.network.ReserveResponse
import com.example.off_side_app.network.ReserveViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

class ReservationActivity : AppCompatActivity() {

    private val buttons: List<Button> by lazy {
        List(13) { index ->
            findViewById<Button>(
                resources.getIdentifier(
                    "hour${1000 + 100 * index}_btn2",
                    "id",
                    packageName
                )
            )
        }
    }

    lateinit var binding: ActivityReservationBinding
    private var uri: Uri? = null


    private val viewModel2: ReserveViewModel by viewModels()

    private val ReserveConnectionApi = ReserveConnectionApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[GroundViewModel::class.java]

        val currentStadiumId = intent.getIntExtra("stadiumId", -1)

        viewModel.getGroundDetailData(currentStadiumId, 1210)  // 오늘 날짜로
        val dialog = LoadingDialog(this@ReservationActivity)

        dialog.show()

        viewModel.detail.observe(this) { notice ->
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
                binding.userPhoneText.setText(groundInfo.contactPhone)
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
        }

        binding.reservationBackBtn.setOnClickListener {
            finish()
        }


        binding.daySelectBtn.setOnClickListener {

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val selectedDate = "${year - 2000}${month + 1}${day}"
                binding.daySelectBtn.text = "${year - 2000}/${month + 1}/${day}"

                // ReserveViewModel을 사용하여 데이터를 가져오고 UI를 갱신
                viewModel2.fetchDataAndChangeButtonBackground1(
                    stadiumId = currentStadiumId,
                    date = selectedDate,
                    onSuccess = {
                        // 성공 시 reserveData를 사용하는 예시
                        viewModel2.reservationListData.observe(
                            this@ReservationActivity
                        ) { reservationList ->
                            // reservationList에 접근하는 부분 예시
                            Log.d("ReservationActivity", "Reservation List Observer Called: $reservationList")

                            // 성공 시 matchingQData를 사용하는 예시
                            viewModel2.matchingQData.observe(
                                this@ReservationActivity
                            ) { matchingQData ->
                                // matchingQData에 접근하는 부분 예시
                                Log.d("ReservationActivity", "MatchingQ List Observer Called: $matchingQData")

                                // 여기서 matchingQData와 reservationList를 사용하여 UI를 업데이트하는 로직을 추가할 수 있음
                                updateUI(reservationList, matchingQData, currentStadiumId, selectedDate)
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
        Log.d("ReservationActivity", "Updating UI - Reservation List: $reservationList, MatchingQ List: $matchingQ")
        for (index in buttons.indices) {
            val button = buttons[index]
            val time = (1000 + index * 100).toString()

            when {
                time in reservationList -> {
                    // 해당 시간대의 버튼의 배경을 검정색으로 변경
                    button.setBackgroundResource(R.drawable.buttonshape3)// 적절한 리소스 ID로 변경
                    button.isClickable = false
                }
                time in matchingQ -> {
                    // 해당 시간대의 버튼의 배경을 녹색으로 변경
                    button.setBackgroundResource(R.drawable.buttonshape2) // 적절한 리소스 ID로 변경
                    button.setOnClickListener{
                        getmatcingDialog(stadiumId, date, time)
                    }
                }
                else -> {
                    // 해당 시간대가 없는 경우, 버튼의 배경을 흰색으로 변경
                    button.setBackgroundResource(R.drawable.buttonshape) // 적절한 리소스 ID로 변경
                    button.setOnClickListener {
                        getbookingDialog(stadiumId, date, time)
                    }
                }
            }
        }
        // buttonIndex를 사용하여 특정 버튼에 대한 작업을 수행할 수 있습니다.
    }

    private fun getmatcingDialog(stadiumId: Int, date: String, time: String){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.user_matching_info_dialog, null)

        val matchinginfo2 = mDialogView.findViewById<TextView>(R.id.matching_info)
        val matchingname2 = mDialogView.findViewById<TextView>(R.id.matching_name)
        val matchingpnum2 = mDialogView.findViewById<TextView>(R.id.matching_pnum)

        viewModel2.fetchcommentData(
            stadiumId = stadiumId,
            date = date,
            time = time,
            onSuccess = {
                viewModel2.matchingname.observe(
                    this@ReservationActivity
                ) { matchingname ->
                    Log.d("ReservationActivity", "mathingname Observer Called: $matchingname")

                    viewModel2.matchingphone.observe(
                        this@ReservationActivity
                    ) { matchingphone ->
                        // matchingQData에 접근하는 부분 예시
                        Log.d("ReservationActivity", "matchingphone Observer Called: $matchingphone")

                        viewModel2.matchinginfo.observe(
                            this@ReservationActivity
                        ){matchinginfo->

                            Log.d("ReservationActivity", "matchinginfo Observer Called: $matchinginfo")

                            matchingname2?.text = matchingname
                            matchingpnum2?.text = matchingphone
                            matchinginfo2?.text = matchinginfo
                        }
                    }
                }
            },
            onError = { exception ->
                // 실패 시 처리
                Log.e("ReservationActivity", "Error: $exception")
            }
        )

        // EditText 참조
        val matchingnameEdit: EditText = mDialogView.findViewById(R.id.matchingnameEdit)
        val matchingpnumEdit: EditText = mDialogView.findViewById(R.id.matchingpnumEdit)
        val matchinginfoEdit: EditText = mDialogView.findViewById(R.id.matchinginfoEdit)


        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("예약 정보")

        val  mAlertDialog = mBuilder.show()

        val bookingButton = mDialogView.findViewById<Button>(R.id.usermatcing_btn1)
        bookingButton.setOnClickListener {
            val matchingname = matchingnameEdit.text.toString()
            val matchingpnum = matchingpnumEdit.text.toString()
            if (matchingname.isNotEmpty() && matchingpnum.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.Main) {
                    try {
                        ReserveConnectionApi.createReservation(
                            userName = matchingname,
                            userPhone = matchingpnum,
                            stadiumId = stadiumId,
                            date = date,
                            time = time
                        )
                    }catch (e: Exception) {
                        // 네트워크 호출이나 비동기 작업에서 예외 처리
                        e.printStackTrace()
                        Toast.makeText(this@ReservationActivity, "예약 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } finally {
                        // 예약 생성 성공 여부와 상관없이 대화상자 닫기
                        mAlertDialog.dismiss()
                    }
                }
            } else {
                Toast.makeText(this, "전화번호와 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                // 사용자에게 이름과 전화번호를 입력하라는 메시지를 보여줄 수 있음
            }
            mAlertDialog.dismiss()
        }

        val matchingButton = mDialogView.findViewById<Button>(R.id.usermatcing_btn2)
        matchingButton.setOnClickListener {
            val matchingName = matchingnameEdit.text.toString()
            val matchingPnum = matchingpnumEdit.text.toString()
            val matchingInfo = matchinginfoEdit.text.toString()
            if (matchingName.isNotEmpty() && matchingPnum.isNotEmpty() && matchingInfo.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.Main) {
                    try {
                        ReserveConnectionApi.createMatching(
                            userName = matchingName,
                            contactPhone = matchingPnum,
                            stadiumId = stadiumId,
                            date = date,
                            time = time,
                            comment = matchingInfo
                        )
                    }catch (e: Exception) {
                        // 네트워크 호출이나 비동기 작업에서 예외 처리
                        e.printStackTrace()
                        Toast.makeText(this@ReservationActivity, "매칭 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } finally {
                        // 예약 생성 성공 여부와 상관없이 대화상자 닫기
                        mAlertDialog.dismiss()
                    }
                }
            } else {
                Toast.makeText(this, "전화번호와 이름, 상대방에게 보여줄 정보를 입력하세요.", Toast.LENGTH_SHORT).show()
                // 사용자에게 이름과 전화번호를 입력하라는 메시지를 보여줄 수 있음
            }
            mAlertDialog.dismiss()
        }

        val noButton = mDialogView.findViewById<Button>(R.id.usermatcing_btn3)
        noButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun getbookingDialog(stadiumId: Int, date: String, time: String) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.user_booking_info_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("예약 정보")

        lateinit var userNameEditText: EditText
        lateinit var userPhoneEditText: EditText
        lateinit var userinfoEditText: EditText

        // EditText 초기화
        userNameEditText = mDialogView.findViewById(R.id.usernameEdit) ?: throw IllegalStateException("usernameEdit not found")
        userPhoneEditText = mDialogView.findViewById(R.id.userpnumEdit) ?: throw IllegalStateException("userpnumEdit not found")
        userinfoEditText = mDialogView.findViewById(R.id.userinfoEdit) ?: throw IllegalStateException("userinfoEdit not found")

        val mAlertDialog = mBuilder.show()


        val bookingButton = mDialogView.findViewById<Button>(R.id.userbooking_btn1)
        bookingButton.setOnClickListener {
            val userName = userNameEditText.text.toString()
            val userPhone = userPhoneEditText.text.toString()

            if (userName.isNotEmpty() && userPhone.isNotEmpty()) {
                // 사용자 입력이 유효한 경우
                lifecycleScope.launch {
                    try {
                        // 백그라운드 스레드에서 예약 생성
                        ReserveConnectionApi.createReservation(
                            userName = userName,
                            userPhone = userPhone,
                            stadiumId = stadiumId,
                            date = date,
                            time = time
                        )
                        // 예약 생성 성공 시 추가 작업 수행 가능
                    } catch (e: Exception) {
                        // 네트워크 호출이나 비동기 작업에서 예외 처리
                        e.printStackTrace()
                        Toast.makeText(this@ReservationActivity, "예약 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } finally {
                        // 예약 생성 성공 여부와 상관없이 대화상자 닫기
                        mAlertDialog.dismiss()
                    }
                }
            } else {
                // 사용자 입력이 유효하지 않은 경우
                Toast.makeText(this@ReservationActivity, "전화번호와 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }


        val matcingButton = mDialogView.findViewById<Button>(R.id.userbooking_btn2)
        matcingButton.setOnClickListener {
            val userName = userNameEditText.text.toString()
            val userPhone = userPhoneEditText.text.toString()
            val userinfo = userinfoEditText.text.toString()
            if (userName.isNotEmpty() && userPhone.isNotEmpty() && userinfo.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.Main) {
                    try {
                        ReserveConnectionApi.createMatching(
                            userName = userName,
                            contactPhone = userPhone,
                            stadiumId = stadiumId,
                            date = date,
                            time = time,
                            comment = userinfo
                        )
                    }catch (e: Exception) {
                        // 네트워크 호출이나 비동기 작업에서 예외 처리
                        e.printStackTrace()
                        Toast.makeText(this@ReservationActivity, "매칭 생성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } finally {
                        // 예약 생성 성공 여부와 상관없이 대화상자 닫기
                        mAlertDialog.dismiss()
                    }
                }
            } else {
                Toast.makeText(this, "전화번호와 이름, 상대방에게 보여줄 정보를 입력하세요.", Toast.LENGTH_SHORT).show()
                // 사용자에게 이름과 전화번호를 입력하라는 메시지를 보여줄 수 있음
            }
            mAlertDialog.dismiss()
        }


        val noButton = mDialogView.findViewById<Button>(R.id.userbooking_btn3)
        noButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }



    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // 갤러리에서 선택한 이미지의 Uri를 저장
            uri = result.data!!.data
            Glide.with(this)
                .load(uri)
                .into(binding.pictureImageView)
        }
    }

}






