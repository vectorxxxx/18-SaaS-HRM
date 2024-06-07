package com.ihrm.common.poi;

import com.ihrm.domain.poi.ExcelAttribute;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-07 09:45:26
 */
@Getter
@Setter
public class ExcelExportUtil<T>
{
    /**
     * 行索引
     */
    private int rowIndex;
    /**
     * 样式所在的行索引
     */
    private int styleIndex;
    /**
     * 类型
     */
    private Class<T> clazz;
    /**
     * 字段
     */
    private Field[] fields;

    public ExcelExportUtil(Class<T> clazz, int rowIndex, int styleIndex) {
        this.rowIndex = rowIndex;
        this.styleIndex = styleIndex;
        this.clazz = clazz;
        fields = clazz.getDeclaredFields();
    }

    /**
     * 写入Excel
     *
     * @param response 响应
     * @param is       输入流
     * @param objs     数据集合
     * @param fileName 文件名
     */
    public void writeExcel(HttpServletResponse response, InputStream is, List<T> objs, String fileName) {
        try {
            // Workbook wb = XSSFWorkbookFactory.createWorkbook(is);
            Workbook wb = new SXSSFWorkbook(XSSFWorkbookFactory.createWorkbook(is));    // 百万级数据
            final Sheet sheet = wb.getSheetAt(0);

            // 获取指定行的样式Map
            final Map<Integer, CellStyle> styleMap = getCellStyleMap(sheet, objs);

            // 遍历数据
            for (T t : objs) {
                final Row row = sheet.createRow(rowIndex++);
                // 遍历单元格（这里遍历样式）
                for (Map.Entry<Integer, CellStyle> entry : styleMap.entrySet()) {
                    final Integer index = entry.getKey();
                    final CellStyle cellStyle = entry.getValue();

                    // 创建单元格
                    final Cell cell = row.createCell(index);
                    cell.setCellStyle(cellStyle);

                    // 遍历字段
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(ExcelAttribute.class)) {
                            field.setAccessible(true);
                            final ExcelAttribute annotation = field.getAnnotation(ExcelAttribute.class);
                            if (index == annotation.index() && field.get(t) != null) {
                                cell.setCellValue(field
                                        .get(t)
                                        .toString());
                            }
                        }
                    }
                }
            }

            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=".concat(new String(fileName.getBytes(StandardCharsets.ISO_8859_1))));
            response.setHeader("filename", fileName);
            wb.write(response.getOutputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, CellStyle> getCellStyleMap(Sheet sheet, List<T> objs) {
        final Row styleRow = sheet.getRow(styleIndex);
        return IntStream
                .range(0, objs.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), cellNum -> styleRow
                        .getCell(cellNum)
                        .getCellStyle()));
    }
}
