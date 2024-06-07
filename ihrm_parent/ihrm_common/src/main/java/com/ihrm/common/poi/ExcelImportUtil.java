package com.ihrm.common.poi;

import com.ihrm.domain.poi.ExcelAttribute;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-07 10:25:25
 */
public class ExcelImportUtil<T>
{
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Class<T> clazz;
    private Field[] fields;

    public ExcelImportUtil(Class<T> clazz) {
        this.clazz = clazz;
        fields = clazz.getDeclaredFields();
    }

    /**
     * 读取 Excel
     *
     * @param is        输入流
     * @param rowIndex  行索引
     * @param cellIndex 单元格索引
     * @return {@link List }<{@link T }>
     */
    public List<T> readExcel(InputStream is, int rowIndex, int cellIndex) {
        List<T> list = new ArrayList<>();
        try {
            final Workbook wb = XSSFWorkbookFactory.createWorkbook(is);
            final Sheet sheet = wb.getSheetAt(0);

            for (int i = rowIndex; i < sheet.getLastRowNum(); i++) {
                final T entity = clazz.newInstance();
                final Row row = sheet.getRow(i);
                for (int j = cellIndex; j < row.getLastCellNum(); j++) {
                    final Cell cell = row.getCell(j);
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(ExcelAttribute.class)) {
                            field.setAccessible(true);
                            final ExcelAttribute annotation = field.getAnnotation(ExcelAttribute.class);
                            if (j == annotation.index()) {
                                field.set(entity, convertAttrType(field, cell));
                            }
                        }
                    }
                }
                list.add(entity);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Object convertAttrType(Field field, Cell cell) throws Exception {
        final String fieldType = field
                .getType()
                .getSimpleName();

        switch (fieldType) {
            case "String":
                return getValue(cell);
            case "Date":
                return new SimpleDateFormat(DATE_FORMAT).parse(getValue(cell));
            case "int":
            case "Integer":
                return Integer.parseInt(getValue(cell));
            case "double":
            case "Double":
                return Double.parseDouble(getValue(cell));
            default:
                return null;
        }
    }

    private String getValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell
                        .getRichStringCellValue()
                        .getString()
                        .trim();
            case BOOLEAN:
                return String
                        .valueOf(cell.getBooleanCellValue())
                        .trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    final Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                    return new SimpleDateFormat(DATE_FORMAT).format(date);
                }
                else {
                    // 防止科学计数法
                    final String number = BigDecimal
                            .valueOf(cell.getNumericCellValue())
                            .toPlainString();
                    // 去除浮点型后面的 .0
                    return number.endsWith(".0") ?
                           number.substring(0, number.lastIndexOf(".")) :
                           number;
                }
            default:
                return "";
        }
    }
}
