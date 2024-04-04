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
        binding.textFieldName.helperText = "이름을 입력해주세요."
        binding.textFieldEmailFront.helperText = "이메일을 입력해주세요."
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
                when (checkType) {
                    CheckType.NAME -> viewModel.updateInputName(s.toString())
                    CheckType.EMAIL_FRONT -> viewModel.updateInputEmailFront(s.toString())
                    CheckType.EMAIL_TAIL -> viewModel.updateInputEmailTail(s.toString())
                    CheckType.PW -> viewModel.updateInputPw(s.toString())
                    CheckType.PW_CHECK -> viewModel.updateInputPwCheck(s.toString())
                }
            }
        }

        binding.buttonRegister.setOnClickListener {
            viewModel.signUp()
        }
    }

    private fun setObserve() {
        viewModel.inputName.observe(this) {
            // helperText에 null을 넣으면 disabled 되면서 TextInputLayout에 topToBottomOf 제약을 건 비밀번호 확인 창의 위치가 변경됨
            binding.textFieldName.helperText = if (it.isBlank()) {
                "이름을 입력해주세요."
            } else {
                "　" // // Helper Text가 사라지면 비밀번호 확인 창의 높이가 달라지기 때문에 ㄱ + 한자 + 1을 이용해 만들 수 있는 빈 특수문자 사용
            }
        }
        viewModel.inputPw.observe(this) {
            binding.textFieldPw.helperText = if (it.isBlank()) {
                "10자리 이상, 특수문자, 대문자 포함"
            } else {
                "　" // Helper Text가 사라지면 비밀번호 확인 창의 높이가 달라지기 때문에 ㄱ + 한자 + 1을 이용해 만들 수 있는 빈 특수문자 사용
            }
        }
        viewModel.isEmailFilled.observe(this) {
            binding.textFieldEmailFront.helperText = if (it) {
                "이메일을 입력해주세요."
            } else {
                "　"
            }
        }
        viewModel.isAllInputFilled.observe(this) {
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
                SignUpErrorType.PW_LENGTH_IS_NOT_CORRECT -> getString(R.string.pw_length_is_no_correct)
                SignUpErrorType.CAPITAL_IS_NOT_CONTAINED -> getString(R.string.capital_is_not_contained)
                SignUpErrorType.SPECIAL_CHARACTER_IS_NOT_CONTAINED -> getString(R.string.special_character_is_not_contained).format(
                    Validator.allowedSpecialCharacter.joinToString(", ")
                )

                SignUpErrorType.NOT_ALLOWED_CHARACTER_IS_CONTAINED -> getString(R.string.not_allowed_character_is_contained)
                SignUpErrorType.ALREADY_REGISTERED_ID -> getString(R.string.id_is_already_registered)
            }
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }
}