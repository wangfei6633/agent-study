package com.wangf.agent.javaassist.enhance.method.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wangf.agent.javaassist.TrackLog;
import com.wangf.agent.javaassist.enhance.execution.AnnotationMethodExecution;
import com.wangf.agent.javaassist.enhance.method.MethodEnhance;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author wangfei
 */
public class AnnotationMethodEnhanceImpl extends MethodEnhance {
    private ClassPool classPool = ClassPool.getDefault();
    private final String ANNOTATION = "annotation";

    public AnnotationMethodEnhanceImpl() {
    }

    @Override
    public Map<String, Object> matchMethod(String className, String methodFullName) throws Throwable {
        if (StringUtils.isEmpty(className)) {
            return null;
        }

        String methodName = getSimpleMethodName(methodFullName);
        String[] params = getMethodParameter(methodFullName);
        CtClass ctClass = classPool.getCtClass(className);
        CtClass[] paramCtclasses = new CtClass[params.length];
        for (int i = 0; i < params.length; i++) {
            CtClass paramCtclass = classPool.getCtClass(params[i]);
            paramCtclasses[i] = paramCtclass;
        }

        CtMethod ctMethod = ctClass.getDeclaredMethod(methodName, paramCtclasses);
        Object annotation = ctMethod.getAnnotation(TrackLog.class);
        if (annotation != null) {
            Map<String, Object> result = Maps.newHashMap();
            TrackLog trajectoryLog = (TrackLog) annotation;
            result.put(ANNOTATION, trajectoryLog.value());
            return result;
        }

        return null;
    }

    @Override
    public void before(CtMethod ctMethod, Map<String, Object> param) throws Throwable {
        classPool.importPackage(AnnotationMethodExecution.class.getPackage().getName());
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("try {\n");
        stringBuilder.append(appendClassnameAndMethodName(ctMethod));

        CtClass ctClass = classPool.get(String.class.getName());
        stringBuilder.append("String _annotationValue_ = ");
        Object annotation = param.get(ANNOTATION);
        if (annotation != null) {
            stringBuilder.append("\"").append(annotation).append("\"");
        } else {
            stringBuilder.append("null");
        }
        stringBuilder.append(";\n");
        ctMethod.addLocalVariable("_annotationValue_", ctClass);
        ctClass.detach();

        stringBuilder.append("AnnotationMethodExecution _$methodExecution$_ = AnnotationMethodExecution.getInstance();\n");
        stringBuilder.append("_$methodExecution$_.methodBefore(_className_, _methodName_, $sig, $args, _annotationValue_);");
        stringBuilder.append("} catch (Throwable _$throwable$_){}");
//        System.out.println("[before]:"+stringBuilder.toString());
        ctMethod.insertBefore(stringBuilder.toString());
    }

    @Override
    public void afterReturning(CtMethod ctMethod, Map<String, Object> param) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("try {\n");
        stringBuilder.append(appendClassnameAndMethodName(ctMethod));
        stringBuilder.append("AnnotationMethodExecution _$methodExecution$_ = AnnotationMethodExecution.getInstance();\n");
        stringBuilder.append("_$methodExecution$_.afterReturning(_className_, _methodName_, ($w)$_);");
        stringBuilder.append("} catch (Throwable _$throwable$_){}");
//        System.out.println("[after]:"+stringBuilder.toString());
        ctMethod.insertAfter(stringBuilder.toString(), false);

    }

    @Override
    public void afterCatch(CtMethod ctMethod, Map<String, Object> param) throws Throwable {
        classPool.importPackage(AnnotationMethodExecution.class.getPackage().getName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appendClassnameAndMethodName(ctMethod));
        stringBuilder.append("AnnotationMethodExecution _$methodExecution$_ = AnnotationMethodExecution.getInstance();\n");
        stringBuilder.append("_$methodExecution$_.afterCatch(_className_, _methodName_, $e);\n");
        stringBuilder.append("throw $e;\n");
        CtClass exceptionType = classPool.get("java.lang.Throwable");
        ctMethod.addCatch(stringBuilder.toString(), exceptionType);
        exceptionType.detach();
    }

    private String appendClassnameAndMethodName(CtMethod ctMethod) throws Throwable {
        String className = ctMethod.getDeclaringClass().getName();
        String methodName = ctMethod.getName();
        StringBuilder stringBuilder = new StringBuilder();
        CtClass ctClass = classPool.get(String.class.getName());
        stringBuilder.append("String _className_ = ").append("\"");
        stringBuilder.append(className).append("\"").append(";\n");
        stringBuilder.append("String _methodName_ = ").append("\"");
        stringBuilder.append(methodName).append("\"").append(";\n");
        ctMethod.addLocalVariable("_className_", ctClass);
        ctMethod.addLocalVariable("_methodName_", ctClass);
        ctClass.detach();
        return stringBuilder.toString();
    }
}
