package com.wangf.agent.javaassist;

import com.wangf.agent.javaassist.enhance.method.MethodEnhance;
import com.wangf.agent.javaassist.enhance.method.impl.AnnotationMethodEnhanceImpl;
import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassFileTransformerImpl implements ClassFileTransformer {
    private ClassPool classPool = ClassPool.getDefault();
    private final List<String> includeClassName = new ArrayList<String>();
    private final List<String> excludeMethodName = new ArrayList<String>();
    private MethodEnhance enhance = new AnnotationMethodEnhanceImpl();

    public ClassFileTransformerImpl() {
        includeClassName.add("demo");
        excludeMethodName.add("access$0");
        excludeMethodName.add("access$000");
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        CtClass ctClass = null;
        ClassPath byteArrayClassPath = null;
        try {
            if (className == null || excludeStartWithClassName(className)) {
                return null;
            }
            System.out.println("loader = [" + loader + "], className = [" + className + "], classBeingRedefined = [" + classBeingRedefined + "], protectionDomain = [" + protectionDomain + "], classfileBuffer = [" + classfileBuffer + "]");
            String fixedClassName = className.replace("/", ".");
            byteArrayClassPath = new ByteArrayClassPath(fixedClassName, classfileBuffer);
            classPool.insertClassPath(byteArrayClassPath);
            ctClass = classPool.getCtClass(fixedClassName);
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();
            if (ctMethods != null && ctMethods.length > 0) {
                for (CtMethod method : ctMethods) {
                    if (!Modifier.isAbstract(method.getModifiers())
                            && !excludeMethodName.contains(method.getName())
                            && !Modifier.isNative(method.getModifiers())) {
                        /**
                         * 不是abstract native 和不在排除列表中的方法增强.
                         */
                        methodEnhance(method);
                    }
                }
                return ctClass.toBytecode();
            }
        } catch (Throwable e) {

        }
        return classfileBuffer;
    }

    public void methodEnhance(CtMethod ctMethod) throws Throwable {
        try {
            CtClass ctClass = ctMethod.getDeclaringClass();
            Map<String, Object> param = enhance.matchMethod(ctClass.getName(), ctMethod.getLongName());
            if (param != null) {
                enhance.before(ctMethod, param);
                enhance.afterReturning(ctMethod, param);
                enhance.afterCatch(ctMethod, param);
                System.out.println("方法: " + ctMethod.getLongName() + " 增强成功!!!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean excludeStartWithClassName(String className) {
        for (String exclude : includeClassName) {
            if (className.startsWith(exclude)) {
                return false;
            }
        }
        return true;
    }
}
