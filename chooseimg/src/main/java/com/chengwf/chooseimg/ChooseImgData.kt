package com.chengwf.chooseimg

class ChooseImgData {
    /**
     * 图片信息
     *
     * @param id 图片id
     * @param path 图片路径
     * @param name 文件名
     * @param bucketId 所属文件夹id
     * @param dir 文件夹名字
     * @param isSelect 图片是否被选中
     */
    data class ImageInfo(
        val id: Int,
        val path: String,
        val name: String,
        val bucketId: Long,
        val dir: String,
        var isSelect: Boolean
    )
}