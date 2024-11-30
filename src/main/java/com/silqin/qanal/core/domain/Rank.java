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

    public Rank(LocalDate collectionDate, String categoryId, int rank, String productId) {
        this.collectionDate = collectionDate;
        this.categoryId = categoryId;
        this.rank = rank;
        this.productId = productId;
    }

    public LocalDate getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "collectionDate=" + collectionDate +
                ", categoryId='" + categoryId + '\'' +
                ", rank=" + rank +
                ", productId='" + productId + '\'' +
                '}';
    }
}
