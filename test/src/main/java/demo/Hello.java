package demo;


import com.wangf.agent.javaassist.TrackLog;

/**
 * @author wangfei
 */
public class Hello {
    @TrackLog(value = "A")
    public void sayA(String name) {
        System.out.println("say  hello  to "+name);
        sayB();
    }
    public void sayB() {
        System.out.println(" say  hello to B ");
    }
}
