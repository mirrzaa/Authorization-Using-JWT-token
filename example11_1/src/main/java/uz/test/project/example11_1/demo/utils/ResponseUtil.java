package uz.test.project.example11_1.demo.utils;

import uz.test.project.example11_1.demo.entities.Status;

public class ResponseUtil {
    public static Status getDefaultMessage() {
        return new Status("500", "Internal System Error");
    }

    public static Status getSuccessMessage() {
        return new Status("200", "Success");
    }

    public static Status getUserNotFoundMessage() {
        return new Status("400", "User not found");
    }
}
