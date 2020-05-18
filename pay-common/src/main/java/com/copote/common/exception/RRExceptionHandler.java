

package com.copote.common.exception;
import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.NoHandlerFoundException;
//
//import javax.validation.ValidationException;

/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public R handleRRException(RRException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());
		return r;
	}

//	/**
//	 * 参数校验异常
//	 * @param e
//	 * @return
//	 */
//	@ExceptionHandler(ValidationException.class)
//	public R handleRRException(ValidationException e){
//		String[] str = e.getMessage().split("'");
//		log.info("参数错误，"+str[1]);
//		return R.error("参数错误，"+str[1]);
//	}
//
//	@ExceptionHandler(NoHandlerFoundException.class)
//	public R handlerNoFoundException(Exception e) {
//		logger.error(e.getMessage(), e);
//		return R.error(404, "路径不存在，请检查路径是否正确");
//	}
//
//	@ExceptionHandler(DuplicateKeyException.class)
//	public R handleDuplicateKeyException(DuplicateKeyException e){
//		logger.error(e.getMessage(), e);
//		return R.error("数据库中已存在该记录");
//	}
//
//	@ExceptionHandler(AuthorizationException.class)
//	public R handleAuthorizationException(AuthorizationException e){
//		logger.error(e.getMessage(), e);
//		return R.error("没有权限，请联系管理员授权");
//	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error();
	}
}
