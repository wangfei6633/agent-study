package com.test;

import com.wangf.agent.javaassist.VmAttachHelper2;
import demo.Hello;

public class HelloWithAnnotationTest {
    public static void main(String[] args) {
        String agentPath = "/Users/wangfei/Devlop/openSource/agent-study/annotation-agent/target/annotation-agent.jar";
        VmAttachHelper2.attach(agentPath);
        Hello hello=new Hello();
        hello.sayA();
    }
}
