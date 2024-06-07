package com.ihrm.domain.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author VectorX
 * @version V1.0
 * @description
 * @date 2024-06-07 09:20:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAttribute
{
    /**
     * 列名
     *
     * @return {@link String }
     */
    String name() default "";

    /**
     * 列序号
     *
     * @return int
     */
    int index();

    /**
     * 字段类型对应的格式
     *
     * @return {@link String }
     */
    String format() default "";
}
