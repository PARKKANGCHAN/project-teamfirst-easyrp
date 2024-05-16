package co.second.easyrp.collectedmoney.service;

import java.util.List;

public interface CollectedMoneyService {
	
	List<CollectedMoneyVO> getHyunwooOrderData();
	
	// 데이터베이스에서 삭제되지 않은 모든 값을 가져오기 위한 인터페이스
	List<CollectedMoneyVO> tableAllList(SearchVO searchVO);
	
	// 데이터베이스에 데이터를 삽입하기 위한 기능 인터페이스
	int insertFn(CollectedMoneyVO collectedMoneyVO);
	
	// 데이터베이스에 데이터를 수정하기 위한 기능 인터페이스
	int updateFn(CollectedMoneyVO collectedMoneyVO);
	
	// 데이터베이스에 데이터를 삭제하기 위한 기능 인터페이스
	int deleteFn(String orderCod);
	
	// 검색 후 페이지네이션을 위한 인터페이스
	int countTable(SearchVO searchVO);
}
