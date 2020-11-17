package com.test;

import com.wangf.agent.javaassist.VmAttachHelper2;

public class HelloWithAnnotationTest {
    public static void main(String[] args) {
        String agentPath = "D:\\agent-study\\annotation-agent\\target\\annotation-agent.jar";
//        String pid="9405";
        String pid=null;
        VmAttachHelper2.attach(pid,agentPath);
//        Hello hello=new Hello();
//        String name="wf";
//        hello.sayA(name);

    }
}
