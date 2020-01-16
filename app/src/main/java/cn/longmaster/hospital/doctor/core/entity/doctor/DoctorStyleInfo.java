package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.utils.LibCollections;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 16:22
 * @description:
 */
public class DoctorStyleInfo {

    @JsonField("article_data")
    private List<ArticleDataBean> articleData;
    @JsonField("video_data")
    private List<VideoDataBean> videoData;
    @JsonField("material_data")
    private List<MaterialDataBean> materialData;

    public List<ArticleDataBean> getArticleData() {
        return articleData;
    }

    public void setArticleData(List<ArticleDataBean> articleData) {
        this.articleData = articleData;
    }

    public List<VideoDataBean> getVideoData() {
        return videoData;
    }

    public void setVideoData(List<VideoDataBean> videoData) {
        this.videoData = videoData;
    }

    public List<MaterialDataBean> getMaterialData() {
        return materialData;
    }

    public void setMaterialData(List<MaterialDataBean> materialData) {
        this.materialData = materialData;
    }

    public static class ArticleDataBean {
        /**
         * id : 118
         * doctor_id : 1004859
         * data_type : 2
         * content : 234
         * url_link : http://test-dbws.39hospital.com/Home/Doctor/doctor_edit?docid=1003974
         * insert_dt : 2017-06-13 16:56:07
         */

        @JsonField("id")
        private String id;
        @JsonField("doctor_id")
        private String doctorId;
        @JsonField("data_type")
        private String dataType;
        @JsonField("content")
        private String content;
        @JsonField("url_link")
        private String urlLink;
        @JsonField("insert_dt")
        private String insertDt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrlLink() {
            return urlLink;
        }

        public void setUrlLink(String urlLink) {
            this.urlLink = urlLink;
        }

        public String getInsertDt() {
            return insertDt;
        }

        public void setInsertDt(String insertDt) {
            this.insertDt = insertDt;
        }
    }

    public static class VideoDataBean {
        /**
         * id : 117
         * doctor_id : 1004859
         * data_type : 3
         * content : 123
         * url_link : http://test-dbws.39hospital.com/Home/Doctor/doctor_edit?docid=1003974
         * insert_dt : 2017-06-13 16:56:08
         */

        @JsonField("id")
        private String id;
        @JsonField("doctor_id")
        private String doctorId;
        @JsonField("data_type")
        private String dataType;
        @JsonField("content")
        private String content;
        @JsonField("url_link")
        private String urlLink;
        @JsonField("insert_dt")
        private String insertDt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrlLink() {
            return urlLink;
        }

        public void setUrlLink(String urlLink) {
            this.urlLink = urlLink;
        }

        public String getInsertDt() {
            return insertDt;
        }

        public void setInsertDt(String insertDt) {
            this.insertDt = insertDt;
        }
    }

    public static class MaterialDataBean {
        /**
         * material_name : 测试
         * constitutor :
         * release_dt : 2018-08-09 14:28:20
         * pic_url : http://test-dfs.39hospital.com/3021/0/
         * content_url : http://127.0.0.1/39hosp-web/ppt2h5/?path_name=4vGyQ6df&info_name=index
         */

        @JsonField("material_name")
        private String materialName;
        @JsonField("constitutor")
        private String constitutor;
        @JsonField("release_dt")
        private String releaseDt;
        @JsonField("pic_url")
        private String picUrl;
        @JsonField("content_url")
        private String contentUrl;
        //可看状态0可见，1不可见
        @JsonField("self_visible")
        private int selfVisible;
        @JsonField("upload_user_id")
        private int uploadUserId;

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public String getConstitutor() {
            return constitutor;
        }

        public void setConstitutor(String constitutor) {
            this.constitutor = constitutor;
        }

        public String getReleaseDt() {
            return releaseDt;
        }

        public void setReleaseDt(String releaseDt) {
            this.releaseDt = releaseDt;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public boolean isSelfVisible() {
            return selfVisible == 0;
        }

        public int getSelfVisible() {
            return selfVisible;
        }

        public void setSelfVisible(int selfVisible) {
            this.selfVisible = selfVisible;
        }

        public int getUploadUserId() {
            return uploadUserId;
        }

        public void setUploadUserId(int uploadUserId) {
            this.uploadUserId = uploadUserId;
        }
    }

    public boolean isEmpty() {
        return LibCollections.isEmpty(articleData) && LibCollections.isEmpty(videoData) && LibCollections.isEmpty(materialData);
    }
}
