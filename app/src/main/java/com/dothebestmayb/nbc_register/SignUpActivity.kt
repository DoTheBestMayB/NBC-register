package com.dothebestmayb.nbc_register

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dothebestmayb.nbc_register.model.UserInfo
import com.dothebestmayb.nbc_register.util.BUNDLE_KEY_FOR_USER_INFO

class SignUpActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextId: EditText
    private lateinit var editTextPw: EditText
    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setReference()
        setListener()
    }

    private fun setReference() {
        editTextName = findViewById(R.id.edit_text_name)
        editTextId = findViewById(R.id.edit_text_id)
        editTextPw = findViewById(R.id.edit_text_pw)
        registerBtn = findViewById(R.id.button_register)
    }

    private fun setListener() {
        registerBtn.setOnClickListener {
            if (checkValidity().not()) {
                Toast.makeText(this, "입력되지 않은 정보가 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userInfo = UserInfo(
                editTextName.text.toString(),
                editTextId.text.toString(),
                editTextPw.text.toString(),
            )
            intent.putExtra(BUNDLE_KEY_FOR_USER_INFO, userInfo)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun checkValidity(): Boolean =
        editTextName.text.isNotBlank() && editTextId.text.isNotBlank() && editTextPw.text.isNotBlank()
}