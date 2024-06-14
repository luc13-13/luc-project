package com.lc.auth.gateway.enums;

import com.lc.framework.core.mvc.RequestHeaderConstants;
import static com.lc.framework.core.mvc.RequestHeaderConstants.JSESSIONID;
import static com.lc.framework.core.mvc.RequestHeaderConstants.REQUEST_ID;

/**
 * <pre>
 *  必备的请求头属性： {@link RequestHeaderConstants#JSESSIONID} {@link RequestHeaderConstants#REQUEST_ID}
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-09-04 09:53
 */
public enum RequiredHeaderEnum {
    HEADER_JSESSIONID(JSESSIONID, "session唯一标识"),
    HEADER_REQUEST_ID(REQUEST_ID, "每次url请求的唯一标识"),
    HEADER_FROM("FROM", "清理处理请求头中的参数");

    private final String code;
    private final String desc;
    RequiredHeaderEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
