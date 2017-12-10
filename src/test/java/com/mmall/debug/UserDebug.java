package com.mmall.debug;

import com.mmall.controller.portal.UserController;
import com.mmall.service.impl.UserServiceImpl;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2017/12/10.
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDebug {

    private Logger logger = LoggerFactory.getLogger(UserDebug.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserController userController;

    @Test
    public void testRestPassword(){
        String passwordMD = MD5Util.MD5EncodeUtf8("geely");
        logger.info(passwordMD);
        logger.info("======");
//        userService.forgetResetPassword("geely",passwordMD,"eef8d5ce-6527-4396-ba7b-fdcc2a96f944");
//        userController.forgerResetPassword("geely",passwordMD,"3b8e6a58-a70b-4ac4-a129-0b3230132e59");
    }
    @Test
    public void testTrim(){
        String str = "   ac b   ";
        String str1 = str.trim();
        logger.info(str1);
    }
}
