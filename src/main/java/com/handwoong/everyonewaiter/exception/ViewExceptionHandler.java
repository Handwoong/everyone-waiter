package com.handwoong.everyonewaiter.exception;

import com.handwoong.everyonewaiter.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice("com.handwoong.everyonewaiter.controller")
public class ViewExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public RedirectView handleCustomException(CustomException err, HttpServletRequest req,
            RedirectAttributes redirectAttr) {
        ErrorCode errorCode = err.getErrorCode();
        log.error("[{}] {} {}", errorCode.name(), errorCode.getStatus(), errorCode.getMessage());

        String viewName = getRedirectUri(req, errorCode);
        redirectAttr.addFlashAttribute("errorCode", errorCode);

        return new RedirectView(viewName);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public RedirectView handleMethodArgsNotValidException(MethodArgumentNotValidException err,
            HttpServletRequest req, RedirectAttributes redirectAttr) {
        ErrorCode errorCode = ErrorCode.METHOD_ARGUMENT_NOT_VALID;
        log.error("[{}] {} {}", errorCode.name(), errorCode.getStatus(), errorCode.getMessage());

        Map<String, String[]> formData = req.getParameterMap();
        List<FieldError> fieldErrors = err.getFieldErrors();

        HashMap<String, String> fieldErrorMap = getFieldErrorMap(fieldErrors);
        String viewName = getRedirectUri(req, errorCode);

        redirectAttr.addFlashAttribute("formData", formData);
        redirectAttr.addFlashAttribute("errors", fieldErrorMap);
        redirectAttr.addFlashAttribute("errorCode", errorCode);

        return new RedirectView(viewName);
    }

    @ExceptionHandler({Exception.class})
    public ModelAndView handleException(Exception err) {
        ErrorCode errorCode = ErrorCode.SERVER_ERROR;
        log.error("[{}] {} {} {}",
                errorCode.name(), errorCode.getStatus(), errorCode.getMessage(), err.getMessage());
        return getModelAndView(errorCode);
    }

    private ModelAndView getModelAndView(ErrorCode errorCode) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorCode", errorCode);

        String viewName = getViewName(errorCode);
        modelAndView.setViewName(viewName);

        return modelAndView;
    }

    private String getViewName(ErrorCode errorCode) {
        StringBuilder sb = new StringBuilder();

        switch (errorCode.getStatus()) {
            case BAD_REQUEST -> sb.append("error/400");
            case NOT_FOUND -> sb.append("error/404");
            default -> sb.append("error/5xx");
        }

        return sb.toString();
    }

    private String getRedirectUri(HttpServletRequest req, ErrorCode errorCode) {
        return errorCode.getStatus() == HttpStatus.NOT_FOUND ? "/error" : req.getRequestURI();
    }

    private HashMap<String, String> getFieldErrorMap(List<FieldError> fieldErrors) {
        HashMap<String, String> fieldErrorMap = new HashMap<>();

        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            String fieldMessage = fieldError.getDefaultMessage();

            log.error("에러 필드 명 : '{}', 에러 메시지 : '{}'", fieldName, fieldMessage);
            fieldErrorMap.put(fieldName, fieldMessage);
        }
        return fieldErrorMap;
    }
}
