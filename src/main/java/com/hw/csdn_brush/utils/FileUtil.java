package com.hw.csdn_brush.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {
    public FileUtil() {
        super();
    }
    
    /**
     * 从本地文件读取报文
     */
    public static String getFileContent(String filePath){
        System.out.println("filePath: " + filePath);
        File file = new File(filePath);
        FileInputStream fis;
        String str = "";
        try {
            fis = new FileInputStream(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis, "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                    buffer.append(line);
            }
            str = buffer.toString();
            fis.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
