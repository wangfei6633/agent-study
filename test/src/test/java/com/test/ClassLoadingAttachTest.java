package com.test;

import com.attach.VmAttachHelper;
import demo.Hello;
/*
  javaassist 字节码修改时机：agentmain
*/
public class ClassLoadingAttachTest {
    public static void main(String[] args) {
        String agentPath = "/Users/wangfei/Devlop/openSource/agent-study/annotation-agent/target/annotation-agent.jar";
        VmAttachHelper.attach(agentPath);
        Hello hello=new Hello();
        String name="wf";
        hello.sayA(name);
    }
}
