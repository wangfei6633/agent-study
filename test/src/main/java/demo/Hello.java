package demo;


import com.wangf.agent.javaassist.TrackLog;

/**
 * @author wangfei
 */
public class Hello {
    @TrackLog(value = "A")
    public void sayA() {
        System.out.println("say  hello  to A");
        sayB();
    }
    public void sayB() {
        System.out.println(" say  hello to B ");
    }
}
