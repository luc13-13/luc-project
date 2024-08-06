package com.lc.framework.web.condition;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Map;

import static org.springframework.boot.autoconfigure.condition.ConditionMessage.forCondition;
import static org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN;

/**
 * <pre>
 * 条件抽象类，为配置类创建Bean提供判断条件
 * Example：
 * 需要配置判断是否创建AbstractClient实现类
 *     {@code
 *     public abstract class AbstractClient {}
 *     public class ClientA extends AbstractClient {}
 *     public class ClientB extends AbstractClient {}
 *     }
 * （1）继承抽象类，重写方法
 *      {@code
 *     public final class OnEnableClient extends OnEnableComponent<AbstractClient> {
 *       @Override
 *       public String normalizeComponentName(Class<? extends AbstractClient> componentClass) {
 *         // 获取版本号
 *         String[] packageNameArray = componentClass.getPackage().getName().split("\\.");
 *         String version = packageNameArray[packageNameArray.length - 1];
 *         // 获取前缀
 *         String clientName = componentClass.getSimpleName().toLowerCase().replaceAll("Client", "");
 *         return clientName + "-" + version;
 *       }
 *
 *       @Override
 *       public Class<?> annotationClass() {
 *         // 返回条件注解
 *         return ConditionalOnEnabledClient.class;
 *       }
 *
 *       @Override
 *       public Class<? extends AbstractClient> defaultValueClass() {
 *         return DefaultClient.class;
 *       }
 *
 *       public static class DefaultClient extends AbstractClient {
 *            public DefaultClient(String endpoint, String version, Credential credential, String region) {
 *                   super(endpoint, version, credential, region);
 *            }
 *       }
 *     }
 * }
 * （2）创建条件注解
 *     {@code
 *        @Retention(RetentionPolicy.RUNTIME)
 *        @Conditional(OnEnableTceClient.class)
 *        @Target(ElementType.TYPE, ElementType.METHOD)
 *        @Documented
 *        public @interface ConditionalOnEnabledClient {
 *          Class<?extends AbstractClient> value()default OnEnableClient.DefaultClient.class;
 *        }
 *     }
 * （3）配置类中应用条件注解
 *      {@code
 *      @Configuration
 *      public class TceClientAutoConfiguration implements InitializingBean {
 *          @Bean
 *          @ConditionalOnEnabledClient
 *          public ClientA clientA() {
 *              return new ClientA();
 *          }
 *
 *          @Bean
 *          @ConditionalOnEnabledClient
 *          public ClientB clientB() {
 *              return new ClientB();
 *          }
 *      }
 * }
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2024/6/26 11:21
 */
public abstract class OnEnableComponent<T>  extends SpringBootCondition implements ConfigurationCondition {

    private static final String SUFFIX = ".enabled";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Class<? extends T> candidate = getComponentType(annotationClass(), context, metadata);
        return determineOutcome(candidate, context.getEnvironment());
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends T> getComponentType(Class<?> annotationClass, ConditionContext context,
                                                  AnnotatedTypeMetadata metadata) {
        // 如果注解指定了Class，则取value
        Map<String, Object> attributes = metadata.getAnnotationAttributes(annotationClass.getName());
        if (attributes != null && attributes.containsKey("value")) {
            Class<?> target = (Class<?>) attributes.get("value");
            if (target != defaultValueClass()) {
                return (Class<? extends T>) target;
            }
        }
        Assert.state(metadata instanceof MethodMetadata && metadata.isAnnotated(Bean.class.getName()),
                getClass().getSimpleName() + " must be used on @Bean methods when the value is not specified");
        MethodMetadata methodMetadata = (MethodMetadata) metadata;
        // 注解没有指定，则根据方法返回值获取类型
        try {
            return (Class<? extends T>) ClassUtils.forName(methodMetadata.getReturnTypeName(),
                    context.getClassLoader());
        }
        catch (Throwable ex) {
            throw new IllegalStateException("Failed to extract component class for "
                    + methodMetadata.getDeclaringClassName() + "." + methodMetadata.getMethodName(), ex);
        }
    }

    private ConditionOutcome determineOutcome(Class<? extends T> componentClass, PropertyResolver resolver) {
        // 如果环境变量中配置了 getPrefix() + "类名" + getSuffix() 为false，则不加载bean
        String key =  getPrefix() + normalizeComponentName(componentClass) + getSuffix();
        ConditionMessage.Builder messageBuilder = forCondition(annotationClass().getName(), componentClass.getName());
        if ("false".equalsIgnoreCase(resolver.getProperty(key))) {
            return ConditionOutcome.noMatch(messageBuilder.because("bean is not available"));
        }
        return ConditionOutcome.match();
    }

    protected abstract String getPrefix();

    protected String getSuffix() {
        return SUFFIX;
    }

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return REGISTER_BEAN;
    }

    /**
     * 提供创建Bean的名称
     * @param componentClass 将要创建的Bean.class
     * @return 将要创建Bean的名称
     */
    protected abstract String normalizeComponentName(Class<? extends T> componentClass);

    /**
     * @return 获取条件注解
     */
    protected abstract Class<?> annotationClass();

    protected abstract Class<? extends T> defaultValueClass();


}
