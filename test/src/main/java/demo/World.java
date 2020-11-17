package demo;

import com.wangf.agent.javaassist.TrackLog;

public class World {
    @TrackLog(value = "byebye")
    public void bye(String name){
        System.out.println("say to name = [" + name + "]  byebye");
    }
}
