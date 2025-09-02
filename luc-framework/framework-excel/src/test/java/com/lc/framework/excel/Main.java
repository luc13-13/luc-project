package com.lc.framework.excel;

import com.lc.framework.excel.entity.ModelA;
import com.lc.framework.excel.utils.EasyExcelUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Lu Cheng
 * @date 2023-08-09 15:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)})
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class Main {
    static List<ModelA> models = new ArrayList<>();

    static {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                models.add(ModelA.builder()
                        .orderId(String.valueOf(i))
                        .instanceId("ins-" + (j / 2))
                        .flavorId("flavor-" + (j / 2))
                        .operateProperty("property-" + (j / 2))
                        .source("source")
                        .current("pageIndex")
                        .build());
            }
        }
    }

    @Test
    public void test() {
        EasyExcelUtils.export(ModelA.class, models, "D:\\lucDocuments\\luc-gitee\\test01.xlsx");
    }

    public void testList() {

    }
}
