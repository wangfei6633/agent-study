package com.wangf.agent.javaassist;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @author wangfei
 */
public class AnnotationAgent {
    private static Instrumentation instrumentation;
    static {
        System.out.println("类加载------------AnnotationAgent--------------");
    }
    public static void agentmain(String agentArgs, Instrumentation inst)
            throws UnmodifiableClassException {
        try {
            instrumentation = inst;

            if (inst == null){
                return;
            }
            ClassFileTransformer classFileTransformer = new ClassFileTransformerImpl();
            inst.addTransformer(classFileTransformer, true);
          } catch (Exception e) {
        }

    }

    public static Instrumentation getInstrumentation() {

        return instrumentation;
    }
}
