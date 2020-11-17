package com.test;


import demo.Hello;

public class HelloWithAgentTest {
    /**
     *  vmoption -javaagent:/Users/wangfei/Devlop/openSource/agent-study/premain-agent/target/premain-agent.jar
     * @param args
     */
    public static void main(String[] args) {
        Hello hello=new Hello();
        hello.sayA();
    }
}
