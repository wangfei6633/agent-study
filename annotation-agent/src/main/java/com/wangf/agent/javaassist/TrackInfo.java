package com.wangf.agent.javaassist;


import java.io.Serializable;


/**
 * @author wangfei
 */
public class TrackInfo implements Serializable {

    private static final long serialVersionUID = 4502576980002652016L;
    /**
     * 开始时间
     */
    private long startTime;
    /**
     * 结束时间
     */
    private long endTime;
    /**
     * 耗时
     */
    private long cost;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
