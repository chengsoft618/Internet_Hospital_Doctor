package cn.longmaster.doctorlibrary.util.imageloader;

/**
 * 图片缩放类型
 */
public enum ImageScaleType {
    /**
     * 缩放图片，压缩xy，使图片适应width和width
     */
    FIT_XY("fit_xy"),

    /**
     * 裁剪图片
     */
    CENTER_CROP("center_crop"),

    /**
     * 使图片全部显示
     */
    CENTER_INSIDE("centrt_inside"),

    /**
     * 从顶部开始剪裁
     */
    START_CROP("start_crop");

    private String value;

    private ImageScaleType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

