package com.ihrm.common.utils;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.util.Date;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-07 15:52:21
 */
public class QiniuUploadUtil
{
    private static final String ACCESS_KEY = "ulwjxV5GsZ2oQX51or8CVb5HDn9hZdGsKXDSXKCJ";
    private static final String SECRET_KEY = "m1cDkMZ28ybLZh40oZ6s6m0r-vIz04uCWpkBZpU4";
    private static final String BUCKET = "vectorx-ihrm-bucket";
    private static final String PRIX = "http://pkbivgfrm.bkt.clouddn.com/";

    private UploadManager uploadManager;

    public QiniuUploadUtil() {
        final Configuration cfg = new Configuration(Zone.zone0());
        uploadManager = new UploadManager(cfg);
    }

    public String upload(String fileName, byte[] bytes) {
        final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        final String uploadToken = auth.uploadToken(BUCKET, fileName);
        try {
            final Response response = uploadManager.put(bytes, fileName, uploadToken);
            final DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return PRIX
                    .concat(putRet.key)
                    .concat("?t=")
                    .concat(String.valueOf(new Date().getTime()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
