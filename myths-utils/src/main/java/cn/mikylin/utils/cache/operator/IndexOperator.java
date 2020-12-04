package cn.mikylin.utils.cache.operator;

import cn.mikylin.utils.cache.file.FileOperator;
import cn.mikylin.utils.cache.index.Index;
import cn.mikylin.utils.cache.serialize.Serialize;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * cache index operator.
 *
 * @author mikylin
 * @date 20201203
 */
public class IndexOperator<T extends Index> {

    private Serialize serialize;

    private Class<T> clz;

    private IndexSerializeRunnalbe r;

    private FileOperator operator;

    public IndexOperator(Serialize serialize, FileOperator operator,Class<T> clz) {
        this.serialize = serialize;
        this.clz = clz;
        this.operator = operator;

        r = new IndexSerializeRunnalbe(new LinkedBlockingQueue<>(),this);

        Thread t = new Thread(r);
        t.setDaemon(false);
        t.start();
    }


    public void ayncSerial(Index index) {
        r.tasks.add(index);
    }

    private void serialize(List<Index> indexs) {
        List<byte[]> ls = new ArrayList<>(indexs.size());
        int count = 0;
        for (Index i : indexs) {
            byte[] serial = this.serialize.serialize(i,clz);
            count = count + serial.length;
            ls.add(serial);
        }

        ByteBuffer buffer = ByteBuffer.allocate(count + ls.size() - 1);

        for (byte[] bs : ls) {
            buffer.put(bs);
            buffer.put((byte)0);
        }

        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes,0,bytes.length);

        operator.write(bytes);
    }





    private class IndexSerializeRunnalbe implements Runnable {

        BlockingQueue<Index> tasks;
        IndexOperator operator;

        IndexSerializeRunnalbe(BlockingQueue<Index> tasks,IndexOperator operator) {
            this.tasks = tasks;
            this.operator = operator;
        }


        @Override
        public void run() {
            for (;;) {

                List<Index> indexs = new LinkedList<>();
                Index index = tasks.poll();
                indexs.add(index);
                for (;;) {
                    if (tasks.isEmpty())
                        break;
                    indexs.add(tasks.poll());
                }

                operator.serialize(indexs);
            }
        }
    }


}
