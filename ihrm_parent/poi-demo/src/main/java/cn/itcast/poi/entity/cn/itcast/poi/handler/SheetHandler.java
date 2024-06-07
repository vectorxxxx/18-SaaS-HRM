package cn.itcast.poi.entity.cn.itcast.poi.handler;

import cn.itcast.poi.entity.PoiEntity;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

/**
 * 自定义的事件处理器 处理每一行数据读取 实现接口
 */
public class SheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler
{
    private PoiEntity entity;

    @Override
    public void startRow(int i) {
        if (i > 0) {
            entity = new PoiEntity();
        }
    }

    @Override
    public void endRow(int i) {
        System.out.println(entity);
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        if (entity != null) {
            switch (cellReference.substring(0, 1)) {
                case "A":
                    entity.setId(formattedValue);
                    break;
                case "B":
                    entity.setBreast(formattedValue);
                    break;
                case "C":
                    entity.setAdipocytes(formattedValue);
                    break;
                case "D":
                    entity.setNegative(formattedValue);
                    break;
                case "E":
                    entity.setStaining(formattedValue);
                    break;
                case "F":
                    entity.setSupportive(formattedValue);
                    break;
                default:
                    break;
            }
        }
    }

}
