package cn.mikylin.myths.common.concurrent.buffer;

import java.io.Serializable;

public class RangBuffer<T> {

    private Node<T>[] buffer;
    private int bufferSize;
    private volatile int readPoint;
    private long readTurns;
    private volatile int writePoint;
    private long writeTurns;

    public RangBuffer(int bufferSize){

        this.bufferSize = bufferSize;

        initBuffer();
    }

    private void initBuffer(){
        this.buffer = new Node[bufferSize];
        for(int i = 0 ; i < bufferSize ; i ++)
            buffer[i] = new Node<>();

    }



    private static class Node<T> implements Serializable {
        T t;

        Node() { }

        void write(T t){
            this.t = t;
        }

        T read(){
            return t;
        }
    }



    public T read(){
        T t = buffer[readPoint].read();
        readIncrease();
        return t;
    }

    private void readIncrease(){
        if(readPoint == bufferSize){
            readPoint = 0;
            readTurns ++;
            return;
        }
        readPoint ++;
    }


    public void write(T t){
        buffer[writePoint].write(t);
        writeIncrease();
    }

    private void writeIncrease(){
        if(writePoint == bufferSize){
            writePoint = 0;
            writeTurns ++;
            return;
        }
        writePoint ++;
    }

}
