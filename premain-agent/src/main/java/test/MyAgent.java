package test;

import java.lang.instrument.Instrumentation;

/**
 * @ Author     ：lihuiyue.
 * @ Date       ：Created in 15:38 2020/11/16
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class MyAgent {
    public static void premain(String args, Instrumentation inst){
        System.out.println("Hi, I'm agent!");
        inst.addTransformer(new MyTransformer());
    }

}
