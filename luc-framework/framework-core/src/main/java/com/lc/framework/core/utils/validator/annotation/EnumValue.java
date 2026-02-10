package com.lc.framework.core.utils.validator.annotation;

import com.lc.framework.core.constants.NoneEnum;
import com.lc.framework.core.utils.validator.EnumValueValidator;
import com.lc.framework.core.utils.validator.annotation.EnumValue.List;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.lc.framework.core.constants.StringConstants.EMPTY_STRING;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <pre>
 *     参数取值枚举注解，枚举值获取顺序：key > value > enumClass
 * <pre/>
 * @author : Lu Cheng
 * @date : 9/2/26 16:23
 * @version : 1.0
 */
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface EnumValue {

    String message() default "{com.lc.framework.core.utils.validator.annotation.EnumValue.message}";

    String key() default EMPTY_STRING;

    String[] value() default {};

    /**
     * constraint of enum class
     */
    Class<? extends Enum<?>> enumClass() default NoneEnum.class;

    /**
     * constraint group
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link EnumValue} annotations on the same element.
     *
     * @see EnumValue
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        EnumValue[] value();
    }
}
