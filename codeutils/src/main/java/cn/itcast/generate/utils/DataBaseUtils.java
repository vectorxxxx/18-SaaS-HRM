package cn.itcast.generate.utils;

import cn.itcast.generate.entity.Column;
import cn.itcast.generate.entity.DataBase;
import cn.itcast.generate.entity.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataBaseUtils
{

    //获取到mysql中所有的数据库名称

    //获取数据库连接
    public static Connection getConnection(DataBase db) throws Exception {
        Properties props = new Properties();
        props.put("remarksReporting", "true");//获取数据库的备注信息
        props.put("user", db.getUserName());
        props.put("password", db.getPassWord());
        Class.forName(db.getDriver());//注册驱动
        return DriverManager.getConnection(db.getUrl(), props);
    }

    //获取数据库列表
    public static List<String> getSchemas(DataBase db) throws Exception {
        List<String> list;
        try (Connection connection = getConnection(db)) {
            //1.获取元数据
            DatabaseMetaData metaData = connection.getMetaData();
            //2.获取所有数据库列表
            try (ResultSet rs = metaData.getCatalogs()) {
                list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getString(1));
                }
            }
        }
        return list;
    }

    /**
     * 获取数据库中的表和字段构造实体类 Table对象
     * <p>
     * 1.参数 DataBase 数据库对象 2.操作步骤 1.获取连接 2.获取databasemetaData 3.获取当前数据库中的所有表 4.获取每个表中的所有字段 5.封装到java对象中即可
     */
    public static List<Table> getDbInfo(DataBase db) throws Exception {
        //1.获取连接
        List<Table> list = new ArrayList<>();
        try (Connection connection = getConnection(db)) {
            //2.获取元数据
            DatabaseMetaData metaData = connection.getMetaData();
            //3.获取当前数据库中的所有表
            try (ResultSet tables = metaData.getTables(null, null, "pe_permission", new String[] {"TABLE"})) {
                while (tables.next()) {
                    Table tab = new Table();
                    //i.表名
                    String tableName = tables.getString("TABLE_NAME"); //bs_user  User
                    //ii.类名
                    String className = removePrefix(tableName);
                    //iii.描述
                    String remarks = tables.getString("REMARKS");

                    //iiii.主键
                    StringBuilder keys;
                    try (ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName)) {
                        keys = new StringBuilder();
                        while (primaryKeys.next()) {
                            String keyname = primaryKeys.getString("COLUMN_NAME");
                            keys
                                    .append(keyname)
                                    .append(",");
                        }
                        tab.setName(tableName);
                        tab.setName2(className);
                        tab.setComment(remarks);
                        tab.setKey(keys.toString());
                    }

                    //处理表中的所有字段
                    List<Column> columnList = new ArrayList<>();
                    try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                        while (columns.next()) {
                            //构造Column对象
                            Column cn = new Column();
                            final String columnName = columns.getString("COLUMN_NAME"); //user_id  userId , create_time createTime
                            final String dbType = columns.getString("TYPE_NAME");//VARCHAR,DATETIME
                            final String remarks1 = columns.getString("REMARKS");//VARCHAR,DATETIME
                            //列名称
                            cn.setColumnName(columnName);
                            //属性名
                            cn.setColumnName2(StringUtils.toJavaVariableName(columnName));
                            //java类型和数据库类型
                            cn.setColumnDbType(dbType);
                            cn.setColumnType(PropertiesUtils.customMap.get(dbType));
                            //备注
                            cn.setColumnComment(remarks1);
                            //是否主键
                            String pri = null;
                            if (StringUtils.contains(columnName, keys
                                    .toString()
                                    .split(","))) {
                                pri = "PRI";
                            }
                            cn.setColumnKey(pri);
                            columnList.add(cn);
                        }
                    }
                    tab.setColumns(columnList);
                    list.add(tab);
                }
            }
        }
        return list;
    }

    public static String removePrefix(String tableName) {
        String prefix = PropertiesUtils.customMap.get("tableRemovePrefixes");
        //bs_,     tb_    , co_    ,
        String temp = tableName;  //bs_user
        for (String pf : prefix.split(",")) {
            temp = StringUtils.removePrefix(temp, pf, true);
        }
        //temp = user
        return StringUtils.makeAllWordFirstLetterUpperCase(temp);
    }

    public static void main(String[] args) throws Exception {
        DataBase db = new DataBase("MYSQL", "ihrm");
        db.setUserName("root");
        db.setPassWord("111111");

        List<Table> dbInfo = DataBaseUtils.getDbInfo(db);
        for (Table table : dbInfo) {
            List<Column> columns = table.getColumns();
            for (Column column : columns) {
                System.out.println(column);
            }
        }
    }
}
