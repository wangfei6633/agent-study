package com.wangf.agent.javaassist;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class AnnotationAgent {
    private static Instrumentation instrumentation;
    public static void agentmain(String agentArgs, Instrumentation inst)
            throws UnmodifiableClassException {
        instrumentation = inst;

        if (inst == null){
            return;
        }
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
