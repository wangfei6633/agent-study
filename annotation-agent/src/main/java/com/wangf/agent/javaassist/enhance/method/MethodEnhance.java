package com.wangf.agent.javaassist.enhance.method;

import javassist.CtMethod;

import java.util.Map;

public abstract class MethodEnhance {
    public abstract Map<String,Object> matchMethod(String className, String methodFullName) throws Throwable;
    public abstract void before(CtMethod ctMethod, Map<String,Object> param) throws Throwable;
    public abstract void afterReturning(CtMethod ctMethod, Map<String,Object> param) throws Throwable;
    public abstract void afterCatch(CtMethod ctMethod, Map<String,Object> param) throws Throwable;

    protected String getSimpleMethodName(String methodFullName) {
        if (methodFullName == null || methodFullName.isEmpty()){
            return null;
        }

        String methodName = methodFullName.substring(0, methodFullName.lastIndexOf('('));
        methodName = methodName.substring(methodName.lastIndexOf('.') + 1);

        return methodName;
    }

    protected String[] getMethodParameter(String methodFullName) {
        if (methodFullName == null || methodFullName.isEmpty()){
            return null;
        }

        String methodParams = methodFullName.substring(methodFullName.lastIndexOf('(') + 1, methodFullName.lastIndexOf(')'));
        if (methodParams.trim().isEmpty()){
            return new String[0];
        }

        return methodParams.split(",");
    }
}
