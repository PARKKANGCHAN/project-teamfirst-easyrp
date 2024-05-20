package co.second.easyrp.bom.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import co.second.easyrp.bom.map.BomMapper;
import co.second.easyrp.bom.service.BomService;
import co.second.easyrp.bom.service.BomVO;


@Service
@Primary
public class BomServiceImpl implements BomService {
	
	@Autowired
	BomMapper bomMapper;

	@Override
	public BomVO getData(BomVO bomVo) {
		return bomMapper.getData(bomVo);
	}

	@Override
	public int insertFn(BomVO bomVO) {
		return bomMapper.insertFn(bomVO);
	}

	@Override
	public int updateFn(BomVO bomVO) {
		return bomMapper.updateFn(bomVO);
	}

	@Override
	public int deleteFn(String cod) {
		return bomMapper.deleteFn(cod);
	}

	@Override
	public int eachDeleteFn(BomVO bomVO) {
		return bomMapper.eachDeleteFn(bomVO);
	}

	@Override
	public List<BomVO> getHyunwooProductData() {
		return bomMapper.getHyunwooProductData();
	}
}