package com.todorex.common.security;

import com.todorex.common.utils.MD5Utils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by rex on 2018/5/1.
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    /**
     *@Author RexLi [www.todorex.com]
     *@Date 2018/5/3 下午1:12
     *@Description 用户密码验证
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        // 对比用户登入的数据与数据库查询出来的信息
        try {
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
            // 用户登录的密码
            String password = String.valueOf(usernamePasswordToken.getPassword());
            Object tokenCredentials = MD5Utils.encryptPassword(password);
            // getCredentials()和equals都是SimpleCredentialsMatcher自带的方法
            Object acountCredentials = getCredentials(authenticationInfo);
            return equals(tokenCredentials,acountCredentials);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
