package com.example.myprj.domain.common.paging;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PageCriteriaTest {

	@Test
	void calPage() {
		RecordCriteria rc = new RecordCriteria(5);
		rc.setReqPage(5);
		PageCriteria pc = new PageCriteria(rc, 5);
		pc.setTotalRec(324);
		
		log.info("시작레코드:{}, 종료레코드:{}",rc.getStartRec(),rc.getEndRec() );
		log.info("시작페이지:{}, 종료페이지:{}",
				pc.getStartPage(),pc.getEndPage() );
		log.info("최종레코드:{}",pc.getFinalEndPage() );
		
		
		
	}
}
