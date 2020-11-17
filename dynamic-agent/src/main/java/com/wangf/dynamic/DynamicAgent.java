package com.wangf.dynamic;

import com.wangf.agent.javaassist.ClassFileTransformerImpl;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @author wangfei
 */
public class DynamicAgent {
    public static void agentmain(String agentArgs, Instrumentation inst)
            throws UnmodifiableClassException {
        try {
            if (inst == null) {
                return;
            }
            ClassFileTransformer classFileTransformer = new ClassFileTransformerImpl();
            inst.addTransformer(classFileTransformer, true);
            //需要指定类 重新加载一下
            inst.retransformClasses(Class.forName("demo.Hello"));
            System.out.println("classFileTransformer = [" + classFileTransformer + "]");
        } catch (Exception e) {
        }

    }
}
