package com.dothebestmayb.nbc_register.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dothebestmayb.nbc_register.R
import com.dothebestmayb.nbc_register.databinding.ActivityHomeBinding
import com.dothebestmayb.nbc_register.model.PictureValue
import com.dothebestmayb.nbc_register.util.ID
import com.dothebestmayb.nbc_register.util.NAME

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataFromIntent()
        setListener()
        setObserve()
    }

    private fun getDataFromIntent() {
        val id = intent.getStringExtra(ID)
            ?: throw IllegalStateException(getString(R.string.home_activity_should_get_id))
        val name = intent.getStringExtra(NAME)
            ?: throw IllegalStateException(getString(R.string.home_activity_should_get_name))

        viewModel.setUserInfo(id, name)
    }

    private fun setListener() {
        binding.buttonExit.setOnClickListener {
            finish()
        }
    }

    private fun setObserve() {
        viewModel.pictureValue.observe(this) {
            val resourceId = when (it) {
                PictureValue.GARLIC -> R.drawable.garlic
                PictureValue.ONION -> R.drawable.onion
                PictureValue.POTATO -> R.drawable.potato
                PictureValue.STRAWBERRY -> R.drawable.strawberry
                PictureValue.SWEET_POTATO -> R.drawable.sweet_potato
            }
            binding.imageView.setImageResource(resourceId)
        }

        viewModel.userId.observe(this) {
            binding.textViewId.text = it
        }
        viewModel.userName.observe(this) {
            binding.textViewName.text = it
        }
        viewModel.userAge.observe(this) {
            binding.textViewAge.text = it.toString()
        }
        viewModel.userMbti.observe(this) {
            binding.textViewMbti.text = it
        }
    }
}