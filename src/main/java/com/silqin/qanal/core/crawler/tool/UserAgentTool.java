package com.silqin.qanal.core.crawler.tool;

import java.util.Random;

public class UserAgentTool {
    private static UserAgentTool instance;
    private String userAgent;
    private final String[] userAgents = {
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Version/14.1.1 Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
        "Mozilla/5.0 (Linux; Android 10; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Mobile Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    };

    private UserAgentTool() {
        // Private constructor to prevent instantiation from outside the class
        userAgent = generate();
    }

    public static UserAgentTool getInstance() {
        if (instance == null) {
            synchronized (UserAgentTool.class) {
                if (instance == null) {
                    instance = new UserAgentTool();
                }
            }
        }
        return instance;
    }

    public String getUserAgent() {
        return userAgent;
    }

    private String generate() {
        // 랜덤 User-Agent 선택
        Random random = new Random();
        userAgent = userAgents[random.nextInt(userAgents.length)];
        return userAgent;
    }
}
