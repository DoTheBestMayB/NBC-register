package com.dothebestmayb.nbc_register.ui.signin

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dothebestmayb.nbc_register.R
import com.dothebestmayb.nbc_register.databinding.ActivitySignInBinding
import com.dothebestmayb.nbc_register.model.SignInErrorType
import com.dothebestmayb.nbc_register.model.UserInfo
import com.dothebestmayb.nbc_register.ui.home.HomeActivity
import com.dothebestmayb.nbc_register.ui.signup.SignUpActivity
import com.dothebestmayb.nbc_register.util.BUNDLE_KEY_FOR_USER_INFO
import com.dothebestmayb.nbc_register.util.ID
import com.dothebestmayb.nbc_register.util.NAME

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val viewModel by viewModels<SignInViewModel>()

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }
            val data = result.data
            val userInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data?.getParcelableExtra(BUNDLE_KEY_FOR_USER_INFO, UserInfo::class.java)
            } else {
                data?.getParcelableExtra<UserInfo>(BUNDLE_KEY_FOR_USER_INFO)
            } ?: return@registerForActivityResult
            fillIdAndPw(userInfo)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        setObserve()
    }

    private fun setListener() {
        binding.editTextId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.updateInputId(s.toString())
            }
        })

        binding.editTextPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.updateInputPw(s.toString())
            }
        })

        binding.buttonLogin.setOnClickListener {
            viewModel.login()
        }

        binding.buttonRegister.setOnClickListener {
            getContent.launch(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun setObserve() {
        viewModel.isAllInputFilled.observe(this) {
            binding.buttonLogin.isEnabled = it
        }

        viewModel.loggedUserInfo.observe(this) { userInfo ->
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra(ID, userInfo.email)
                putExtra(NAME, userInfo.name)
            }
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        viewModel.errorMessage.observe(this) {
            val text = when (it) {
                SignInErrorType.NOT_VALID -> getString(R.string.check_id_and_pw)
                SignInErrorType.NO_ID_INPUT -> getString(R.string.missing_id_input)
                SignInErrorType.NO_PW_INPUT -> getString(R.string.missing_pw_input)
            }
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }

    private fun fillIdAndPw(userInfo: UserInfo) {
        binding.editTextId.setText(userInfo.email)
        binding.editTextPw.setText(userInfo.pw)
    }
}