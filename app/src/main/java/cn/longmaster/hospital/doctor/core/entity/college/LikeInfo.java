package cn.longmaster.hospital.doctor.core.entity.college;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by Yang² on 2018/3/27.
 */

public class LikeInfo implements Serializable {
    @JsonField("likes_total")
    private int likesTotal;//点赞总数

    public int getLikesTotal() {
        return likesTotal;
    }

    public void setLikesTotal(int likesTotal) {
        this.likesTotal = likesTotal;
    }

    @Override
    public String toString() {
        return "LikeInfo{" +
                "likesTotal=" + likesTotal +
                '}';
    }
}
