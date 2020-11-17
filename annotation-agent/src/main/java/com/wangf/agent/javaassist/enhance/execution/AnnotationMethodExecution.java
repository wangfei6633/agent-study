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

    public final void methodBefore(String className, String methodName, Class<?>[] paramsType, Object[] args, String value) {
        try {
            System.out.println("[before]className = [" + className + "], methodName = [" + methodName + "], paramsType = [" + paramsType + "], args = [" + args + "], value = [" + value + "]");

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
