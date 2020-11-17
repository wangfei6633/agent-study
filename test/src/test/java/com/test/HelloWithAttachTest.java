package com.test;

import com.attach.VmAttachHelper;
import demo.Hello;
/*
  javaassist 字节码修改时机：agentmain
*/
public class HelloWithAttachTest {
    public static void main(String[] args) {
        String agentPath = "D:\\agent-study\\agent-main\\target\\agent-main.jar";
        VmAttachHelper.attach(agentPath);
        Hello hello=new Hello();
        String name="wf";
        hello.sayA(name);
    }
}
