package cn.mikylin.myths.litjson.write;

import cn.mikylin.myths.litjson.OptionBox;
import java.io.StringWriter;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Json String writer
 * @author mikylin
 */
public class JWriter {

    private ReentrantLock lock;

    private WriteObjectBuilder builder;
    private StringWriter writer;
    private boolean isWriteFinish;

    private WriteManager manager;

    public JWriter(Object object){
        this(object, new WriteManager(OptionBox.OptionBoxBuilder.builder().over()));
    }

    public JWriter(Object object, WriteManager manager){
        writer = new StringWriter();

        if(Objects.nonNull(object)){
            this.manager = manager;
            builder = new WriteObjectBuilder(object,manager);
            isWriteFinish = false;
        }else{
            writer.append("null");
            isWriteFinish = true;
        }

        lock = new ReentrantLock();
    }

    public String toJson(){
        lock.lock();
        if(!isWriteFinish){
            isWriteFinish = true;
            manager.distribute(builder,writer);
        }
        lock.unlock();
        return writer.toString();
    }

}
