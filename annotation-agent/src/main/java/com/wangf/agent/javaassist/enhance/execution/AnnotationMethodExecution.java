package com.wangf.agent.javaassist.enhance.execution;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangf.agent.javaassist.TrackEnum;
import com.wangf.agent.javaassist.TrackInfo;
import com.wangf.agent.javaassist.TrackLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Stack;

public class AnnotationMethodExecution {
    private static Logger logger = LoggerFactory.getLogger(AnnotationMethodExecution.class);
    protected static ThreadLocal<Stack<TrackInfo>> trackStackInheritable = new ThreadLocal<Stack<TrackInfo>>();

    private AnnotationMethodExecution() {

    }

    private static class AnnotationMethodExecutionHolder {
        static AnnotationMethodExecution methodExecution = new AnnotationMethodExecution();
    }

    public static AnnotationMethodExecution getInstance() {
        return AnnotationMethodExecutionHolder.methodExecution;
    }

    protected String buildParamsString( Object[] params){
        StringBuilder stringBuilder = new StringBuilder();
        if (params != null && params.length > 0){
            stringBuilder.append("{");
            for (int i = 0; i < params.length; i++){
                try {
                    Object param = params[i];
                    Class<?> paramClazz = params[i].getClass();
                    // fix bug getOutputStream() has already been called for this response

                    stringBuilder.append("\"")
                            .append(paramClazz.getSimpleName())
                            .append("_").append(i)
                            .append("\":\"")
                            .append(JSON.toJSONString(param))
                            .append("\",");
                }catch (Throwable throwable){
                    continue;
                }
            }
            if (stringBuilder.length() > 1){
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }
    public final void methodBefore(String className, String methodName, Class<?>[] paramsType, Object[] args, String value) {
        try {
                System.out.println("[before]className = [" + className + "], methodName = [" + methodName + "], paramsType = [" + paramsType + "], args = [" + buildParamsString(args) + "], value = [" + value + "]");

            Stack<TrackInfo> stack = trackStackInheritable.get();
            if (stack == null) {
                stack = new Stack<TrackInfo>();
                trackStackInheritable.set(stack);
            }

            long start = System.currentTimeMillis();
            TrackInfo trackInfo = new TrackInfo();
            trackInfo.setStartTime(start);
            stack.add(trackInfo);
        } catch (Exception e) {
            //织入代码不影响正常业务执行
            logger.error("AnnotationMethodExecution methodBefore error", e);
        }
    }

    public final void afterReturning(String className, String methodName, Object result) {
        try {
            popTrackInfo();
            System.out.println("[after]className = [" + className + "], methodName = [" + methodName + "], result = [" + result + "]");
        } catch (Exception e) {
            //织入代码不影响正常业务执行
            logger.error("AnnotationMethodExecution afterReturning error", e);
        }
    }

    public final void afterCatch(String className, String methodName, Throwable exception) {
        try {
            popTrackInfo();
        } catch (Exception e) {
            //织入代码不影响正常业务执行
            logger.error("AnnotationMethodExecution afterReturning error", e);
        }
    }

    public void popTrackInfo() {
        Stack<TrackInfo> stack = trackStackInheritable.get();
        if (stack.empty()) {
            return;
        }
        TrackInfo info = stack.pop();
        if (info != null) {
            info.setEndTime(System.currentTimeMillis());
            long cost = System.currentTimeMillis() - info.getStartTime();
            info.setCost(cost);
            logger.info(JSONObject.toJSONString(info));
        }
    }
}
