package com.silqin.qanal.core.crawler.coupang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.silqin.qanal.core.domain.Category;
import com.silqin.qanal.core.domain.Product;
import com.silqin.qanal.core.domain.Rank;
import com.silqin.qanal.core.util.HttpUtil;


public class CoupangCollector {
    
    public String COUPANG_URL = "https://www.coupang.com";

    // @Autowired
    // private CategoryService categoryService;

    public void collect() {
        
        try {
            System.out.println("::::::::::::::::::: 쿠팡 카테고리 정보 수집 시작 :::::::::::::::::::");

    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("::::::::::::::::::: 쿠팡 카테고리 정보 수집 종료 :::::::::::::::::::");
        }

    }

    public List<Category> getCategories() {

        List<Category> allCategories = null;

        try {
            System.out.println("::::::::::::::::::: 쿠팡 카테고리 정보 수집 시작 :::::::::::::::::::");
    
            // 메인 페이지 가져오기
            Document mainPageDoc = null;
    
            // 대분류 카테고리 가져오기
            List<Category> depth1Categories = new ArrayList<>();
            while (depth1Categories.size() == 0) {
                mainPageDoc = fetchDocumentWithRetry(COUPANG_URL);
                System.out.println("Main Page Loaded Successfully");
                depth1Categories = getDepth1ByMainPage(mainPageDoc);
            }
    
            allCategories = new ArrayList<>(depth1Categories);
            // 중분류와 소분류 가져오기
            for (Category depth1Category : depth1Categories) {
                getCategoriesByDepth1Id(depth1Category.getCategoryId());
            }
    
        } catch (IOException e) {
            System.err.println("정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("::::::::::::::::::: 쿠팡 카테고리 정보 수집 종료 :::::::::::::::::::");
        }

        return allCategories;
    }


    public List<Category> getCategoriesByDepth1Id(String depth1Id) {
        List<Category> allCategories = new ArrayList<>();

        try {

            // 중분류 페이지 가져오기
            Elements depth2LiEls = new Elements();
            while (depth2LiEls.size() == 0) {
                String url = COUPANG_URL + "/np/categories/" + depth1Id;;
                Document depth2CategoryDoc = fetchDocumentWithRetry(url);
                depth2LiEls = depth2CategoryDoc.select("#searchCategoryComponent ul.search-option-items > li");
            }

            // 중분류 카테고리 수집
            for (Element depth2CategoryEl : depth2LiEls) {

                Category depth2Category = getCategoryByEl(depth2CategoryEl, depth1Id);
                allCategories.add(depth2Category);

                // // 소분류 페이지 가져오기
                // Elements depth3LiEls = new Elements();
                // while (depth3LiEls.size() == 0) {
                //     Document depth3CategoryDoc = fetchDocumentWithRetry(COUPANG_URL + depth2Category.getUrl());
                //     depth3LiEls = depth3CategoryDoc.select("#searchCategoryComponent ul.search-option-items > li");
                // }

                // // 소분류 카테고리 수집
                // for (Element depth3CategoryEl : depth3LiEls) {
                //     Category depth3Category = getCategoryByEl(depth3CategoryEl, depth2Category);
                //     allCategories.add(depth3Category);
                // }

            }
        
        } catch (IOException e) {
            System.err.println("정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allCategories;
    }


    //For Depth2, Depth3
    private Category getCategoryByEl(Element el, Category parentCategory) {
        Category category = getCategoryByEl(el, parentCategory.getDepth1Id());
        String id = category.getCategoryId();

        if(parentCategory.getDepth2Id() != null) {
            category.setDepth2Id(parentCategory.getDepth2Id());
            category.setDepth3Id(id);
        }else {
            category.setDepth2Id(id);
        }

        if(category.getDepth3Id() == null){
            System.out.println("대분류 : " + category.getDepth1Id() + 
            " 중분류 : " + category.getDepth2Id() +  " 중분류 이름 : " + category.getCategoryName());
        }else{
            System.out.println("대분류 : " + category.getDepth1Id() + 
            " 중분류 : " + category.getDepth2Id() +  " 중분류 이름 : " + parentCategory.getCategoryName() + 
            " 소분류 : " + category.getDepth3Id() + " 소분류 이름 : " + category.getCategoryName());
        }

        return category;
    }

    //For Depth2
    private Category getCategoryByEl(Element el, String depth1Id) {
        Category category = new Category();

        String url = el.attr("data-link-uri");
        String[] hrefParts = url.split("/");
        String type = hrefParts[hrefParts.length - 2];
        String id = hrefParts[hrefParts.length - 1];
        String name = el.select("> label > .seo-link-url").text().trim();
        if(name.equals("")) {
            name = el.select("> label").text().trim();
        }

        category.setCategoryId(id);
        category.setCategoryType(type);
        category.setCategoryName(name);
        category.setUrl(url);
        category.setDepth1Id(depth1Id);
        category.setDepth2Id(id);

        System.out.println("대분류 : " + category.getDepth1Id() + 
        " 중분류 : " + category.getDepth2Id() +  " 중분류 이름 : " + category.getCategoryName());

        return category;
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

    public List<Rank> getRanksByCategory(Document doc, Category category) throws Exception {
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

    public List<Product> getProductsByCategory(Document doc, Category category) {
        List<Product> products = new ArrayList<>();

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
            int productPrice = Integer.parseInt(li.select(".price-value").text().trim().replaceAll("[^0-9]", ""));
            String productUnitPrice = li.select(".unit-price").text().trim();
            int productRatingStar = Integer.parseInt(li.select(".rating").text().trim().replaceAll("[^0-9]", ""));
            int productRatingCount = Integer.parseInt(li.select(".rating-total-count").text().trim().replaceAll("[^0-9]", ""));

            Product product = new Product();
            product.setProductId(productId);
            product.setProductName(productName);
            product.setPrice(productPrice);
            product.setUnitPrice(productUnitPrice);
            product.setRatingStar(productRatingStar);
            product.setRatingCount(productRatingCount);
            products.add(product);

            System.out.println(product.toString());
        }

        return products;
    }

    private Document fetchDocumentWithRetry(String url) throws Exception {
        int maxRetries = 5;       // 최대 재시도 횟수
        int retryDelayMillis = 2000; // 재시도 대기 시간 (2초)
        return HttpUtil.sendJsoupWithRetry(url, maxRetries, retryDelayMillis);
    }
}
