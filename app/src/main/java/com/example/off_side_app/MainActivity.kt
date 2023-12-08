package com.example.off_side_app

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.databinding.ActivityMainBinding
import android.Manifest


class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == RESULT_OK){
            /*
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
             */
        }
    }

    var permission_list = arrayOf<String>(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val sharedPreference = getSharedPreferences("ground", MODE_PRIVATE)
        val savedPhoneNumber = sharedPreference.getString("phoneNumber", "no number")

        checkPermission()

        if (savedPhoneNumber == "no number"){
            // 만약 sharedPreference에 전화번호가 없다면 입력창으로
            val intent = Intent(this, UserPhoneNumberActivity::class.java)
            activityResultLauncher.launch(intent)
        }
        else{
            AppDataManager.phoneNumber = savedPhoneNumber
        }

        binding.imageButton2.setOnClickListener{
            val intent = Intent(this, UserMainActivity::class.java)
            startActivity(intent)
        }

        binding.mainGroundButton.setOnClickListener{
            val intent = Intent(this, GroundMainActivity::class.java)
            startActivity(intent)
        }

        binding.testPhoneNumber.setOnClickListener {
            Toast.makeText(this, AppDataManager.phoneNumber, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun checkPermission() {
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        //안드로이드6.0 (마시멜로) 이후 버전부터 유저 권한설정 필요
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        for (permission in permission_list) {
            //권한 허용 여부를 확인한다.
            val chk = checkCallingOrSelfPermission(permission!!)
            if (chk == PackageManager.PERMISSION_DENIED) {
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list, 0)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            for (i in grantResults.indices) {
                //허용됬다면
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //권한을 하나라도 허용하지 않는다면 앱 종료
                    Toast.makeText(applicationContext, "앱권한설정하세요", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

}