package com.dothebestmayb.nbc_register

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dothebestmayb.nbc_register.util.ID
import com.dothebestmayb.nbc_register.util.NAME
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    private lateinit var textId: TextView
    private lateinit var textName: TextView
    private lateinit var textAge: TextView
    private lateinit var textMbti: TextView
    private lateinit var imageView: ImageView
    private lateinit var exitBtn: Button

    private val random = Random(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setReference()
        setRandomImage()
        setInfo()
        setListener()
    }

    private fun setReference() {
        textId = findViewById(R.id.text_view_id)
        textName = findViewById(R.id.text_view_name)
        textAge = findViewById(R.id.text_view_age)
        textMbti = findViewById(R.id.text_view_mbti)
        imageView = findViewById(R.id.image_view)
        exitBtn = findViewById(R.id.button_exit)
    }

    private fun setRandomImage() {
        val imageId = listOf(
            R.drawable.garlic,
            R.drawable.onion,
            R.drawable.potato,
            R.drawable.strawberry,
            R.drawable.sweet_potato
        )
        val selectedId = imageId[random.nextInt(0, imageId.size)]
        imageView.setImageResource(selectedId)
    }

    private fun setInfo() {
        val id = intent.getStringExtra(ID)
            ?: throw IllegalStateException("HomeActivity를 호출하는 곳에서 ID를 전달해야 합니다.")
        val name = intent.getStringExtra(NAME)
            ?: throw IllegalStateException("HomeActivity를 호출하는 곳에서 NAME을 전달해야 합니다.")

        textId.text = id
        textName.text = name
        textAge.text = random.nextInt(10, 50).toString()
        textMbti.text = getString(R.string.default_mbti)
    }

    private fun setListener() {
        exitBtn.setOnClickListener {
            finish()
        }
    }
}