package com.chengwf.chooseimg

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chengwf.chooseimg.ChooseImgTool
import com.chengwf.chooseimg.R
import com.chengwf.chooseimg.ChooseImgData

internal class ChooseImgDialogAdapter(layoutResId: Int, chooseImgData: List<ChooseImgData.ImageDir>) :
    BaseQuickAdapter<ChooseImgData.ImageDir, BaseViewHolder>(layoutResId, chooseImgData) {
    override fun convert(helper: BaseViewHolder, item: ChooseImgData.ImageDir) {
        helper.setText(R.id.choose_img_dialog_dir, item.dir)
            .setText(R.id.choose_img_dialog_count, "${item.photoList.size} 张")

        Glide.with(helper.itemView)
            .setDefaultRequestOptions(ChooseImgTool.getReqOptByGlide())
            .load(item.photoPath)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(0.5f)
            .into(helper.itemView.findViewById(R.id.choose_img_dialog_img_pre))
    }
}