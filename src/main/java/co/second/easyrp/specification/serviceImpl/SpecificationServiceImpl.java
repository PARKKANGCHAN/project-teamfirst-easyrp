package co.second.easyrp.specification.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import co.second.easyrp.specification.map.SpecificationMapper;
import co.second.easyrp.specification.service.SpecificationService;
import co.second.easyrp.specification.service.SpecificationVO;

@Service
@Primary
public class SpecificationServiceImpl implements SpecificationService {
	
	@Autowired
	SpecificationMapper specificationMapper;

	@Override
	public List<SpecificationVO> getOrderDatas(SpecificationVO specificationVO) {
		return specificationMapper.getOrderDatas(specificationVO);
	}

}
