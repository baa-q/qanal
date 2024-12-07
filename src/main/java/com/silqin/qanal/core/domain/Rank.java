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
public class Rank {
    private LocalDate collectionDate;
    private String categoryId;
    private int rank;
    private String productId;

}
