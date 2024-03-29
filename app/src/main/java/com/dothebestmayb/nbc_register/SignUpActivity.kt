package com.dothebestmayb.nbc_register

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dothebestmayb.nbc_register.databinding.ActivitySignUpBinding
import com.dothebestmayb.nbc_register.model.ErrorType
import com.dothebestmayb.nbc_register.util.BUNDLE_KEY_FOR_USER_INFO

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
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

        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.updateInputName(s.toString())
            }
        })

        binding.buttonRegister.setOnClickListener {
            viewModel.signUp()
        }
    }

    private fun setObserve() {
        viewModel.isAllInputFilled.observe(this) {
            binding.buttonRegister.isEnabled = it
        }

        viewModel.registeredUserInfo.observe(this) {
            intent.putExtra(BUNDLE_KEY_FOR_USER_INFO, it)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        viewModel.errorMessage.observe(this) {
            val text = when (it) {
                ErrorType.NO_USER_EXIST -> getString(R.string.id_is_not_registerd)
                ErrorType.NO_INPUT -> getString(R.string.missing_input_exist)
            }
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }
}