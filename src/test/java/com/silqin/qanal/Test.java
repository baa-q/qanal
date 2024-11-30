package com.silqin.qanal;

import java.util.HashMap;
import java.util.Map;

import com.silqin.qanal.core.crawler.tool.UserAgentTool;
import com.silqin.qanal.core.util.HttpUtil;

public class Test {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        try {

            UserAgentTool userAgentTool = UserAgentTool.getInstance();
            String userAgent = userAgentTool.getUserAgent();
            System.out.println(userAgent);

            //https://iamgus.tistory.com/699
            //https://velog.io/@chltpdus48/%ED%81%AC%EB%A1%A4%EB%A7%81%EC%9D%84-%ED%86%B5%ED%95%9C-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%88%98%EC%A7%91feat.-%EC%BF%A0%ED%8C%A1
            // String url = "https://www.naver.com";
            String url = "https://www.coupang.com";
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("User-Agent", userAgent);
            headers.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,vi;q=0.6");

            String response = HttpUtil.sendGet(url, headers);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
