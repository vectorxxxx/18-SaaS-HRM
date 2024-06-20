package com.ihrm.system.controller;

import com.baidu.aip.util.Base64Util;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.response.FaceLoginResult;
import com.ihrm.domain.system.response.QRCode;
import com.ihrm.system.service.FaceLoginService;
import com.ihrm.system.utils.BaiduAiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sys/faceLogin")
public class FaceLoginController
{

    @Autowired
    private FaceLoginService faceLoginService;

    @Autowired
    private BaiduAiUtil baiduAiUtil;

    /**
     * 获取刷脸登录二维码 返回值：QRCode对象（code，image）
     */
    @RequestMapping(value = "/qrcode",
                    method = RequestMethod.GET)
    public Result qrcode() throws Exception {
        QRCode qrCode = faceLoginService.getQRCode();
        return new Result(ResultCode.SUCCESS, qrCode);
    }

    /**
     * 检查二维码：登录页面轮询调用此方法，根据唯一标识code判断用户登录情况 查询二维码扫描状态 返回值：FaceLoginResult state ：-1，0，1 （userId和token）
     */
    @RequestMapping(value = "/qrcode/{code}",
                    method = RequestMethod.GET)
    public Result qrcodeCeck(
            @PathVariable(name = "code")
                    String code) throws Exception {
        FaceLoginResult checkQRCode = faceLoginService.checkQRCode(code);
        return new Result(ResultCode.SUCCESS, checkQRCode);
    }

    /**
     * 人脸登录：根据落地页随机拍摄的面部头像进行登录 根据拍摄的图片调用百度云AI进行检索查找
     */
    @RequestMapping(value = "/{code}",
                    method = RequestMethod.POST)
    public Result loginByFace(
            @PathVariable(name = "code")
                    String code,
            @RequestParam(name = "file")
                    MultipartFile attachment) throws Exception {
        //人脸登录获取用户id（不为null登录成功）
        String userId = faceLoginService.loginByFace(code, attachment);
        return new Result(userId != null ?
                          ResultCode.SUCCESS :
                          ResultCode.FAIL);
    }

    /**
     * 图像检测，判断图片中是否存在面部头像
     */
    @RequestMapping(value = "/checkFace",
                    method = RequestMethod.POST)
    public Result checkFace(
            @RequestParam(name = "file")
                    MultipartFile attachment) throws Exception {
        String image = Base64Util.encode(attachment.getBytes());
        Boolean aBoolean = baiduAiUtil.faceCheck(image);
        return new Result(aBoolean ?
                          ResultCode.SUCCESS :
                          ResultCode.FAIL);
    }

}
