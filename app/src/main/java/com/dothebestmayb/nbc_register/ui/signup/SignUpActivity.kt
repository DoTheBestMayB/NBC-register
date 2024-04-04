package com.dothebestmayb.nbc_register.ui.signup

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.dothebestmayb.nbc_register.R
import com.dothebestmayb.nbc_register.databinding.ActivitySignUpBinding
import com.dothebestmayb.nbc_register.model.CheckType
import com.dothebestmayb.nbc_register.model.SignUpErrorType
import com.dothebestmayb.nbc_register.model.TypeForObserveAtSignUp
import com.dothebestmayb.nbc_register.util.BUNDLE_KEY_FOR_USER_INFO
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setListener()
        setObserve()
    }

    private fun initView() {
        binding.textFieldName.helperText = getString(R.string.hint_for_name)
        binding.textFieldEmailFront.helperText = getString(R.string.missing_input_email)
    }

    private fun setListener() = with(binding) {
        listOf(
            CheckType.NAME to textFieldName,
            CheckType.EMAIL_FRONT to textFieldEmailFront,
            CheckType.EMAIL_TAIL to textFieldEmailTail,
            CheckType.PW to textFieldPw,
            CheckType.PW_CHECK to textFieldPwCheck,
        ).forEach { (checkType: CheckType, tf: TextInputLayout) ->
            tf.editText?.doOnTextChanged { s, _, _, _ ->
                inputData(checkType, s.toString())
            }
            // Focus가 바뀔 때 유효성을 검사해야 하는 이유를 아직 모르겠습니다.
            tf.setOnFocusChangeListener { v, _ ->
                val s = (v as TextInputLayout).editText.toString()
                inputData(checkType, s)
            }
        }
        binding.buttonRegister.setOnClickListener {
            viewModel.signUp()
        }
    }

    private fun inputData(checkType: CheckType, s: String) {
        when (checkType) {
            CheckType.NAME -> viewModel.updateInputName(s)
            CheckType.EMAIL_FRONT -> viewModel.updateInputEmailFront(s)
            CheckType.EMAIL_TAIL -> viewModel.updateInputEmailTail(s)
            CheckType.PW -> viewModel.updateInputPw(s)
            CheckType.PW_CHECK -> viewModel.updateInputPwCheck(s)
        }
    }

    private fun setObserve() {
        listOf(
            TypeForObserveAtSignUp.NAME to viewModel.isNameFilled,
            TypeForObserveAtSignUp.PW_CHECK to viewModel.isPwFilled,
            TypeForObserveAtSignUp.EMAIL to viewModel.isEmailFilled,
        ).forEach { (checkType, liveData) ->
            val (targetView, helperText) = when (checkType) {
                TypeForObserveAtSignUp.NAME -> binding.textFieldName to getString(R.string.hint_for_name)
                TypeForObserveAtSignUp.PW_CHECK -> binding.textFieldPw to getString(R.string.hint_for_pw_condition)
                TypeForObserveAtSignUp.EMAIL -> binding.textFieldEmailFront to getString(R.string.missing_input_email)
            }
            liveData.observe(this) {
                // helperText에 null을 넣으면 disabled 되면서 TextInputLayout에 topToBottomOf 제약을 건 비밀번호 확인 창의 위치가 변경됨
                targetView.helperText = if (it) {
                    "　" // Helper Text가 사라지면 비밀번호 확인 창의 높이가 달라지기 때문에 ㄱ + 한자 + 1을 이용해 만들 수 있는 빈 특수문자 사용
                } else {
                    helperText
                }
            }
        }
        viewModel.isAllValid.observe(this) {
            binding.buttonRegister.isEnabled = it
        }

        viewModel.registeredUserInfo.observe(this) {
            intent.putExtra(BUNDLE_KEY_FOR_USER_INFO, it)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        viewModel.errorType.observe(this) {
            val text = when (it) {
                SignUpErrorType.NO_INPUT -> getString(R.string.missing_input_exist)
                SignUpErrorType.ALREADY_REGISTERED_ID -> getString(R.string.id_is_already_registered)
            }
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }
}