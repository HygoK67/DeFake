package com.group6.defakelogibackend.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.springframework.stereotype.Component;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class PDFService {

    public List<File> extractImages(File pdfFile) throws IOException {

        String outputFolder = System.getProperty("java.io.tmpdir");
        List<File> imageList = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            for (PDPage page : document.getPages()) {
                PDResources resources = page.getResources();
                if (resources == null) continue;

                for (COSName cosName : resources.getXObjectNames()) {
                    PDXObject xobj = resources.getXObject(cosName);
                    if (xobj instanceof PDImageXObject pdImageXObject) {
                        BufferedImage bImage = pdImageXObject.getImage();
                        File outputFile = new File(outputFolder, UUID.randomUUID() + ".png");
                        ImageIO.write(bImage, "PNG", outputFile);
                        imageList.add(outputFile);
                    }
                }
            }
        }
        return imageList;
    }

    public int getPageNumber(File pdfFile) throws IOException {
        PDDocument document = Loader.loadPDF(pdfFile);
        return document.getNumberOfPages();
    }

}
