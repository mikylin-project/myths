package cn.mikylin.myths.exam.base;

public class ExamException extends RuntimeException {

    private CheckResult result;

    public ExamException(CheckResult result){
        this.result = result;
    }

    public CheckResult getResult(){
        return result;
    }
}
