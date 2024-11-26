package com.silqin.qanal;

import java.util.HashMap;
import java.util.Map;

import com.silqin.core.util.HttpUtil;

public class Test {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        try {
            String url = "https://www.naver.com";
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");

            String response = HttpUtil.sendGet(url, headers);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
