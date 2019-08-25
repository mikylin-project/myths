package cn.mikylin.myths.exam.base;

public final class CheckResult {

    private Boolean ok;

    private String message;

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private CheckResult(){}

    public Boolean isOk() {
        return ok;
    }

    public String message() {
        return message;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static Builder getSuccessBuild() {
        return getBuilder().ok(true);
    }

    public static Builder getFailBuild() {
        return getBuilder().ok(false);
    }

    public static class Builder {

        private Boolean ok;
        private String message;

        public Builder ok(Boolean ok) {
            this.ok = ok;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public CheckResult build(){
            CheckResult result = new CheckResult();
            result.setOk(ok);
            result.setMessage(message);
            return result;
        }
    }

}
