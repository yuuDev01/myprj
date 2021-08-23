package com.example.myprj.domain.common.dao;

import java.util.List;

import com.example.myprj.web.form.Code;

public interface CodeDAO {
	List<Code> getCode(String Pcode);
}