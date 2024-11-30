package com.silqin.qanal.core.domain;

import java.time.LocalTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class CollectionLog {
    private String collectionDate;
    private boolean isSuccess;
    private String msg;
    private LocalTime startTime;
    // private LocalTime endTime;

    // public CollectionLog(String collectionDate, boolean isSuccess, String msg, LocalTime startTime, LocalTime endTime) {
    //     this.collectionDate = collectionDate;
    //     this.isSuccess = isSuccess;
    //     this.msg = msg;
    //     this.startTime = startTime;
    //     this.endTime = endTime;
    // }

    // public String getCollectionDate() {
    //     return collectionDate;
    // }

    // public void setCollectionDate(String collectionDate) {
    //     this.collectionDate = collectionDate;
    // }

    // public boolean isSuccess() {
    //     return isSuccess;
    // }

    // public void setSuccess(boolean success) {
    //     isSuccess = success;
    // }

    // public String getMsg() {
    //     return msg;
    // }

    // public void setMsg(String msg) {
    //     this.msg = msg;
    // }

    // public LocalTime getStartTime() {
    //     return startTime;
    // }

    // public void setStartTime(LocalTime startTime) {
    //     this.startTime = startTime;
    // }

    // public LocalTime getEndTime() {
    //     return endTime;
    // }

    // public void setEndTime(LocalTime endTime) {
    //     this.endTime = endTime;
    // }

    // @Override
    // public String toString() {
    //     return "CollectionLog{" +
    //             "collectionDate='" + collectionDate + '\'' +
    //             ", isSuccess=" + isSuccess +
    //             ", msg='" + msg + '\'' +
    //             ", startTime=" + startTime +
    //             ", endTime=" + endTime +
    //             '}';
    // }

}
