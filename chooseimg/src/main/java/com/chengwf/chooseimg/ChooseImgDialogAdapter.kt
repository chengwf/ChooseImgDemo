package com.chengwf.chooseimg

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

internal class ChooseImgDialogAdapter(layoutResId: Int, chooseImgData: List<ChooseImgData.ImageInfo>) :
        BaseQuickAdapter<ChooseImgData.ImageInfo, BaseViewHolder>(layoutResId, chooseImgData) {
    override fun convert(helper: BaseViewHolder, item: ChooseImgData.ImageInfo) {
        helper.setText(R.id.choose_img_dialog_dir, item.dir)
                .setText(R.id.choose_img_dialog_count, "${ChooseImgTool.photoList.filter { it.dir == item.dir }.size} 张")

        Glide.with(helper.itemView)
                .setDefaultRequestOptions(ChooseImgTool.getReqOptByGlide())
                .load(item.path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .into(helper.itemView.findViewById(R.id.choose_img_dialog_img_pre))
    }
}