package com.group6.defakelogibackend.utils;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

@Component
public class ImageService {

    public String downloadAndGenerateThumbnail(String imagePath, int width, int height) throws IOException {
        // 1. 从网络下载图片
        URL url = new URL(imagePath);
        InputStream inputStream = url.openStream();
        BufferedImage originalImage =  ImageIO.read(inputStream);

        if (originalImage == null) {
            throw new IOException();
        }

        // 2. 生成缩略图
        Image thumbnail = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedThumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedThumbnail.createGraphics();
        g2d.drawImage(thumbnail, 0, 0, null);
        g2d.dispose();

        // 3. 将缩略图转换为字节数组
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedThumbnail, "png", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // 4. 将字节数组编码为 Base64 字符串
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // 返回 Base64 编码的字符串
        return base64Image;
    }

}