package cn.mikylin.myths.interpreter.math;

public class Math {

    public static double num(double a, Object opr, double b) {

        if (opr.equals("==")) {
            if (a == b) {
                return 1;
            } else {
                return 0;
            }
        } else if (opr.equals("!=")) {
            if (a == b) {
                return 0;
            } else {
                return 1;
            }
        } else if (opr.equals(">=")) {
            if (a >= b) {
                return 1;
            } else {
                return 0;
            }
        } else if (opr.equals("<=")) {
            if (a <= b) {
                return 1;
            } else {
                return 0;
            }
        } else if (opr.equals("&&")) {
            if (a != 0 && b != 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (opr.equals("||")) {
            if (a != 0 || b != 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (opr.equals('>')) {
            if (a > b) {
                return 1;
            } else {
                return 0;
            }
        } else if (opr.equals('<')) {
            if (a < b) {
                return 1;
            } else {
                return 0;
            }
        } else if (opr.equals('+')) {
            return a + b;
        } else if (opr.equals('-')) {
            return a - b;
        } else if (opr.equals('*')) {
            return a * b;
        } else if (opr.equals('/')) {
            return a / b;
        }  else if (opr.equals('%')) {
            return a % b;
        } else if (opr.equals('=')) {
            return a;
        } else {
            throw new RuntimeException("非法运算符---( " + opr + " )");
        }
    }
}
