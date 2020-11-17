package com.attach;


import org.apache.commons.lang3.StringUtils;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangfei
 */
public class VmAttachHelper {

    private static final String AGENT_CLASS_NAME = "com.attach.MyAgent";
    /**
     * VirtualMachine
     */
    private static Object virtualmachineObject = null;
    /**
     * VirtualMachine.class
     */
    private static Class<?> virtualmachineClass = null;

    /**
     * 是否使用AppLoader加载的此类
     */
    private static AtomicBoolean agentLoadedUseAppLoader = new AtomicBoolean(false);

    public static AtomicBoolean getAgentLoadedUseAppLoader() {
        return agentLoadedUseAppLoader;
    }

    public static void attach(String agentPath) {
        try {
            // 保证只初始化一遍
            Class.forName("sun.jvmstat.monitor.MonitoredHost");
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];

            MonitoredHost local = MonitoredHost.getMonitoredHost("localhost");

            MonitoredVm vm = local.getMonitoredVm(new VmIdentifier("//" + pid));
            boolean attachable = MonitoredVmUtil.isAttachable(vm);


            if (attachable) {
                attachPid(pid);
                // 指定agent.jar绝对路径，仅供应本地调试
                if(StringUtils.isEmpty(agentPath)){
                     agentPath = getAgentPath();
                }
                if (!agentPath.endsWith(".jar")) {
                    return;
                }

                loadAgent(agentPath);

                if (!agentLoadedUseAppLoader.get()) {
                    Instrumentation inst = getInstrumentation();
                    if (inst == null) {
                        return;
                    }
                    inst.addTransformer(new MyTransformer());
                }
            }
        } catch (Throwable e) {
        } finally {
            try {
                detach();
            } catch (Exception e) {
            }
        }
    }

    private static void detach() throws Exception {

        Method detach = virtualmachineClass.getDeclaredMethod("detach", new Class[0]);

        detach.invoke(virtualmachineObject, new Object[0]);

    }

    private static void attachPid(String pid) throws Exception {

        virtualmachineClass = Class.forName("com.sun.tools.attach.VirtualMachine", true, VmAttachHelper.class.getClassLoader());

        Method attach = virtualmachineClass.getDeclaredMethod("attach", String.class);

        virtualmachineObject = attach.invoke(virtualmachineClass, new Object[]{pid});

    }

    private static String getAgentPath() {

        String selfpath = VmAttachHelper.class.getProtectionDomain().getCodeSource().getLocation().getFile();


        File file = new File(selfpath);

        if (file.exists()) {

            return file.getAbsolutePath();

        } else {


            return null;
        }
    }

    private static void loadAgent(String agentPath) throws Exception {

        Method loadAgent = virtualmachineClass.getDeclaredMethod("loadAgent", String.class);

        loadAgent.invoke(virtualmachineObject, new Object[]{agentPath});

    }

    public static Instrumentation getInstrumentation() {
        Instrumentation inst = null;
        try {
            // 使用系统classloader才能加载到 带有inst 变量的类
            Class<?> agentClass = Class.forName(AGENT_CLASS_NAME, true, ClassLoader.getSystemClassLoader());

            Method getInstrumentationMethod = agentClass.getMethod("getInstrumentation", new Class[0]);

            inst = (Instrumentation) getInstrumentationMethod.invoke(agentClass, new Object[0]);
        } catch (Exception e) {
        }
        return inst;
    }
}
