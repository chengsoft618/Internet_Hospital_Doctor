package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/3/18.
 */

public class ProjectCourseTypeInfo implements Serializable {
    @JsonField("course_type")
    private String courseType;//课程类型
    @JsonField("course_list")
    private List<CourseContentInfo> courseContentInfos;//课程内容

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public List<CourseContentInfo> getCourseContentInfos() {
        return courseContentInfos;
    }

    public void setCourseContentInfos(List<CourseContentInfo> courseContentInfos) {
        this.courseContentInfos = courseContentInfos;
    }

    @Override
    public String toString() {
        return "ProjectCourseTypeInfo{" +
                "courseType='" + courseType + '\'' +
                ", courseContentInfos=" + courseContentInfos +
                '}';
    }
}
