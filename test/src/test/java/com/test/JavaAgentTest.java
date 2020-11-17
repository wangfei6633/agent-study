package com.test;


import demo.Hello;
/*
  javaassist 字节码修改时机：premain
*/
public class JavaAgentTest {
    /**
     *  vmoption -javaagent:D:\agent-study\premain-agent\target\premain-agent.jar
     * @param args
     */
    public static void main(String[] args) {
        Hello hello=new Hello();
        String name="wf";
        hello.sayA(name);
    }
}
