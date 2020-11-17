package com.wangf.agent.javaassist;


import java.lang.annotation.*;

/**
 * @Author Lee
 * @Description 轨迹日志注解
 * @Create 2020/4/21 13:59
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TrackLog {
    /** 方法描述 */
    String value();

    /**
     * 轨迹类型 中间结果 最终结果 其他
     */
    TrackEnum type() default TrackEnum.OTHER;
}
