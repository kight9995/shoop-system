package com.zxst.shoop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
//@Aspect  将容器管理普通的javabean变成切面bean
@Aspect
@Component
public class TimerUtilAspect {

   @Around("execution(* com.zxst.shoop.service.impl.*.*(..))")
   public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
       //记录起始时间
       long start = System.currentTimeMillis();
       //执行业务方法
       Object result = joinPoint.proceed();
       //记录结束时间
       long end = System.currentTimeMillis();
       System.out.println("当前业务耗费"+(end - start)+"ms");
       return result;
   }
}
