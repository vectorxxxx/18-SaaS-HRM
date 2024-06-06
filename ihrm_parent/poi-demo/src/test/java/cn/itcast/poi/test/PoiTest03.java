package cn.itcast.poi.test;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 加载Excel
 */
public class PoiTest03
{

    public static void main(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        final XSSFWorkbook wb = new XSSFWorkbook("D:\\workspace-mine\\18-SaaS-HRM\\ihrm_parent\\poi-demo\\src\\main\\resources\\demo.xlsx");
        final XSSFSheet sheet = wb.getSheetAt(0);
        for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
            final XSSFRow row = sheet.getRow(rowNum);
            for (int cellNum = 2; cellNum < row.getLastCellNum(); cellNum++) {
                final XSSFCell cell = row.getCell(cellNum);
                final Object value = getCellValue(cell);
                sb
                        .append(value)
                        .append("\t");
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static Object getCellValue(XSSFCell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getNumericCellValue();
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
