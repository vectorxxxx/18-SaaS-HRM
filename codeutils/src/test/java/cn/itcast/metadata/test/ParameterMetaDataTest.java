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
 * 测试参数元数据（ParameterMetaData） 通过PreparedStatement获取 获取sql参数中的属性信息
 */
public class ParameterMetaDataTest
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

    //sql = SELECT * FROM bs_user WHERE id="1063705482939731968"
    @Test
    public void test() throws Exception {
        String sql = "select * from bs_user where id=?";
        final PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, "1063705989926227968");
        try (ResultSet rs = psmt.executeQuery()) {
            final ResultSetMetaData metaData = rs.getMetaData();
            final int columnCount = metaData.getColumnCount();
            System.out.println("总列数：" + columnCount);
            for (int i = 1; i < columnCount; i++) {
                // 获取列名
                System.out.print(metaData.getColumnName(i) + "\t");
                // 获取Java类型
                System.out.print(metaData.getColumnClassName(i) + "\t");
                // 获取SQL类型
                System.out.println(metaData.getColumnType(i));
            }
        }
    }
}
