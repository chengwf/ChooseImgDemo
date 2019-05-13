package com.chengwf.chooseimg

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.choose_img_activity_preview.*

class PreviewImgActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_img_activity_preview)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS /*or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION*/)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        val path = intent.getStringExtra("path")

        if (path.substring(path.lastIndexOf("."), path.length).toLowerCase().contains("gif")) {
            Glide.with(this).asGif().load(path).into(choose_img_preview)
        } else {
            choose_img_preview.transitionName = "preview"
            Glide.with(this).load(path).into(choose_img_preview)
        }
        choose_img_preview.setOnClickListener {
            ActivityCompat.finishAfterTransition(this)
        }
    }
}