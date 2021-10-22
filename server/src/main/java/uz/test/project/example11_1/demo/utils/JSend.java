package uz.test.project.example11_1.demo.utils;

import com.fasterxml.jackson.annotation.*;

@JsonRootName("result")
public final class JSend<T> {
    public enum Status {
        SUCCESS("success"), FAIL("fail"), ERROR("error");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        public boolean isSuccess() {
            return equals(SUCCESS);
        }

        public boolean isFail() {
            return equals(FAIL);
        }

        public boolean isError() {
            return equals(ERROR);
        }
    }

    @JsonProperty
    private final Status status;

    @JsonProperty
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String code;

    @JsonProperty
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String message;

    @JsonProperty
    private T data;

    public JSend(Status status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }


    @JsonCreator // for work un-serialization
    public static <T> JSend<T> success() {
        return JSend.success(null, null);
    }

    public static <T> JSend<T> success(T data) {
        return JSend.success(null, data);
    }

    public static <T> JSend<T> success(String message, String code, T data) {
        return new JSend<>(Status.SUCCESS, code, message, data);
    }

    public static <T> JSend<T> success(String code, T data) {
        return new JSend<>(Status.SUCCESS, code, null, data);
    }

    public static <T> JSend<T> fail() {
        return JSend.fail(null, null);
    }

    public static <T> JSend<T> fail(String code) {
        return JSend.fail(code, null);
    }

    public static <T> JSend<T> fail(String code, T data) {
        return new JSend<>(Status.FAIL, code, null, data);
    }

    public static <T> JSend<T> error() {
        return JSend.error(null, null, null);
    }

    public static <T> JSend<T> error(String code) {
        return JSend.error(null, code, null);
    }

    public static <T> JSend<T> error(String message, String code) {
        return JSend.error(message, code, null);
    }

    public static <T> JSend<T> error(String message, String code, T data) {
        return new JSend<>(Status.ERROR, code, message, data);
    }

    @Override
    public String toString() {
        return "{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
