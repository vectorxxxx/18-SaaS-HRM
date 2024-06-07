package cn.itcast.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class QiniuDemo
{
    //...生成上传凭证，然后准备上传
    private static final String ACCESS_KEY = "ulwjxV5GsZ2oQX51or8CVb5HDn9hZdGsKXDSXKCJ";
    private static final String SECRET_KEY = "m1cDkMZ28ybLZh40oZ6s6m0r-vIz04uCWpkBZpU4";
    private static final String BUCKET = "vectorx-ihrm-bucket";

    /**
     * <pre>{@literal
     * 将图片上传到七牛云服务
     *      1.更新用户图片信息（用户id=key）
     *      2.访问图片
     *          存储空间分配的：http://pkbivgfrm.bkt.clouddn.com
     *          上传的文件名
     *          更新图片之后：访问的时候，再请求连接添加上时间戳
     * }</pre>
     */
    @Test
    public void testUpload01() {
        final String filePath = "D:\\workspace-mine\\18-SaaS-HRM\\ihrm_parent\\qiniu-demo\\src\\main\\resources\\001.png";
        final String key = "test";

        // 创建认证对象
        final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        // 上传凭证
        final String uploadToken = auth.uploadToken(BUCKET, key);

        // 创建配置类
        final Configuration configuration = new Configuration(Zone.zone0());
        // 创建上传管理器
        final UploadManager uploadManager = new UploadManager(configuration);
        try {
            // 上传文件
            final Response response = uploadManager.put(filePath, key, uploadToken);
            // 解析上传结果
            final DefaultPutRet ret = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(ret.key);
            System.out.println(ret.hash);
        }
        catch (QiniuException e) {
            e.printStackTrace();
        }
    }

    //断点续传
    @Test
    public void testUpload02() throws IOException {
        final String filePath = "D:\\workspace-mine\\18-SaaS-HRM\\ihrm_parent\\qiniu-demo\\src\\main\\resources\\test.xlsx";
        final String key = "testExcel";

        final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        final String uploadToken = auth.uploadToken(BUCKET, key);

        final Configuration configuration = new Configuration(Zone.zone0());
        // 断点续传
        final FileRecorder recorder = new FileRecorder(Paths
                .get(System.getProperty("java.io.tmpdir"), BUCKET)
                .toString());
        final UploadManager uploadManager = new UploadManager(configuration, recorder);
        try {
            final Response response = uploadManager.put(filePath, key, uploadToken);
            final DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        }
        catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
