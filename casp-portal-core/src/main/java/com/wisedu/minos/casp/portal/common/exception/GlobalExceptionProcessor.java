package com.wisedu.minos.casp.portal.common.exception;

import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.util.MinosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

/**
 * 功能描述： 全局异常处理器
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title GlobalExceptionProcessor
 * @Author: jcx
 * @Date: 2020/7/20
 */
@RestControllerAdvice
public class GlobalExceptionProcessor {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionProcessor.class);

    @Autowired
    HttpServletRequest request;
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResultData handleRRException(BusinessException be) {
        logger.error("自定义异常："+be.getErrorDetail());
        return new ResultData(be.getErrorCode(), be.getErrorDetail());
    }

    @ExceptionHandler(MinosException.class)
    public ResultData handleMinoException(MinosException be) {
        logger.error("Minos自定义异常："+be.getMessage());
        return new ResultData("999", be.getMessage());
    }

    @ExceptionHandler(NoLoginException.class)
    public ResultData handleNoLoginException(NoLoginException be) {
        logger.error("NoLogin自定义异常："+be.getMessage());
        return new ResultData("999", be.getMessage());
    }

    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        String errorMsg = GlobalEnum.PARAM_FAIL.getMsg();
        if (e.getBindingResult().getFieldError() !=null){
            errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();
        }
        return new ResultData(GlobalEnum.PARAM_FAIL.getCode(), errorMsg);
    }

    /**
     * @Author jcx
     * @Description 参数校验不通过异常
     * @Date 9:45 2020/7/27
     * @Param e:
     * @return ResultData
     **/
    @ExceptionHandler(ValidationException.class)
    public ResultData handleValidationException(ValidationException e) {
        logger.error(e.getMessage(), e);
        return new ResultData(GlobalEnum.VALIDATION.getCode(), e.getCause().getMessage());
    }

    /**
     * @Author jcx
     * @Description 数据重复异常
     * @Date 9:45 2020/7/27
     * @Param e:
     * @return ResultData
     **/
    @ExceptionHandler(DuplicateKeyException.class)
    public ResultData handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return new ResultData(GlobalEnum.DUPLICATE_KEY.getCode(), GlobalEnum.DUPLICATE_KEY.getMsg());
    }

    /**
     * @Author jcx
     * @Description 请求参数与定义的参数的传递方式不一致异常
     * @Date 9:45 2020/7/27
     * @Param e:
     * @return ResultData
     **/
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultData httpMessageNotReadableException (HttpMessageNotReadableException e){
        logger.error(e.getMessage(), e);
        return new ResultData(GlobalEnum.NO_TREAD_ABLE.getCode(), GlobalEnum.NO_TREAD_ABLE.getMsg());

    }

    /**
     * @Author jcx
     * @Description 非法参数校验
     * @Date 9:43 2020/7/27
     * @Param e:
     * @return ResultData
     **/
    @ExceptionHandler(IllegalArgumentException.class)
    public ResultData illegalArgumentException (IllegalArgumentException e){
        logger.error(e.getMessage(), e);
        return new ResultData(GlobalEnum.PARAM_FAIL.getCode(), GlobalEnum.PARAM_FAIL.getMsg());
    }

    /**
     * 处理页面加载异常
     */
    @ExceptionHandler(PageException.class)
    public ModelAndView handlePageException(PageException e) {
        logger.error("页面加载异常：",e);
        return getModelAndView("500",e.getMessage(),request.getRequestURL().toString());
    }

    /**
     * 处理页面不存在异常
     */
    @ExceptionHandler(PageNotFoundException.class)
    public ModelAndView handlePageNotFoundException(PageNotFoundException e) {
        logger.error("页面不存在：",e);
        return getModelAndView("404",e.getMessage(),request.getRequestURL().toString());
    }

    public static ModelAndView getModelAndView(String errorCode,String msg,String url){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorCode", errorCode);
        modelAndView.addObject("message", msg);
        modelAndView.addObject("url", url);
        modelAndView.setViewName("error/error");
        return modelAndView;
    }

    /**
     * @Author jcx
     * @Description 总的异常捕获
     * @Date 9:43 2020/7/27
     * @Param e:
     * @return ResultData
     **/
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return getModelAndView("500","服务器出错了。。。",request.getRequestURL().toString());
    }


}
