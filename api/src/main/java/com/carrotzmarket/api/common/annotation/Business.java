package com.carrotzmarket.api.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 다른 어노테이션의 대상이 될 수 있는 타켓이 되게 해줌. 특정 위치에 적용될 수 있게 하는 어노테이션
@Retention(RetentionPolicy.RUNTIME) // 컴파일 후 실행시점에서 어떻게 보관되는지 설정하는 옵션(?) /  어노테이션의 수명이 어떻게 되느냐?
@Service
public @interface Business { // 여러 도메인이나 외부 작업을 처리하는 어노테이션

    @AliasFor(
            annotation = Service.class
    )
    String value() default "";

}
