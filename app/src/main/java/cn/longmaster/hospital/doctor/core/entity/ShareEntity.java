package cn.longmaster.hospital.doctor.core.entity;

/**
 * 分享实体类
 * Created by Yang² on 2017/11/30.
 */

public class ShareEntity {
    private String title;
    private String content;
    private String url;
    private String path;

    private String imgUrl;
    private int drawableId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    @Override
    public String toString() {
        return "ShareEntity{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", drawableId=" + drawableId +
                '}';
    }
}
