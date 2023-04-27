package com.handwoong.everyonewaiter.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler({Exception.class})
    public ModelAndView handleException(Exception err) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
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
}
