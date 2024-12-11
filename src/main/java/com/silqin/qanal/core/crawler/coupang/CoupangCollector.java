package com.silqin.qanal.core.crawler.coupang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.silqin.qanal.api.category.service.CategoryService;
import com.silqin.qanal.core.domain.Category;
import com.silqin.qanal.core.domain.Rank;
import com.silqin.qanal.core.util.HttpUtil;


public class CoupangCollector {
    
    public String COUPANG_URL = "https://www.coupang.com";

    // @Autowired
    // private CategoryService categoryService;

    public void collect() {
        
        try {
            System.out.println("::::::::::::::::::: 쿠팡 카테고리 정보 수집 시작 :::::::::::::::::::");
    
            // 메인 페이지 가져오기
            Document mainPageDoc = null;
    
            // 대분류 카테고리 가져오기
            List<Category> depth1Categories = new ArrayList<>();
            while (depth1Categories.size() == 0) {
                mainPageDoc = fetchDocumentWithRetry(COUPANG_URL);
                System.out.println("Main Page Loaded Successfully");
                System.out.println(mainPageDoc);

                depth1Categories = getDepth1ByMainPage(mainPageDoc);
                System.out.println("::::::::::::::::::: 대분류 카테고리 정보 획득 시도 :::::::::::::::::::");
                System.out.println("대분류 카테고리 수: " + depth1Categories.size());
            }
            System.out.println("::::::::::::::::::: 대분류 카테고리 정보 획득 성공 :::::::::::::::::::");
    
            // 중분류와 소분류 가져오기
            List<Category> allCategories = new ArrayList<>(depth1Categories);
            for (Category depth1Category : depth1Categories) {

                // 중분류 페이지 가져오기
                Document depth2CategoryDoc = fetchDocumentWithRetry(COUPANG_URL + depth1Category.getUrl());
                System.out.println("대분류 " + depth1Category.getCategoryId() + ":" + depth1Category.getCategoryName() + "의 중분류 Page Loaded Successfully: " + depth1Category.getUrl());

                // 중분류와 소분류 카테고리 수집
                List<Category> depth2Categories = getChildByCategoryPage(depth2CategoryDoc, depth1Category);
                System.out.println("대분류 " + depth1Category.getCategoryId() + ":" + depth1Category.getCategoryName() + "의 하위 중분류 카테고리 수: " + depth2Categories.size());
                allCategories.addAll(depth2Categories);

                for (Category depth2Category : depth2Categories) {
                    // 소분류 페이지 가져오기
                    Document depth3CategoryDoc = fetchDocumentWithRetry(COUPANG_URL + depth2Category.getUrl());
                    System.out.println("중분류 " + depth2Category.getCategoryId() + ":" + depth2Category.getCategoryName() + "의 소분류 Page Loaded Successfully: " + depth2Category.getUrl());

                    // 소분류 카테고리 수집
                    List<Category> depth3Categories = getChildByCategoryPage(depth3CategoryDoc, depth2Category);
                    System.out.println("중분류 " + depth2Category.getCategoryId() + ":" + depth2Category.getCategoryName() + "의 하위 소분류 카테고리 수: " + depth3Categories.size());
                    allCategories.addAll(depth3Categories);
                }

            }
    
        } catch (IOException e) {
            System.err.println("정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("::::::::::::::::::: 쿠팡 카테고리 정보 수집 종료 :::::::::::::::::::");
        }

    }

    private List<Category> getDepth1ByMainPage(Document doc) {
        List<Category> categories = new ArrayList<>();
    
        // 대분류 요소 선택
        Elements liEls = doc.select(".menu.shopping-menu-list > li");
    
        for (Element li : liEls) {
            Elements els = li.select("a.first-depth");
            for (Element categoryEl : els) {
    
                // 대분류 정보 추출
                String url = categoryEl.attr("href");
                String[] hrefParts = url.split("/");
                String type = hrefParts[hrefParts.length - 2];
                String id = hrefParts[hrefParts.length - 1];
                String name = categoryEl.text().trim();
    
                // 대분류 정보를 리스트에 추가
                Category category = new Category();
                category.setCategoryId(id);
                category.setCategoryType(type);
                category.setCategoryName(name);
                category.setDepth1Id(id);
                category.setUrl(url);
                category.setHasChildLi(true);
                categories.add(category);
            }
        }

        // 카테고리 로그 출력
        for (Category category : categories) {
            System.out.println(category.toString());
        }

        return categories;
    }
    

    private List<Category> getChildByCategoryPage(Document doc, Category parenCategory) {
        List<Category> categories = new ArrayList<>();

        Elements liEls = null;
        if (parenCategory.isHasChildLi()) {
            liEls = doc.select("#searchCategoryComponent ul.search-option-items > li");
        } else {
            liEls = doc.select("#searchCategoryComponent ul.search-option-items-child > li");
        }

        for (Element categoryEl : liEls) {
            String url = categoryEl.attr("data-link-uri");
            String[] hrefParts = url.split("/");
            String type = hrefParts[hrefParts.length - 2];
            String id = hrefParts[hrefParts.length - 1];
            String name = liEls.select(".seo-link-url[href*='"+url+"']").text().trim();

            Category category = new Category();
            category.setCategoryId(id);
            category.setCategoryType(type);
            category.setCategoryName(name);
            category.setDepth1Id(parenCategory.getDepth1Id());
            
            Elements childrenLi = categoryEl.select(".ul.search-option-items-child > li");
            category.setHasChildLi(true);
            if (childrenLi.size() == 0) {
                category.setHasChildLi(false);
            }

            if (parenCategory.getDepth2Id() != null) { //부모에게 depth2가 있다면 현재 카테고리는 depth3
                category.setDepth2Id(parenCategory.getDepth2Id());
                category.setDepth3Id(id);
            }else { //부모에게 depth2가 없다면 현재 카테고리는 depth2
                category.setDepth2Id(id);
            }
            category.setUrl(url);
            categories.add(category);

        }
        
        // 카테고리 로그 출력
        for (Category category : categories) {
            System.out.println(category.toString());
        }

        return categories;

    }

    public List<Rank> getRankByCategory(Document doc, Category category) throws Exception {
        List<Rank> ranks = new ArrayList<>();

        Elements liEls = doc.select("#productList > li");

        String categoryId = category.getCategoryId();
        System.out.println("Category ID: " + categoryId);

        int index = 0;
        List<Element> liElsList = liEls.subList(0, 30);
        for (Element li : liElsList) {
            index++;
            System.out.println("Rank: " + index);
            String productId = li.attr("data-product-id");
            String productName = li.select(".name").text().trim();

            Rank rank = new Rank();
            rank.setCategoryId(categoryId);
            rank.setRank(index);
            rank.setProductId(productId);
            ranks.add(rank);

            System.out.println(productName);
            System.out.println(rank.toString());
        }

        return ranks;
    }

    private Document fetchDocumentWithRetry(String url) throws Exception {
        int maxRetries = 5;       // 최대 재시도 횟수
        int retryDelayMillis = 2000; // 재시도 대기 시간 (2초)
        return HttpUtil.sendJsoupWithRetry(url, maxRetries, retryDelayMillis);
    }
}
