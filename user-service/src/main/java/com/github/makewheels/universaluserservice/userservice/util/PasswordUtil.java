package com.github.makewheels.universaluserservice.userservice.util;

import cn.hutool.crypto.SecureUtil;

public class PasswordUtil {
    public static String encrypt(String plaintext) {
        return SecureUtil.md5(plaintext);
    }
}
