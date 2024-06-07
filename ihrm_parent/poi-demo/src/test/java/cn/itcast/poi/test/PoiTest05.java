package cn.itcast.poi.test;

import cn.itcast.poi.entity.cn.itcast.poi.handler.SheetHandler;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.Iterator;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-07 13:52:39
 */
public class PoiTest05
{
    public static void main(String[] args) throws Exception {
        final long start = System.currentTimeMillis();

        String path = "D:\\workspace-mine\\18-SaaS-HRM\\ihrm_parent\\poi-demo\\src\\main\\resources\\test04.xlsx";

        // 1、创建 OPCPackage 对象
        final OPCPackage pck = OPCPackage.open(path);

        // 2、创建 XSSFReader 对象
        final XSSFReader reader = new XSSFReader(pck);
        // 获取 stringsTable
        final SharedStringsTable stringsTable = reader.getSharedStringsTable();
        // 获取 stylesTable
        final StylesTable stylesTable = reader.getStylesTable();

        // 3、创建 XMLReader 对象
        final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler(new XSSFSheetXMLHandler(stylesTable, stringsTable, new SheetHandler(), false));

        // 4、逐行读取
        final Iterator<InputStream> sheetIterator = reader.getSheetsData();
        while (sheetIterator.hasNext()) {
            try (InputStream stream = sheetIterator.next()) {
                xmlReader.parse(new InputSource(stream));
            }
        }

        final long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000.0F + "秒");  // 9.349秒
    }
}
