package com.example.myprj.web.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.myprj.web.APIMemberController;
import com.example.myprj.web.BoardController;
import com.example.myprj.web.api.JsonResult;

//모든 컨트롤러의 예외처리에 대응한다.
@RestControllerAdvice(assignableTypes = {APIMemberController.class, BoardController.class}) // @ControllerAdvice + @ResponseBody
public class ApiGlobalExceptionHandler {

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public JsonResult<String> emptyExHeldler(EmptyResultDataAccessException ex){
		return new JsonResult<String>("01","nok","일치하는 정보가 없습니다.");
	}
	
	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public JsonResult<String> inValidHandler(InvalidDataAccessApiUsageException ex){
		return new JsonResult<String>("01","nok","일치하는 정보가 없습니다.");
	}
}