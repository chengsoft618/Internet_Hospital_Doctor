package cn.longmaster.doctorlibrary.util.fileloader.downloader;

/**
 * 文件下载参数
 * Created by JinKe on 2016-11-21.
 */
public class FileLoadOptions {
    private String url;
    private String filePath;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean hasUrl() {
        if (url == null || "".equals(url.trim())) {
            return false;
        } else {
            return true;
        }
    }

    public static class Builder {
        private FileLoadOptions mFileLoadOptions;

        public Builder(String filePath, String url) {
            mFileLoadOptions = new FileLoadOptions();
            mFileLoadOptions.filePath = filePath;
            mFileLoadOptions.url = url;
        }

        public Builder(String filePath) {
            this(filePath, "");
        }

        /**
         * 创建图片加载参数
         */
        public FileLoadOptions build() {
            if (mFileLoadOptions.filePath == null || "".equals(mFileLoadOptions.filePath.trim())) {
                throw new IllegalArgumentException("文件下载，必须设置filePath");
            }

            return mFileLoadOptions;
        }

        /**
         * 设置图片网络地址
         */
        public Builder setUrl(String url) {
            if (url == null) {
                throw new NullPointerException("文件下载，url == null");
            }
            setUrl(url);
            return this;
        }

        /**
         * 设置图片下载后的本地路径
         */
        public Builder setFilePath(String filePath) {
            if (filePath == null) {
                throw new NullPointerException("图片加载，filePath == null");
            }
            setFilePath(filePath);
            return this;
        }
    }
}
