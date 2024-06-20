package com.ihrm.system.utils;

import com.baidu.aip.util.Base64Util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-20 09:55:57
 */
@Component
public class QRCodeUtil
{
    public String createQRCode(String content) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, 200, 200);
            final BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, "png", baos);
            return "data:image/png;base64,".concat(Base64Util.encode(baos.toByteArray()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
