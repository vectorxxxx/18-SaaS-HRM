package cn.itcast.poi.test;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 绘制图形
 */
public class PoiTest02
{

    public static void main(String[] args) throws Exception {
        Workbook wb = new XSSFWorkbook();
        final Sheet sheet = wb.createSheet("test");

        // 获取图片文件输入流
        final FileInputStream stream = new FileInputStream("D:\\workspace-mine\\18-SaaS-HRM\\ihrm_parent\\poi-demo\\src\\main\\resources\\test.jpg");
        final byte[] bytes = IOUtils.toByteArray(stream);
        stream.read(bytes);

        // 设置锚点
        final ClientAnchor anchor = wb
                // 获取创建辅助
                .getCreationHelper()
                // 创建锚点
                .createClientAnchor();
        anchor.setCol1(0); // 列号
        anchor.setRow1(0); // 行号

        // 添加图片（向POI内存中添加，返回图片在图片集合中的索引）
        final int pictureIndex = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);

        // 创建图片
        final Picture picture = sheet
                // 创建绘图元老
                .createDrawingPatriarch()
                // 创建图片
                .createPicture(anchor, pictureIndex);
        picture.resize();   // 自适应缩放

        try (FileOutputStream fos = new FileOutputStream("D:\\workspace-mine\\18-SaaS-HRM\\ihrm_parent\\poi-demo\\src\\main\\resources\\test02.xlsx")) {
            wb.write(fos);
        }
    }
}
