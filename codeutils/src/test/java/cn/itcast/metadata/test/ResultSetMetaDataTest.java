package cn.itcast.metadata.test;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Properties;

/**
 * 测试结果集元数据（ResultSetMetaData） 通过ResultSet获取 获取查询结果的信息
 */
public class ResultSetMetaDataTest
{

    private Connection conn;

    @Before
    public void init() throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/ihrm?useUnicode=true&characterEncoding=utf8";
        String username = "root";
        String password = "root";

        Properties props = new Properties();
        props.put("remarksReporting", "true");
        props.put("user", username);
        props.put("password", password);

        Class.forName(driver);
        conn = DriverManager.getConnection(url, props);
    }

    @Test
    public void test() throws Exception {
        String sql = "select * from bs_user where id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "1063705482939731968");
            try (ResultSet rs = pstmt.executeQuery()) {
                // 获取结果集元数据
                ResultSetMetaData metaData = rs.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    //获取列名
                    String columnName = metaData.getColumnName(i);
                    //获取字段类型 sql类型
                    String columnType = metaData.getColumnTypeName(i);
                    //获取java类型
                    String columnClassName = metaData.getColumnClassName(i);
                    System.out.println(columnName + "--" + columnType + "---" + columnClassName);
                }
            }
        }

    }

}
