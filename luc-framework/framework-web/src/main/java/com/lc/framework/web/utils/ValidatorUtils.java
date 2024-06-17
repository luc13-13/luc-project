package com.lc.framework.web.utils;

import com.lc.framework.web.excp.BizException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * @author : Lu Cheng
 * @version : 1.0
 * @desc : 校验实体的工具类
 * @date : 2023/4/15 13:43
 */
public class ValidatorUtils {
    static final Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * @author Lu Cheng
     * @desc 校验实体类的工具， 支持分组校验
     * @date 2023/4/15
     */
    public static <T> void validate(T targets, Class<?>...groups) throws BizException {
        Set<ConstraintViolation<T>> validations = validator.validate(targets, groups);
        if(!CollectionUtils.isEmpty(validations)) {
            StringBuilder sb = new StringBuilder();
            for(ConstraintViolation<T> v : validations) {
                sb.append(v.getMessage());
            }throw BizException.exp(sb.toString());
        }
    }
}
