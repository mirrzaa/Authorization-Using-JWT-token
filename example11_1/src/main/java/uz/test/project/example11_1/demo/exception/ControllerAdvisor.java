package uz.test.project.example11_1.demo.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.test.project.example11_1.demo.entities.response.Response;
import uz.test.project.example11_1.demo.utils.ResponseUtil;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    public ControllerAdvisor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Response> handleUserNotFoundExceptin(UserNotFoundException ex, WebRequest request) {
        Response response = new Response();
        response.setData(objectMapper.createObjectNode());
        response.setStatus(ResponseUtil.getUserNotFoundMessage());
        request.getDescription(false);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
