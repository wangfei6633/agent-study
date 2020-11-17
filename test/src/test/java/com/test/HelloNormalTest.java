package com.test;

import demo.Hello;

import java.lang.management.ManagementFactory;

public class HelloNormalTest {

    public static void main(String[] args) {
        int i = 0;
        try {
            while (true) {
                System.out.println(i+"---pid = [" + getPid() + "]");
                i++;
                Hello hello = new Hello();
                hello.sayA("lhy");
                Thread.sleep(10000l);
            System.out.println("-------------continue----------------");

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getPid() {
        String pid = null;
        try {
            Class.forName("sun.jvmstat.monitor.MonitoredHost");
            String name = ManagementFactory.getRuntimeMXBean().getName();
            pid = name.split("@")[0];
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pid;
    }
}
