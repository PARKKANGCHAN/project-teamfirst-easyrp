package co.second.easyrp.invoice.service;

import java.util.List;
import java.util.Map;

public interface InvoiceService {
	//2024.05.16 14:54 손지은 추가
	List<InvoiceVO> selectInvoiceAll(SearchVO searchVo); //청구 전체 불러오기
	int countInvoiceTables(SearchVO searchVo); //청구 페이지 구하기
	InvoiceVO selectInvoice(InvoiceVO invoiceVo); //청구 불러오기
	int insertInvoice(InvoiceVO invoiceVo); //청구 등록
	int updateInvoice(InvoiceVO invoiceVo); //청구 수정
	int deleteInvoice(InvoiceVO invoiceVo); //청구 삭제
	int mrpClosingUpdateToY(InvoiceVO invoiceVo); //청구등록을 하면 mrp의 마감여부를 Y로 변경시킨다.
	int mrpClosingUpdateToN(InvoiceVO invoiceVo); //청구삭제를 하면 mrp의 마감여부를 N으로 변경시킨다.
	
	//2024.05.17 13:32 손지은 추가
	int selectMaxCod();
	
	//청구테이블에서 입고여부와 생산가능 여부를 update하는 메소드
	//하서현
	int updateIboundynOrProdReady();

	//마감된 청구상세수량 + 입고된 청구상세수량을 기반으로 완벽하게 입고된 청구건만 가져오는 쿼리 
	//하서현
	List<Map<String, Object>> allIboundedInvoiceList();
}
