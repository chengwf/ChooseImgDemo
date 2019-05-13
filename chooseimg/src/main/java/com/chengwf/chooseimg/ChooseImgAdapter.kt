package com.chengwf.chooseimg

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chengwf.chooseimg.ChooseImgTool
import com.chengwf.chooseimg.R
import com.chengwf.chooseimg.ChooseImgData

internal class ChooseImgAdapter(layoutResId: Int, chooseImgData: List<ChooseImgData.ImageInfo>) :
    BaseQuickAdapter<ChooseImgData.ImageInfo, BaseViewHolder>(layoutResId, chooseImgData) {

    override fun convert(helper: BaseViewHolder, item: ChooseImgData.ImageInfo) {

        Glide.with(helper.itemView)
            .setDefaultRequestOptions(ChooseImgTool.getReqOptByGlide())
            .load(item.path)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .thumbnail(0.5f)
            .into(helper.itemView.findViewById(R.id.choose_img_adapter_img_list))

        helper.addOnClickListener(R.id.choose_img_adapter_img_list)
            .addOnClickListener(R.id.choose_img_adapter_select)
            .setImageResource(
                R.id.choose_img_adapter_select,
                if (item.isSelect) ChooseImgTool.checkedRes else ChooseImgTool.unCheckedRes
            )
    }

    fun setDirImg(imgList: ArrayList<ChooseImgData.ImageInfo>) {
        data.clear()
        data.addAll(imgList)
        notifyDataSetChanged()
    }
}