package cn.itcast.freemarker.test;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 第一个FreeMarker程序（数据+模板=文件输出） 1.操作步骤
 */
public class FreeMarkerTest01
{

    @Test
    public void test01() throws Exception {
        // 1、配置实例
        final Configuration cfg = new Configuration();
        // 2、模板加载器
        cfg.setTemplateLoader(new FileTemplateLoader(new File("templates")));
        // 3、获取模板
        final Template template = cfg.getTemplate("template01.ftl");
        // 4、处理数据
        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("username", "张三");
        template.process(dataModel, new PrintWriter(System.out));
    }
}
