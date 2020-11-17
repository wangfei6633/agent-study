package com.test;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.wangf.agent.javaassist.VmAttachHelper2;

import java.util.List;

public class AttachOtherThreadTest {
    public static void main(String[] args) {
        String agentPath = "/Users/wangfei/Devlop/openSource/agent-study/annotation-agent/target/annotation-agent.jar";
        try {
            String prefix = "com.test.HelloNormalTest";
            List<VirtualMachineDescriptor> vmlist = VirtualMachine.list();
            for (VirtualMachineDescriptor vmd : vmlist) {
                if (vmd.displayName().startsWith(prefix)) {
                    System.out.println("pid = [" + vmd.id() + "]");
                    VmAttachHelper2.attach(vmd.id(), agentPath);
//                    VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
//                    virtualMachine.loadAgent(agentPath);
                }
            }
            Thread.sleep(100000);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Hello hello=new Hello();
//        String name="wf";
//        hello.sayA(name);

    }
}
