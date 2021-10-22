package uz.test.project.example11_1.demo.entities.response;

import uz.test.project.example11_1.demo.entities.Status;

public class Response {
    Object data;
    Status status;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Response{" +
                "data=" + data +
                ", status=" + status +
                '}';
    }
}
