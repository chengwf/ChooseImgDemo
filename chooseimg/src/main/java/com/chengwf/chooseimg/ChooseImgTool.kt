package com.chengwf.chooseimg

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.provider.MediaStore
import android.support.annotation.RequiresPermission
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import com.bumptech.glide.request.RequestOptions
import org.jetbrains.annotations.NotNull

object ChooseImgTool {

    /**
     * intent key:被选中的图片
     */
    const val KEY_SELECT = "select_image_list"
    /**
     * intent key:图片列表的列数
     */
    const val KEY_COLUMN_NUM = "column_num"
    /**
     * 最多可选数目
     */
    const val KEY_SELECT_MOST = "select_most"
    /**
     * requestCode
     */
    const val REQUEST_CODE = 1208


    /**
     * 图片列表的列数
     */
    internal var listColumnCount = 4
    /**
     * 按文件夹归类的ImageInfo
     */
    internal val photoList = ArrayList<ChooseImgData.ImageInfo>()
    /**
     * 被选中的图片path集合
     */
    internal val checkedList = ArrayList<String>()
    /**
     * 可选择的图片的最大数量
     */
    private var mostNum: Int = 1

    /**
     * 取得本地所有图片的路径
     */
    internal fun initPhotoList(context: Context, selectList: ArrayList<String>) {
        photoList.clear()
        val cursor = MediaStore.Images.Media.query(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(
                // image id.
                MediaStore.Images.Media._ID,
                // image absolute path.
                MediaStore.Images.Media.DATA,
                // image name.
                MediaStore.Images.Media.DISPLAY_NAME,
                // The time to be added to the library.
                MediaStore.Images.Media.DATE_ADDED,
                // folder id.
                MediaStore.Images.Media.BUCKET_ID,
                // folder name.
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        )

        var chooseImgData: ChooseImgData.ImageInfo?
        while (cursor.moveToNext()) {
            chooseImgData = ChooseImgData.ImageInfo(
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                    ,
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    ,
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                    ,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
                    ,
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                    ,
                    selectList.contains(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)))
            )

            photoList.add(chooseImgData)
        }
        cursor.close()
    }

    /**
     * 获得所有图片
     */
    fun getPhotoList() = photoList

    /**
     * 获得所有图片信息
     * @param dirName 要过滤的目录名字，只需要文件夹名字，可多个
     * @param context 上下文
     * @return 返回[ChooseImgData.ImageInfo]的ArrayList集合
     */
    @JvmStatic
    fun getPhotoByDirArr(@NotNull dirName: Array<String>, context: Context): List<ChooseImgData.ImageInfo> {

        if (photoList.isNullOrEmpty()) {
            initPhotoList(context, arrayListOf(""))
        }
        return when {
            dirName.isNotEmpty() && photoList.isNotEmpty() -> photoList.filter { dirName.contains(it.dir) }
            else -> photoList
        }
    }

    /**
     * 判断某个目录是否存在于[photoList]中
     */
    private fun isExistsByDir(chooseImgData: ChooseImgData.ImageInfo): Boolean {
        return photoList.any { it.path == chooseImgData.path }
    }


    /**
     * Glide的一些设置
     */
    internal fun getReqOptByGlide(): RequestOptions {
        return RequestOptions().centerCrop()
                .dontAnimate()
                .override(getImageSize(), getImageSize())
                .placeholder(loadingRes)
                .error(loadErrorRes)
    }

    /**
     * 获得ImageView显示的宽高
     * @return 正方形显示，宽高为屏幕宽度除以列表的列数
     */
    private fun getImageSize() =
            Resources.getSystem().displayMetrics.widthPixels / listColumnCount

    /**
     * 设置图片列表列数
     */
    fun setColumnNum(columns: Int): ChooseImgTool {
        listColumnCount = columns
        return this
    }

    fun setChooseList(list: ArrayList<String>): ChooseImgTool {
        checkedList.addAll(list)
        return this
    }

    fun setChooseMost(mostNum: Int): ChooseImgTool {
        this.mostNum = mostNum
        return this
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun start(context: Activity) {
        val intent = Intent(context, ChooseImgActivity::class.java).apply {
            putExtra(KEY_COLUMN_NUM, 3)
            putExtra(KEY_SELECT_MOST, 9)
        }
        context.startActivityForResult(intent, REQUEST_CODE)
    }

    @JvmStatic
    fun previewImg(view: View, path: String) {

        val intent = Intent(view.context, PreviewImgActivity::class.java).apply {
            putExtra("path", path)
        }
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                view.context as Activity,
                view,
                "preview"
        ).toBundle()

        view.context.startActivity(intent, options)
    }

    private var loadingRes = R.mipmap.choose_img_loading
    private var loadErrorRes = R.mipmap.choose_img_load_error

    /**
     * 列表图片的占位图
     */
    fun setLoadingRes(loadingRes: Int): ChooseImgTool {
        this.loadingRes = loadingRes
        return this
    }

    /**
     * 列表图片加载失败时的图片
     */
    fun setLoadErrorRes(loadErrorRes: Int): ChooseImgTool {
        this.loadErrorRes = loadErrorRes
        return this
    }

    internal var checkedRes = R.mipmap.choose_img_select
    internal var unCheckedRes = R.mipmap.choose_img_un_select

    /**
     * 图片的选中的状态
     */
    fun setCheckedRes(checkedRes: Int): ChooseImgTool {
        this.checkedRes = checkedRes
        return this
    }

    /**
     * 图片未选中的状态
     */
    fun setUnCheckedRes(unCheckedRes: Int): ChooseImgTool {
        this.unCheckedRes = unCheckedRes
        return this
    }
}