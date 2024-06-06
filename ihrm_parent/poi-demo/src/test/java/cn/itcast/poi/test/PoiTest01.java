package cn.itcast.poi.test;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

/**
 * 使用POI创建excel
 */
public class PoiTest01
{

    public static void main(String[] args) throws Exception {
        Workbook wb = new XSSFWorkbook();
        final Sheet sheet = wb.createSheet();
        final Row row = sheet.createRow(3);
        final Cell cell = row.createCell(0);
        cell.setCellValue("测试XSSF");

        // ========================设置格式========================
        // 设置行高和列宽
        row.setHeightInPoints(50);
        sheet.setColumnWidth(2, 31 * 256);

        final CellStyle style = wb.createCellStyle();

        // 设置边框
        style.setBorderTop(BorderStyle.DASHED);
        style.setBorderRight(BorderStyle.DOTTED);
        style.setBorderBottom(BorderStyle.DOUBLE);
        style.setBorderLeft(BorderStyle.HAIR);

        // 设置字体
        final Font font = wb.createFont();
        font.setFontName("华文行楷");
        font.setFontHeightInPoints((short) 28);
        style.setFont(font);

        // 对齐方式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        cell.setCellStyle(style);

        // 合并单元格
        final CellRangeAddress cellAddresses = new CellRangeAddress(3, 4, 0, 1);
        sheet.addMergedRegion(cellAddresses);
        // ========================设置格式========================

        try (FileOutputStream fos = new FileOutputStream("D:\\workspace-mine\\18-SaaS-HRM\\ihrm_parent\\poi-demo\\src\\main\\resources\\test01.xlsx")) {
            wb.write(fos);
        }
    }
}
