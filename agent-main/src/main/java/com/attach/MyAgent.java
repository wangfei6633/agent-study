package com.attach;

import java.lang.instrument.Instrumentation;

/**
 * @ Author     ：lihuiyue.
 * @ Date       ：Created in 15:38 2020/11/16
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class MyAgent {
    private static Instrumentation instrumentation;
    public static void agentmain(String args, Instrumentation inst){
        System.out.println("Hi, I'm agent!");
        instrumentation =inst;
        return ;
    }
    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
