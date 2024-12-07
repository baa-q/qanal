package com.silqin.qanal.core.domain;

import java.time.LocalDate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Category {
    private String categoryId;
    private String categoryType;
    private String categoryName;
    private String depth1Id;
    private String depth2Id;
    private String depth3Id;
    private boolean isCollection;
    private LocalDate updateDate;
    private LocalDate deleteDate;
    private String url;

    //DB에 이용하지 않고 크롤링할 때만 사용되는 필드
    //쿠팡에서 중분류를 펼쳐도 소분류가 나오는 경우를 구분하기 위해 사용
    private boolean hasChildLi;

}