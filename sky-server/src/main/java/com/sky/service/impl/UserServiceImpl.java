package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    @Transactional
    public User login(UserLoginDTO userLoginDTO) {
        //调用微信接口服务
        String openid =getOpenid(userLoginDTO.getCode());
        if(openid==null){
            throw new LoginFailedException("登陆失败");
        }
        //判断当前用户是否为新用户
        User user=userMapper.getUserByOpenid(openid);
        if(user==null){
            user=User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;

    }

    private String getOpenid(String code){
        Map<String, String> params = new HashMap<String, String>();

        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code",code);
        params.put("grant_type", "authorization_code");

        String json =  HttpClientUtil.doGet(WX_LOGIN_URL,params);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String openid = jsonObject.getString("openid");

        return openid;
    }
}
