package com.silqin.qanal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.silqin.qanal.core.crawler.tool.UserAgentTool;
import com.silqin.qanal.core.domain.Category;

public class Test {
    public static void main(String[] args) {

        // 카테고리 데이터를 저장할 리스트
        List<Category> categories = new ArrayList<>();

        try {
            UserAgentTool userAgentTool = UserAgentTool.getInstance();
            String userAgent = userAgentTool.getUserAgent();
            System.out.println(userAgent);

            String url = "https://www.coupang.com";
            Connection conn = Jsoup.connect(url);
            conn.header("User-Agent", userAgent);
            conn.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,vi;q=0.6");

            Document doc = conn.get();

            // System.out.println(doc);

            // 대분류 요소 선택
            Elements firstDepthElements = doc.select(".menu.shopping-menu-list > li");

            for (Element fdEl : firstDepthElements) {
                Elements topCategories = fdEl.select("a.first-depth");

                for (Element topCategory : topCategories) {
                    // 대분류 정보 추출
                    String topCategoryUrl = topCategory.attr("href");
                    String[] hrefParts = topCategoryUrl.split("/");
                    String topCategoryTy = hrefParts[hrefParts.length - 2]; // 대분류 타입
                    String topCategoryId = hrefParts[hrefParts.length - 1]; // 대분류 ID
                    String topCategoryName = topCategory.text().trim();   // 대분류 텍스트

                    // 대분류 정보를 리스트에 추가
                    Category category = new Category();
                    category.setCategoryId(topCategoryId);
                    category.setCategoryType(topCategoryTy);
                    category.setCategoryName(topCategoryName);
                    category.setDepth1Id(topCategoryId);
                    category.setUrl(topCategoryUrl);
                    categories.add(category);

                      
                }
            }
        } catch (IOException e) {
            System.err.println("웹페이지를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        }

        System.out.println(":::::::::::::::::::");
        for (Category category : categories) {
            System.out.println(category.toString());

        }
    }
        
}

