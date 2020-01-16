package cn.longmaster.doctorlibrary.util.photo;


public interface OnItemCheckListener {
    /**
     * @param position 所选图片的位置
     * @param photo    所选的图片
     * @param isCheck  当前状态
     * @return enable check
     */
    boolean OnItemCheck(int position, Photo photo, boolean isCheck);
}
