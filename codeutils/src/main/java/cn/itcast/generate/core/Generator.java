package cn.itcast.generate.core;

import cn.itcast.generate.utils.FileUtils;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器的核心处理类 使用Freemarker完成文件生成 数据模型 + 模板 数据： 数据模型 模板的位置 生成文件的路径
 */
public class Generator
{

    private String templatePath;//模板路径
    private String outPath;//代码生成路径
    private Configuration cfg;

    public Generator(String templatePath, String outPath) throws Exception {
        this.templatePath = templatePath;
        this.outPath = outPath;
        //实例化Configuration对象
        cfg = new Configuration();
        //指定模板加载器
        cfg.setTemplateLoader(new FileTemplateLoader(new File(templatePath)));
    }

    /**
     * 代码生成 1.扫描模板路径下的所有模板 2.对每个模板进行文件生成（数据模型）
     */
    public void scanAndGenerator(Map<String, Object> dataModel) throws Exception {
        //1.根据模板路径找到此路径下的所有模板文件
        List<File> fileList = FileUtils.searchAllFile(new File(templatePath));
        //2.对每个模板进行文件生成
        for (File file : fileList) {
            executeGenertor(dataModel, file);
        }
    }

    /**
     * 对模板进行文件生成
     *
     * @param dataModel ： 数据模型
     * @param file      ： 模板文件 模板文件：c：com.ihrm.system.abc.java
     */
    private void executeGenertor(Map<String, Object> dataModel, File file) throws Exception {
        //1.文件路径处理   (E:\模板\${path1}\${path2}\${path3}\${ClassName}.java)
        //templatePath : E:\模板\
        String templateFileName = file
                .getAbsolutePath()
                .replace(this.templatePath, "");
        String outFileName = processTemplateString(templateFileName, dataModel);
        //2.读取文件模板
        Template template = cfg.getTemplate(templateFileName);
        template.setOutputEncoding("utf-8");//指定生成文件的字符集编码
        //3.创建文件
        File file1 = FileUtils.mkdir(outPath, outFileName);
        //4.模板处理（文件生成）
        try (FileWriter fw = new FileWriter(file1)) {
            template.process(dataModel, fw);
        }
    }

    public String processTemplateString(String templateString, Map dataModel) throws Exception {
        StringWriter out = new StringWriter();
        Template template = new Template("ts", new StringReader(templateString), cfg);
        template.process(dataModel, out);
        return out.toString();
    }

    public static void main(String[] args) throws Exception {
        String templatePath = "D:\\workspace-mine\\18-SaaS-HRM\\codeutils\\模板";
        String outPath = "D:\\workspace-mine\\18-SaaS-HRM\\codeutils\\生成代码路径";
        Generator generator = new Generator(templatePath, outPath);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("username", "张三");
        generator.scanAndGenerator(dataModel);
    }

}
