package com.silqin.qanal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import com.silqin.qanal.core.crawler.coupang.CoupangCollector;
import com.silqin.qanal.core.crawler.tool.UserAgentTool;
import com.silqin.qanal.core.domain.Category;
import com.silqin.qanal.core.domain.Product;
import com.silqin.qanal.core.util.HttpUtil;

public class Test {
    public static void main(String[] args) throws Exception {

        // 카테고리 데이터를 저장할 리스트
        List<Category> categories = new ArrayList<>();

        try {
            UserAgentTool userAgentTool = UserAgentTool.getInstance();
            String userAgent = userAgentTool.getUserAgent();
            System.out.println(userAgent);

            String url = "https://www.coupang.com/np/campaigns/6585";

            Document doc = HttpUtil.sendJsoup(url);

            CoupangCollector coupangCollector = new CoupangCollector();
            Category category = new Category();
            category.setCategoryId("6585");
            // List<Rank> ranks = coupangCollector.getRanksByCategory(doc, category);
            List<Product> products = coupangCollector.getProductsByCategory(doc, category);

        } catch (IOException e) {
            System.err.println("웹페이지를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        }

        System.out.println(":::::::::::::::::::");
        for (Category category : categories) {
            System.out.println(category.toString());

        }
    }
        
}

