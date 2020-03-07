package cn.mikylin.myths.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ThreadFactory;

/**
 * special group thread factory.
 *
 * @author mikylin
 * @date 20200221
 */
public class SpecialGroupThreadFactory implements ThreadFactory {

    private ThreadGroup group;
    private String factoryName;
    private ThreadNameCreator creator;

    public SpecialGroupThreadFactory(String groupName, String factoryName,
                                     boolean isDaemon,ThreadNameCreator creator) {
        init(groupName,factoryName,isDaemon,creator);
    }

    private SpecialGroupThreadFactory() {
        int seq = (int)SEQUENCE.getAndAdd(this, 1);
        init("special-group-"+ seq,"special-factory-"+ seq,
                false,DEFAULT_CREATOR);
    }

    private void init(String groupName, String factoryName,
                      boolean isDaemon,ThreadNameCreator creator) {
        this.group = new ThreadGroup(groupName);
        this.group.setDaemon(isDaemon);
        this.factoryName = factoryName;
        this.creator = creator;
    }


    public String groupName() {
        return group.getName();
    }

    public ThreadGroup group() {
        return group;
    }

    public String factoryName() {
        return factoryName;
    }

    public Thread findThread(long id) {
        ThreadGroup group = group();
        Thread[] ts;
        synchronized (this) {
            ts = new Thread[group.activeCount()];
            group.enumerate(ts);
        }
        for(Thread t : ts) {
            if(t.getId() == id)
                return t;
        }

        return null;
    }


    @Override
    public Thread newThread(Runnable r) {
        Thread t;
        synchronized (this) {
            t = new Thread(group,r);
            t.setName(creator.getName(this));
        }
        return t;
    }


    public interface ThreadNameCreator {
        String getName(SpecialGroupThreadFactory factory);
    }

    private static ThreadNameCreator DEFAULT_CREATOR = factory -> factory.factoryName() + "-";



    private static int sequence = 0;
    private static VarHandle SEQUENCE;
    static {
        try {
            SEQUENCE = MethodHandles.lookup()
                    .findStaticVarHandle(SpecialGroupThreadFactory.class, "sequence", int.class);
        } catch (Exception e) {
            throw new RuntimeException("sequence init exception.");
        }
    }

}
