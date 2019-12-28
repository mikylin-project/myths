package cn.mikylin.myths.actor;

public interface Actor<T> {

    ActorManager getManager();

    void setMessage(Message<T> m);


}
