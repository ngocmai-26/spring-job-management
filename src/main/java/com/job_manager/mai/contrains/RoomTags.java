package com.job_manager.mai.contrains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum RoomTags {

    WORK("Công việc"),
    FAMILY("Gia đình"),

    FRIEND("Bạn bè"),
    CUSTOMER("Khách hàng"),
    WAIT("Trả lời sau"),
    OTHER("Khác");

    private final String val;
}
