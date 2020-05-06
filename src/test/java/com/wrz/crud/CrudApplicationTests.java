package com.wrz.crud;

import com.wrz.crud.utils.email.EmailUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CrudApplicationTests {

    @Autowired
    EmailUtils emailUtils;

    @Test
    void contextLoads() {
        System.out.println(emailUtils.getForm());
    }

    @Test
    void showPassword() {
        String hashAlgorithmName = "MD5";
        Object credentials = "wrzzxc123";
        Object salt = ByteSource.Util.bytes("wrz");
        int hasIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName,credentials,salt,hasIterations);
        System.out.println(result);
    }

}
