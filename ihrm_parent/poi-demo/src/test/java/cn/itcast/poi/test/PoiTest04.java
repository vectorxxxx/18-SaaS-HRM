package cn.itcast.poi.test;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author VectorX
 * @version 1.0.0
 * @description 百万级数据
 * @date 2024/06/07
 */
public class PoiTest04
{

    public static void main(String[] args) throws Exception {
        final long start = System.currentTimeMillis();

        Workbook wb = new XSSFWorkbook("D:\\workspace-mine\\18-SaaS-HRM\\ihrm_parent\\poi-demo\\src\\main\\resources\\test04.xlsx");
        Sheet sheet = wb.getSheetAt(0);
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            StringBuilder sb = new StringBuilder();
            sb
                    .append(rowNum)
                    .append("\t");
            Row row = sheet.getRow(rowNum);
            for (int cellNum = 2; cellNum < row.getLastCellNum(); cellNum++) {
                Cell cell = row.getCell(cellNum);
                Object value = getCellValue(cell);
                sb
                        .append(value)
                        .append("\t");
            }
            System.out.println(sb);
        }

        final long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000.0F + "秒");      // 30.601秒
    }

    public static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                else {
                    return cell.getNumericCellValue();
                }
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }
}
