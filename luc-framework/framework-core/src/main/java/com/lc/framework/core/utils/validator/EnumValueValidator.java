package com.lc.framework.core.utils.validator;

import com.lc.framework.core.utils.validator.annotation.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 9/2/26 16:18
 * @version : 1.0
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {

    @Autowired
    private EnumValueManager enumValueManager;

    private Set<String> enumValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        if (constraintAnnotation == null) {
            return;
        }
        if (CollectionUtils.isEmpty(enumValues) && StringUtils.hasText(constraintAnnotation.key())) {
            // 使用key获取配置的value
            this.enumValues = enumValueManager.getEnumValues(constraintAnnotation.key());
        }
        else if (CollectionUtils.isEmpty(enumValues) && constraintAnnotation.value() != null && constraintAnnotation.value().length > 0) {
            // 使用编码规定的value
            this.enumValues = Arrays.stream(constraintAnnotation.value()).collect(Collectors.toSet());
        } else {
            // 使用枚举类规定的value
            this.enumValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants()).map(Enum::name).collect(Collectors.toSet());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return value == null || enumValues == null || enumValues.contains(value);
    }
}
