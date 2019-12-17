package cn.mikylin.myths.common;

public final class ObjectMonitorUtils {

    public static void wait(Object o) {
        if(o != null)
            synchronized (o) {
                try {
                    o.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException("wait exception.");
                }
            }

    }

    public static void notifyAll(Object o) {
        if(o != null)
            synchronized (o) {
                o.notifyAll();
            }
    }

    public static void notify(Object o) {
        if(o != null)
            synchronized (o) {
                o.notify();
            }
    }
}
