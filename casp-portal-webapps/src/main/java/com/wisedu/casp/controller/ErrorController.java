package com.wisedu.casp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    /**
     * errorPage
     * <p/>
     *
     * @param
     * @return ModelAndView
     * @throws
     * @date 2020/10/12 12:58
     */
    @RequestMapping("error/{errorCode}")
    public ModelAndView errorPage(@PathVariable("errorCode") String errorCode, String errorUrl, String errorMsg) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorCode", errorCode);
        modelAndView.addObject("message", errorMsg);
        modelAndView.addObject("url", errorUrl);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }
}
