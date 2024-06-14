package com.lc.framework.core.excp;

import static com.lc.framework.core.mvc.StatusConstants.AUTH_OUT_OF_DATE;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-25 17:00
 */
public class BizTokenException extends BizException {
    public BizTokenException(String message) {
        super(AUTH_OUT_OF_DATE, message);
    }
}
