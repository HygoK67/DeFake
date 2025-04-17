package com.group6.defakelogibackend.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class TencentCOSService {

    @Value("${tencent-cloud.cos.secretId}")
    private String secretId;
    @Value("${tencent-cloud.cos.secretKey}")
    private String secretKey;
    @Value("${tencent-cloud.cos.bucketId}")
    private String bucketId;
    @Value("${tencent-cloud.cos.regionName}")
    private String regionName;
    private COSCredentials cred;
    private ClientConfig clientConfig;
    private COSClient cosClient;

    @PostConstruct
    public void init() {
        this.cred = new BasicCOSCredentials(this.secretId, this.secretKey);
        this.clientConfig = new ClientConfig(new Region(regionName));
        this.clientConfig.setHttpProtocol(HttpProtocol.https);
        this.cosClient = new COSClient(this.cred, this.clientConfig);
    }

    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        System.out.println("[file] 用户上传了 " + originalFilename);
        int index = originalFilename.lastIndexOf(".");
        String extensionName = index == -1 ? "" : "." + originalFilename.substring(index + 1);
        String key = UUID.randomUUID() + extensionName;
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/" + key);
        file.transferTo(tmpFile);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketId, key, tmpFile);
        PutObjectResult result = cosClient.putObject(putObjectRequest);
        String itemURL = "https://" + bucketId + ".cos." + regionName + ".myqcloud.com/" + key;
        return itemURL;
    }

    public String upload(File file) throws IOException {
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        String extensionName = index == -1 ? "" : "." + fileName.substring(index + 1);
        String key = UUID.randomUUID() + extensionName;
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/" + key);
        FileUtils.copyFile(file, tmpFile);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketId, key, tmpFile);
        PutObjectResult result = cosClient.putObject(putObjectRequest);
        String itemURL = "https://" + bucketId + ".cos." + regionName + ".myqcloud.com/" + key;
        return itemURL;
    }

}