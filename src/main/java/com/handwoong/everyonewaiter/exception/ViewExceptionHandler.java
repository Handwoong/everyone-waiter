package com.handwoong.everyonewaiter.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice("com.handwoong.everyonewaiter.controller")
public class ViewExceptionHandler {

    @ExceptionHandler({CustomException.class})
    public Object handleCustomException(CustomException err, HttpServletRequest req,
            RedirectAttributes redirectAttr) {
        ErrorCode errorCode = err.getErrorCode();
        log.error("[{}] {} {}", errorCode.name(), errorCode.getStatus(), errorCode.getMessage());

        redirectAttr.addFlashAttribute("errorCode", errorCode);
        return new RedirectView(req.getRequestURI());
    }

    @ExceptionHandler({Exception.class})
    public Object handleException(Exception err) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("[{}] {} {} {}",
                errorCode.name(), errorCode.getStatus(), errorCode.getMessage(), err.getMessage());
        return getModelAndView(errorCode);
    }

    private ModelAndView getModelAndView(ErrorCode errorCode) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorCode", errorCode);

        switch (errorCode.getStatus()) {
            case BAD_REQUEST -> modelAndView.setViewName("error/400");
            case NOT_FOUND -> modelAndView.setViewName("error/404");
            default -> modelAndView.setViewName("error/5xx");
        }

        return modelAndView;
    }
}
