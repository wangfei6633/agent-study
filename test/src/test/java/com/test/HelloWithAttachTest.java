package com.test;

import com.attach.VmAttachHelper;
import demo.Hello;

public class HelloWithAttachTest {

    public static void main(String[] args) {
        String agentPath = "/Users/wangfei/Devlop/openSource/agent-study/agent-main/target/agent-main.jar";
        VmAttachHelper.attach(agentPath);
        Hello hello=new Hello();
        String name="wf";
        hello.sayA(name);
    }
}
