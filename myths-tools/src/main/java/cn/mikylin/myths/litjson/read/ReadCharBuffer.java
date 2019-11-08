package cn.mikylin.myths.litjson.read;

import cn.mikylin.myths.common.Constants;

/**
 * simple char buffer
 * @author mikylin
 */
public class ReadCharBuffer {

    private char[] buffer; //chars
    private int pos; //read index
    private int limit; //chars length

    public ReadCharBuffer(char[] chars){
        buffer = chars;
        pos = 0;
        limit = buffer.length;
    }

    public char move(){
        return read(pos ++);
    }

    public char read(int pos){
        if(pos > limit)
            return Constants.Blank.NULL_CHAR;
        return buffer[pos];
    }

    public void moveBack(){
        pos --;
    }

    /**
     * read til any char
     */
    public char[] moveTil(char... tils){

        int oldPos = pos;
        boolean isTil = true;
        while(isTil){
            char c = move();

            if(c == Constants.Blank.NULL_CHAR)
                return null;

            for(char til : tils){
                if(c == til){
                    isTil = false;
                    break;
                }
            }
        }

        if(pos == oldPos + 1)
            return null;

        char[] cs = new char[pos - 1 - oldPos];
        int index = 0;
        for(int i = oldPos ; i < pos - 1 ; i ++){
            cs[index] = buffer[i];
            index ++;
        }

        return cs;
    }
}
