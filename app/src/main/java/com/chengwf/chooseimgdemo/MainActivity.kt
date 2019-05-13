package com.chengwf.chooseimgdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chengwf.chooseimg.ChooseImgTool
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val selectList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)

        bn_start.setOnClickListener {
            if (checkCallingPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                ChooseImgTool.setChooseList(selectList)
                        .setChooseMost(9)
                        .setColumnNum(3)
                        .start(this@MainActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        selectList.clear()
        tv_show.text = ""

        data?.let {
            for (path in data.getStringArrayListExtra(ChooseImgTool.KEY_SELECT)) {
                tv_show.append("\t\t\t")
                tv_show.append(path)
                tv_show.append("\n\n")
            }
            selectList.addAll(data.getStringArrayListExtra(ChooseImgTool.KEY_SELECT))
        }
    }
}
