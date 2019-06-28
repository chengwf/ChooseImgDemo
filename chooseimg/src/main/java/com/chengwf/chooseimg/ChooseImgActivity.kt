package com.chengwf.chooseimg

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.choose_img_activity.*

internal class ChooseImgActivity : AppCompatActivity() {

    /**
     * 适配器
     */
    private lateinit var mAdapter: ChooseImgAdapter

    /**
     * 筛选menu
     */
    private lateinit var filterMenu: MenuItem
    private lateinit var sureMenu: MenuItem
    /**
     * 选择图片的对话框
     */
    private lateinit var dialog: BottomSheetDialog
    /**
     * 所有图片地址
     */
    private val mData = ArrayList<ChooseImgData.ImageInfo>()
    /**
     * 被选中的图片
     */
    /**
     * 最多可选数量
     */
    private var selectMost = 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_img_activity)
        setSupportActionBar(choose_img_toolbar)
        choose_img_toolbar.title = "图片列表"
        choose_img_toolbar.setNavigationOnClickListener {
            ChooseImgTool.checkedList.clear()
            finish()
        }

        // 列表的列数
        ChooseImgTool.listColumnCount = intent.getIntExtra(ChooseImgTool.KEY_COLUMN_NUM, 3)
        // 最多可选数目
        selectMost = intent.getIntExtra(ChooseImgTool.KEY_SELECT_MOST, 9)

        initRecyclerView()
        initDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.choose_img_menu_img_list, menu)
        filterMenu = menu.findItem(R.id.choose_img_menu_filter)
        sureMenu = menu.findItem(R.id.choose_img_menu_sure)
        if (ChooseImgTool.checkedList.size > 0) sureMenu.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.choose_img_menu_filter -> {
                if (dialog.isShowing)
                    dialog.dismiss()
                else dialog.show()
            }
            R.id.choose_img_menu_sure -> {
                setResult(ChooseImgTool.REQUEST_CODE, Intent().putStringArrayListExtra(ChooseImgTool.KEY_SELECT, ChooseImgTool.checkedList))
                finish()
            }
        }
        return true
    }

    private fun initRecyclerView() {
        choose_img_list.layoutManager =
                GridLayoutManager(this@ChooseImgActivity, ChooseImgTool.listColumnCount)

        // 已选择的图片
        if (intent.hasExtra(ChooseImgTool.KEY_SELECT)) {
            val list = intent.getStringArrayListExtra(ChooseImgTool.KEY_SELECT)
            ChooseImgTool.checkedList.addAll(list)

        }
        // 初始化图片list
        ChooseImgTool.initPhotoList(applicationContext,ChooseImgTool.checkedList)
        // 设置副标题
        choose_img_toolbar.subtitle = "${ChooseImgTool.checkedList.size}/$selectMost"
        mData.addAll(ChooseImgTool.getPhotoList())

        mAdapter = ChooseImgAdapter(R.layout.choose_img_adapter_list, mData)
        choose_img_list.adapter = mAdapter
        mAdapter.setOnItemChildClickListener { _, view, position ->

            when (view.id) {
                R.id.choose_img_adapter_select -> {

                    if (!mData[position].isSelect && ChooseImgTool.checkedList.size >= selectMost) return@setOnItemChildClickListener
                    mData[position].isSelect = !mData[position].isSelect

                    addSelectedImg(mData[position].isSelect, mData[position].path)
                    sureMenu.isVisible = ChooseImgTool.checkedList.size > 0
                    mAdapter.notifyItemChanged(position)
                }
                R.id.choose_img_adapter_img_list ->
                    ChooseImgTool.previewImg(view, mData[position].path)
            }
        }
    }

    /**
     * 图片目录的选择
     */
    private fun initDialog() {
        val contentView =
                View.inflate(this@ChooseImgActivity, R.layout.choose_img_dialog, null)
        dialog = BottomSheetDialog(this@ChooseImgActivity)

        val review = contentView.findViewById<RecyclerView>(R.id.choose_img_dialog_review)
        review.layoutManager = LinearLayoutManager(this@ChooseImgActivity)

        val adapter =
                ChooseImgDialogAdapter(R.layout.choose_img_adapter_dialog, ChooseImgTool.getPhotoList().distinctBy { it.dir })
        review.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->
            dialog.dismiss()

            mAdapter.setDirImg(ChooseImgTool.getPhotoByDirArr(arrayOf(ChooseImgTool.getPhotoList().distinctBy { it.dir }[position].dir), applicationContext))

            filterMenu.title = ChooseImgTool.getPhotoList().distinctBy { it.dir }[position].dir
        }
        dialog.setContentView(contentView)
    }

    private fun addSelectedImg(isAdd: Boolean = true, img: String) {

        if (isAdd) {
            if (!ChooseImgTool.checkedList.contains(img)) ChooseImgTool.checkedList.add(img)
        } else {
            ChooseImgTool.checkedList.remove(img)
        }
        Log.d("TAG", "是否选中 ===== $isAdd")
        choose_img_toolbar.subtitle = "${ChooseImgTool.checkedList.size}/$selectMost"
    }
}