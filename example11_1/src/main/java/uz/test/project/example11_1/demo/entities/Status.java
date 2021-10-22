package uz.test.project.example11_1.demo.entities;

public class Status {
    private String code;
    private Object message;

    public Status(String code, Object message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Status{" +
                "code='" + code + '\'' +
                ", message=" + message +
                '}';
    }
}
