package com.info.xiaotingtingBackEnd.controller;


import com.info.xiaotingtingBackEnd.constants.HttpResponseCodes;
import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import com.qiniu.util.Auth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("third")
public class ThirdController {


    @RequestMapping(value = "getQiniuToken", method = RequestMethod.POST)
    public ApiResponse<String> getQiniuToken() {
        String accessKey = "xJwSUxOTy_TM98539iTTrcEsGkjsQkQ4sJWtGJxU";
        String secretKey = "xs47gNY70S-e5IL_7770azCC0bwMuXaq9Co7Ky_n";
        String bucket = "teamworker";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
        ApiResponse<String> response=new ApiResponse<>();
        response.setStatus(HttpResponseCodes.SUCCESS);
        response.setData(upToken);
        return response;
    }


}
