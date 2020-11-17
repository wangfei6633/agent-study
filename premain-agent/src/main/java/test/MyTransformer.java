package test;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * 检测方法的执行时间
 * @author wangfei
 */
public class MyTransformer implements ClassFileTransformer {
    private final List<String> includeClassName = new ArrayList<String>();
    private final List<String> excludeMethodName = new ArrayList<String>();

    final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";
    final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";

    public MyTransformer() {
        includeClassName.add("demo");
        excludeMethodName.add("access$0");
        excludeMethodName.add("access$000");

    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        //java自带的方法不进行处理
        if (className == null || excludeStartWithClassName(className)) {
            return null;
        }
        className = className.replace("/", ".");
        CtClass ctclass = null;
        try {
            // 使用全称,用于取得字节码类<使用javassist>
            ctclass = ClassPool.getDefault().get(className);
            for (CtMethod ctMethod : ctclass.getDeclaredMethods()) {
                String methodName = ctMethod.getName();
                // 新定义一个方法叫做比如sayHello$old
                String newMethodName = methodName + "$old";
                // 将原来的方法名字修改
                ctMethod.setName(newMethodName);

                // 创建新的方法，复制原来的方法，名字为原来的名字
                CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctclass, null);

                // 构建新的方法体
                StringBuilder bodyStr = new StringBuilder();
                bodyStr.append("{");
                bodyStr.append("System.out.println(\"==============Enter Method: " + className + "." + methodName + " ==============\");");
                bodyStr.append(prefix);
                // 调用原有代码，类似于method();($$)表示所有的参数
                bodyStr.append(newMethodName + "($$);\n");
                bodyStr.append(postfix);
                bodyStr.append("System.out.println(\"==============Exit Method: " + className + "." + methodName + " Cost:\" +(endTime - startTime) +\"ms " + "===\");");
                bodyStr.append("}");
                // 替换新方法
                newMethod.setBody(bodyStr.toString());
                // 增加新方法
                ctclass.addMethod(newMethod);
            }
            return ctclass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean excludeStartWithClassName(String className) {
        for (String exclude: includeClassName){
            if (className.startsWith(exclude)){
                return false;
            }
        }
        return true;
    }
}
