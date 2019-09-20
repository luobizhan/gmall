package com.lbz.gmall.manage.client;

import com.github.tobato.fastdfs.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author lbz
 * @create 2019-08-21 11:07
 */
@Component
public class FastDfsClient {

    @Autowired
    private FastFileStorageClient storageClient ;

    @Autowired
    private FdfsWebServer fdfsWebServer ;

    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {

        StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);

        return getResAccessUrl(storePath);
    }


    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        return fileUrl;
    }


}
