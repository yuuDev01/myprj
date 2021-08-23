package com.example.myprj.domain.common.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.myprj.web.form.Code;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@AllArgsConstructor
public class CodeDAOImpl implements CodeDAO {
	private final JdbcTemplate jt;
	
	@Override
	public List<Code> getCode(String pcode) {

		StringBuffer sql = new StringBuffer();
		sql.append("select code,decode from code where pcode = ?");
		List<Code> codes = 
				jt.query(sql.toString(), new BeanPropertyRowMapper<>(Code.class), pcode);
		return codes;
	}
}
