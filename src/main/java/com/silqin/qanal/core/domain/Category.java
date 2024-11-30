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

}