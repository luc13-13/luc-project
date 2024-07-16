package com.lc.framework.core.mvc;

import lombok.Data;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc : rest响应状态封装，包含响应码规范与说明
 * @date : 2023/4/15 13:36
 */
@Data
public class Status {
    private Integer code;
    private String desc;

    public Status(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Status of(Integer code, String desc) {
        return new Status(code, desc);
    }
}
