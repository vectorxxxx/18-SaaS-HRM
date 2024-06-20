package cn.itcast.controller;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class JasperController
{

    @GetMapping("/testJasper")
    public void createPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1、引入 Jasper 文件
        final ClassPathResource resource = new ClassPathResource("templates/test.jasper");

        try (final FileInputStream fis = new FileInputStream(resource.getFile());
             final ServletOutputStream os = response.getOutputStream()) {

            // 2、创建 JasperPrint，向 Jasper 文件填充数据
            final JasperPrint print = JasperFillManager.fillReport(fis, new HashMap<>(), new JREmptyDataSource());

            // 3、将 JasperPrint 以 PDF 的形式输出
            JasperExportManager.exportReportToPdfStream(print, os);
        }
        catch (JRException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/testJasper02")
    public void createPdf02(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1、引入 Jasper 文件
        final ClassPathResource resource = new ClassPathResource("templates/paramtersTest.jasper");

        try (final FileInputStream fis = new FileInputStream(resource.getFile());
             final ServletOutputStream sos = response.getOutputStream()) {

            final Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "用户详情");
            parameters.put("username", "张三");
            parameters.put("mobile", "19165171302");
            parameters.put("companyName", "略晟皇灿亮咨询(河南省)有限公司");
            parameters.put("departmentName", "讲师");

            // 2、创建 JasperPrint，向 Jasper 文件填充数据
            final JasperPrint print = JasperFillManager.fillReport(fis, parameters, new JREmptyDataSource());

            // 3、将 JasperPrint 以 PDF 的形式输出
            JasperExportManager.exportReportToPdfStream(print, sos);
            response.setContentType("application/pdf");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
