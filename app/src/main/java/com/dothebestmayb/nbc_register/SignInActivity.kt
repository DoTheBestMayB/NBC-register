package com.dothebestmayb.nbc_register

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dothebestmayb.nbc_register.model.UserInfo
import com.dothebestmayb.nbc_register.util.BUNDLE_KEY_FOR_USER_INFO
import com.dothebestmayb.nbc_register.util.ID
import com.dothebestmayb.nbc_register.util.NAME

class SignInActivity : AppCompatActivity() {

    private lateinit var editTextId: EditText
    private lateinit var editTextPw: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private var isIdFilled = false
    private var isPwFilled = false

    private val registeredInfo = hashMapOf<String, UserInfo>()

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            return@registerForActivityResult
        }
        val data = result.data
        val userInfo = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data?.getParcelableExtra(BUNDLE_KEY_FOR_USER_INFO, UserInfo::class.java)
        } else {
            data?.getParcelableExtra<UserInfo>(BUNDLE_KEY_FOR_USER_INFO)
        } ?: return@registerForActivityResult
        registeredInfo[userInfo.id] = userInfo
        fillIdAndPw(userInfo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setReference()
        setListener()
    }


    private fun setReference() {
        editTextId = findViewById(R.id.edit_text_id)
        editTextPw = findViewById(R.id.edit_text_pw)
        loginBtn = findViewById(R.id.button_login)
        registerBtn = findViewById(R.id.button_register)
    }

    private fun setListener() {
        editTextId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isIdFilled = s?.isNotEmpty() ?: false
                loginBtn.isEnabled = isIdFilled && isPwFilled
            }
        })

        editTextPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isPwFilled = s?.isNotEmpty() ?: false
                loginBtn.isEnabled = isIdFilled && isPwFilled
            }
        })

        loginBtn.setOnClickListener {
            val userInfo = getUserInfo() ?: run {
                Toast.makeText(this, "아이디, 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra(ID, userInfo.id)
                putExtra(NAME, userInfo.name)
            }
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        registerBtn.setOnClickListener {
            getContent.launch(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun getUserInfo(): UserInfo? {
        val id = editTextId.text.toString()
        val pw = editTextPw.text.toString()
        val info = registeredInfo[id]
        return if (info?.pw == pw) {
            info
        } else {
            null
        }
    }

    private fun fillIdAndPw(userInfo: UserInfo) {
        editTextId.setText(userInfo.id)
        editTextPw.setText(userInfo.pw)
    }
}