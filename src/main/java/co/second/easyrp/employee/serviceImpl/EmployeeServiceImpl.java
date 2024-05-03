package co.second.easyrp.employee.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import co.second.easyrp.employee.map.EmployeeMapper;
import co.second.easyrp.employee.service.EmployeeService;
import co.second.easyrp.employee.service.EmployeeVO;


//사원 관리를 구현하기 위한 Employee 서비스 구현체 클래스
//(2024년 4월 29일 오전 11시 23분 박현우)
@Service
@Primary
public class EmployeeServiceImpl implements EmployeeService {
	
	// employeeMapper 인터페이스 연결
	@Autowired
	private EmployeeMapper employeeMapper;

	// employee 정보(목록)을 구현하기 위한 메서드
	// (2024년 4월 29일 오후 11시 21분 박현우)
	@Override
	public List<EmployeeVO> employeeAllList() {
		return employeeMapper.employeeAllList();
	}

	// 사원을 등록하기위한 메서드
	@Override
	public int employeeRegis(EmployeeVO vo) {
		// TODO Auto-generated method stub
		return employeeMapper.employeeRegis(vo);
	}

	@Override
	public List<Map<String, Object>> empListJoinedDept() {
		// TODO Auto-generated method stub
		return employeeMapper.empListJoinedDept();
	}

	// 로그인 기능 구현을 위한 구현 메서드
	// (2024년 4월 29일 오후 1시 5분 박현우)
	@Override
	public EmployeeVO loginResult(EmployeeVO vo) {
		return employeeMapper.loginResult(vo);
	}

}
