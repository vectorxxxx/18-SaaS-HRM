package cn.itcast.metadata.test;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 测试数据库元数据
 */
public class DataBaseMetaDataTest
{

    private Connection conn;

    @Before
    public void init() throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/ihrm?useUnicode=true&characterEncoding=utf8";
        String username = "root";
        String password = "root";

        // 1、注册驱动
        Class.forName(driver);

        // 2、设置连接属性
        final Properties props = new Properties();
        props.setProperty("remarksReporting", "true");
        props.setProperty("user", username);
        props.setProperty("password", password);
        conn = DriverManager.getConnection(url, props);
    }

    //获取数据库基本信息
    @Test
    public void test01DatabaseMetaData() throws Exception {
        final DatabaseMetaData metaData = conn.getMetaData();
        // 获取数据库名称
        System.out.println(metaData.getDatabaseProductName());
        // 获取数据库版本
        System.out.println(metaData.getDatabaseProductVersion());
        // 获取数据库用户名
        System.out.println(metaData.getUserName());
        // 获取数据库URL
        System.out.println(metaData.getURL());
        // 获取数据库驱动名称
        System.out.println(metaData.getDriverName());
        // 获取数据库驱动版本
        System.out.println(metaData.getDriverVersion());
        // 获取数据库是否只读
        System.out.println(metaData.isReadOnly());
        // 获取数据库是否支持事务
        System.out.println(metaData.supportsTransactions());
    }

    //获取数据库列表
    @Test
    public void test02FindAllCatalogs() throws SQLException {
        final DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getCatalogs()) {
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        }
        conn.close();
    }

    //获取指定数据库中的表信息
    @Test
    public void test03FindAllTables() throws Exception {
        final DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getTables("ihrm", "", "bs_user", new String[] {"TABLE"})) {
            while (rs.next()) {
                // 库名
                System.out.println(rs.getString(1));
                // schema
                System.out.println(rs.getString(2));
                // 表名
                System.out.println(rs.getString(3));
                // 数据库类型
                System.out.println(rs.getString(4));
                // 备注
                System.out.println(rs.getString(5));
            }
        }
    }

    //获取指定表中的字段信息
    @Test
    public void test04() throws Exception {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getColumns("ihrm", null, "bs_user", null)) {
            while (rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME"));
            }
        }
    }

}
