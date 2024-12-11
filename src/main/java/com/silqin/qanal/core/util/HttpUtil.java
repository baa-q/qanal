package com.silqin.qanal.core.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpUtil {

    private static final int TIMEOUT = 5000;

    private final static String[] userAgents = {
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Version/14.1.1 Safari/537.36",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
        "Mozilla/5.0 (Linux; Android 10; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Mobile Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    };
    
    public static String getUserAgent() {
        Random random = new Random();
        return userAgents[random.nextInt(userAgents.length)];
    }

    public static Document sendJsoup(String urlString) throws Exception {

        try {
            String userAgent = getUserAgent(); // Custom method to retrieve User-Agent
            
            Document doc = null;
            Connection conn;
            
            // do {
                conn = Jsoup.connect(urlString)
                        .userAgent(userAgent)
                        .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,vi;q=0.6")
                        .header("Referer", "https://www.google.com") // Optional
                        .timeout(100000); // Set timeout to 10 seconds
                
                doc = conn.get();
                System.out.println(doc.location());
            // } while (doc.location().contains("m.coupang"));

            return doc;
        } catch (HttpStatusException e) {
            System.err.println("HTTP Status Error: " + e.getStatusCode() + " for URL: " + urlString);
            throw e; // Re-throw to handle at a higher level
        } catch (Exception e) {
            System.err.println("Error fetching URL: " + urlString);
            throw e;
        }
    }

    public static Document sendJsoupWithRetry(String urlString, int maxRetries, int retryDelayMillis) throws Exception {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return sendJsoup(urlString); // 기존 메서드 호출
            } catch (IOException e) {
                attempts++;
                System.err.println("Retry " + attempts + "/" + maxRetries + " for URL: " + urlString + " due to: " + e.getMessage());
                if (attempts == maxRetries) {
                    throw e; // 최대 시도 횟수 초과 시 예외 발생
                }
                Thread.sleep(retryDelayMillis); // 재시도 전 대기 시간
            }
        }
        throw new IOException("Failed to fetch URL after " + maxRetries + " attempts: " + urlString);
    }

    public static String sendGet(String urlString, Map<String, String> headers) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        // 헤더 추가
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("GET 요청 실패: HTTP 응답 코드 " + responseCode);
        }

        // 응답 읽기
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    public static String sendPost(String urlString, String body, Map<String, String> headers) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);
        connection.setDoOutput(true);

        // 헤더 추가
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        // 바디 추가
        try (OutputStream os = connection.getOutputStream()) {
            DataOutputStream writer = new DataOutputStream(os);
            writer.writeBytes(body);
            writer.flush();
            writer.close();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("POST 요청 실패: HTTP 응답 코드 " + responseCode);
        }

        // 응답 읽기
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
}
