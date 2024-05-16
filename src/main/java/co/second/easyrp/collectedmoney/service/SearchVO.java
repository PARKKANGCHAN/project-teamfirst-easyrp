package co.second.easyrp.collectedmoney.service;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//(2024년 5월 16일 오후 1시 38분 박현우)
@Getter
@Setter
@ToString
public class SearchVO {
	private String searchOrderCod;
	private int searchCollectedMoneyState;
	private String searchCollectedMoneyEmployeeCod;
	private Date preSearchDate;
	private Date postSearchDate;
	private int pageSize = 10;
	private int offset = 0;
}
