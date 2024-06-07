package cn.itcast.controller;

import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

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
}
