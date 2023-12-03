package com.example.off_side_app

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.AppDataManager.reserve
import com.example.off_side_app.databinding.ActivityGroundBinding
import com.example.off_side_app.ui.GroundMainViewModel
import com.example.off_side_app.ui.GroundViewModel
import java.util.Calendar

class GroundActivity : AppCompatActivity() {
    lateinit var binding: ActivityGroundBinding
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        val currentName = intent.getStringExtra("currentName")
        val currentDes = intent.getStringExtra("currentDes")
        val currentImagePath: Uri? = intent.getParcelableExtra("currentImagePath")
        val currentListIdx = intent.getIntExtra("currentDataListIdx", -1)

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

         */



        binding.saveBtn.setOnClickListener {
            // 기존의 구장이라면 수정, 신규 구장이라면 추가 api호출
            /*
            var adapter = GroundMainAdapter()

            val name = binding.nameText.text.toString()
            val address = binding.addressText.text.toString()

            // 이미지 선택 여부 확인
            if (uri != null && name != null && address != null && currentPosition != -1) {
                if(newFlag) {
                    if(AppDataManager.isLocationExist(currentPosition)){
                        groundItems.add(Ground(name, address, uri, currentPosition))
                    }
                }
                else{
                    val groundItem:Ground = groundItems[currentListIdx] as Ground
                    groundItem.name = name
                    groundItem.address = address
                    groundItem.imagePath = uri
                    groundItem.locationPosition = currentPosition
                }
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                // 내용이 비어있는 경우
                Toast.makeText(this, "내용을 모두 작성하세요.", Toast.LENGTH_SHORT).show()
            }
            */
        }

        val viewModel = ViewModelProvider(this)[GroundViewModel::class.java]

        val currentStadiumId = intent.getIntExtra("stadiumId", -1)

        // 갤러리에서 이미지 불러오기
        binding.selectImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        // 스피너
        val itemArray = AppDataManager.nearLocations
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, itemArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        // 신규 생성의 경우 location의 position=0
        var currentPosition = 0
        if(currentStadiumId != -1){
            // 기존에 있는 경우 api에 받은 location값으로 할당
            currentPosition = 1
        }

        binding.spinner.setSelection(currentPosition)


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                currentPosition = position
                // 선택한 구에 대한 작업 수행
                Toast.makeText(this@GroundActivity, currentPosition.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작
                Toast.makeText(this@GroundActivity, "아무 것도 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        binding.saveBtn.setOnClickListener {
            if(currentStadiumId == -1){
                // 신규 생성의 경우
                if(checkContentsFull(binding, uri)){
                    val body = getBodyForPost(binding, currentPosition)
                    viewModel.postGroundData(body)
                }
            }
        }

        val today = reserve["2023/11/26"]

        if (today != null) {
            val hourIndex = 6
            today.nameList[hourIndex] = "정한샘"
            today.pnumList[hourIndex] = "010-9259-4719"
            today.day[hourIndex] = true
        }

        binding.daySelectBtn.setOnClickListener {

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { _, year, month, day -> val selectedDate = "${year}/${month + 1}/${day}"
                binding.daySelectBtn.text = "${year}/${month+1}/${day}"
            val rese = reserve[selectedDate]

            if (rese != null) {
                // buttonInfo를 사용하여 원하는 작업 수행
                for (i in rese.day.indices) {
                    if (rese.day[i]) {
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

    fun checkContentsFull(binding: ActivityGroundBinding, uri: Uri?): Boolean{
        if(binding.addressText.text.toString() == "")
            return false
        if(binding.nameText.text.toString() == "")
            return false
        if(binding.commentText.text.toString() == "")
            return false
        if(binding.contactPhoneText.text.toString() == "")
            return false
        if(binding.priceText.text.toString() == "")
            return false
        return true
    }

    fun getBodyForPost(binding: ActivityGroundBinding, currentPosition: Int): GroundInfoForPost{
        lateinit var groundInfoForPost: GroundInfoForPost
        groundInfoForPost.address = binding.addressText.text.toString()
        groundInfoForPost.comment = binding.commentText.text.toString()
        groundInfoForPost.contactPhone = binding.contactPhoneText.text.toString()
        groundInfoForPost.location = AppDataManager.nearLocations[currentPosition]
        groundInfoForPost.price = binding.priceText.text.toString().toInt()
        groundInfoForPost.name = binding.nameText.text.toString()
        return groundInfoForPost
    }

    private fun performTrueCase(buttonIndex: Int, ) {
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






