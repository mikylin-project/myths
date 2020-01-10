package cn.mikylin.myths.common.concurrent;

import cn.mikylin.myths.common.lang.CloneObjectAdapter;

/**
 * priority runnable
 *
 * @author mikylin
 * @date 20200110
 */
public class PriorityRunnableTask extends CloneObjectAdapter<PriorityRunnableTask>
        implements Runnable,Comparable<PriorityRunnableTask> {

    private Runnable r;
    private int prority;

    public PriorityRunnableTask(Runnable r, int prority) {
        this.r = r;
        this.prority = prority;
    }

    public PriorityRunnableTask(Runnable r) {
        this(r,1);
    }


    @Override
    public int compareTo(PriorityRunnableTask o) {
        return prority >= o.prority ? 1 : -1;
    }

    @Override
    public void run() {
        r.run();
    }


    public static PriorityRunnableTask create(Runnable r, int prority) {
        return new PriorityRunnableTask(r,prority);
    }

    public static PriorityRunnableTask create(Runnable r) {
        if(r instanceof PriorityRunnableTask)
            return (PriorityRunnableTask)r;
        return new PriorityRunnableTask(r);
    }
}