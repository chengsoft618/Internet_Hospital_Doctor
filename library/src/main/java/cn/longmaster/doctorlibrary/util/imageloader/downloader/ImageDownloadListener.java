package cn.longmaster.doctorlibrary.util.imageloader.downloader;

/**
 * ���ؼ�����
 *
 * @author zdxing 2015��1��26��
 */
public interface ImageDownloadListener {
    /**
     * ��Ȼص�
     *
     * @param totalSize   �ܵ�ֱ������������û�з��ظ�ֵ��Ϊ-1
     * @param currentSize ��ǰ�Ѿ����ص��ֽ���
     */
    public void onProgressChange(String filePath, int totalSize, int currentSize);

    /**
     * ���ؽ��ص�
     */
    public void onDownloadFinish(String filePath, DownloadResult downloadResult);

    /**
     * ���ؽ��
     */
    public static enum DownloadResult {
        /**
         *
         */
        SUCCESSFUL,

        /**
         * ����ʧ��
         */
        FAILED,

        /**
         * URL Ϊ null
         */
        URL_EMPTY,

        /**
         * �ļ��Ѿ�����
         */
        FILE_EXISTS;
    }
}
