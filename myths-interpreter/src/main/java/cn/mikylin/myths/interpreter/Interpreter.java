package cn.mikylin.myths.interpreter;

import cn.mikylin.myths.common.Blank;
import cn.mikylin.myths.interpreter.language.Token;

public class Interpreter {


    public static void main(String[] args) {
        String a = "1 + 2";

        char[] chars = a.toCharArray();
        for(char c : chars){
            if(c == Token.blank){
                continue;
            }

        }
    }
}
