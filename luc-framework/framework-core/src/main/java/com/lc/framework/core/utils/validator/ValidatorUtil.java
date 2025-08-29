package com.lc.framework.core.utils.validator;

import com.lc.framework.core.mvc.BizException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import org.hibernate.validator.HibernateValidator;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 校验实体的工具类， 主要用于校验请求参数
 * @author : Lu Cheng
 * @version : 1.0
 * @date : 2023/4/15 13:43
 */
public class ValidatorUtil {
    static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * @author Lu Cheng
     * @desc 校验实体类的工具， 支持分组校验
     * @date 2023/4/15
     */
    public static <T> void validate(T targets, Class<?>...groups) throws BizException {
        Set<ConstraintViolation<T>> validations;
        if (groups == null) {
            validations = VALIDATOR.validate(targets, Default.class);
        } else {
            validations = VALIDATOR.validate(targets, groups);
        }
        if(!CollectionUtils.isEmpty(validations)) {
            StringBuilder sb = new StringBuilder();
            for(ConstraintViolation<T> v : validations) {
                sb.append(v.getMessage());
            }throw BizException.exp(sb.toString());
        }
    }
}
