package com.ihrm.common.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-06 18:52:50
 */
public class DownloadUtils
{
    /**
     * 下载
     *
     * @param byteArrayOutputStream 字节数组输出流
     * @param response              响应
     * @param fileName              文件名
     * @throws IOException io异常
     */
    public void download(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response, String fileName) throws IOException {
        fileName = response.encodeURL(new String(fileName.getBytes(StandardCharsets.UTF_8)));
        response.setContentType("application/octet-stream");
        response.setContentLength(byteArrayOutputStream.size());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        final ServletOutputStream outputStream = response.getOutputStream();
        byteArrayOutputStream.writeTo(outputStream);
        byteArrayOutputStream.close();
        outputStream.flush();
    }
}
