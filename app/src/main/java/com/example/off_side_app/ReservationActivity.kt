package com.example.off_side_app

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.off_side_app.Adapter.GroundMainAdapter
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.Ground
import com.example.off_side_app.databinding.ActivityGroundBinding
import com.example.off_side_app.databinding.ActivityReservationBinding
import java.util.Calendar

class ReservationActivity : AppCompatActivity() {

    lateinit var binding: ActivityReservationBinding
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentName = intent.getStringExtra("currentName")
        val currentDes = intent.getStringExtra("currentDes")
        val currentImagePath: Uri? = intent.getParcelableExtra("currentImagePath")
        val currentListIdx = intent.getIntExtra("currentDataListIdx", -1)
        var currentPosition = intent.getIntExtra("currentLocationPosition", -1)
        var newFlag = false
        var groundItems = AppDataManager.getOriginalGroundItems()

        if (currentName != null)
            binding.nameText.setText(currentName)
        else
            newFlag = true

        if (currentDes != null)
            binding.addressText.setText(currentDes)

        if (currentImagePath != null) {
            uri = currentImagePath
            Glide.with(this)
                .load(uri)
                .error(R.drawable.baseline_error_24)
                .into(binding.pictureImageView)
        }

        binding.selectImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        val itemArray = AppDataManager.nearLocations

        if(!newFlag) {
            currentPosition = groundItems[currentListIdx].locationPosition
        }

        binding.spinner.setText(AppDataManager.nearLocations[currentPosition])

        binding.backBtn.setOnClickListener {
            finish()
        }

        val dateButtonInfoMap = mutableMapOf<String, BooleanArray>()

        // 날짜 "2023/11/22"에 대한 21개의 버튼 정보를 true로 초기화
        dateButtonInfoMap["2023/11/22"] = BooleanArray(24) { true }

        // 날짜 "2023/11/23"에 대한 21개의 버튼 정보를 false로 초기화
        dateButtonInfoMap["2023/11/23"] = BooleanArray(24) { false }

        binding.daySelectBtn.setOnClickListener {

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { _, year, month, day -> val selectedDate = "${year}/${month + 1}/${day}"
                binding.daySelectBtn.text = "${year}/${month+1}/${day}"
                val buttonInfo = dateButtonInfoMap[selectedDate]

                if (buttonInfo != null) {
                    // buttonInfo를 사용하여 원하는 작업 수행
                    for (i in buttonInfo.indices) {
                        if (buttonInfo[i]) {
                            // true일 경우 실행할 코드
                            performTrueCase(i)
                        } else {
                            // false일 경우 실행할 코드
                            performFalseCase(i)
                        }
                    }
                } else {
                    // 선택된 날짜에 대한 정보가 없을 경우의 처리
                    Toast.makeText(this, "해당 날짜에 대한 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                }

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

    private fun performTrueCase(buttonIndex: Int) {
        // 여기에 true일 경우 실행할 코드를 작성하세요.

        val buttonId = resources.getIdentifier("hour${buttonIndex}_btn", "id", packageName)
        binding.root.findViewById<Button>(buttonId)?.setBackgroundResource(R.drawable.buttonshape2)
        binding.root.findViewById<Button>(buttonId)?.setOnClickListener {
            val name ="정한샘"
            val phonenumeber ="010-9259-4719"
            getbookingDialog(name, phonenumeber)
        }
    }

    // false일 경우 실행할 코드
    private fun performFalseCase(buttonIndex: Int) {
        // 여기에 false일 경우 실행할 코드를 작성하세요.
        val buttonId = resources.getIdentifier("hour${buttonIndex}_btn", "id", packageName)
        binding.root.findViewById<Button>(buttonId)?.setBackgroundResource(R.drawable.buttonshape)
        binding.root.findViewById<Button>(buttonId)?.setOnClickListener{
            Toast.makeText(this, "예약이 되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }
        // buttonIndex를 사용하여 특정 버튼에 대한 작업을 수행할 수 있습니다.
    }

    private fun getbookingDialog(name: String, pnum: String){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.booking_info_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("예약 정보")

        val  mAlertDialog = mBuilder.show()

        val bookname = mDialogView.findViewById<TextView>(R.id.booking_name)
        val bookpnum = mDialogView.findViewById<TextView>(R.id.booking_pnum)

        bookname.text = "예약자 이름 : $name"
        bookpnum.text = "전화 번호 : $pnum"

        val okButton = mDialogView.findViewById<Button>(R.id.booking_btn1)
        okButton.setOnClickListener {
            mAlertDialog.dismiss()
        }

        val noButton = mDialogView.findViewById<Button>(R.id.booking_btn2)
        noButton.setOnClickListener {

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






