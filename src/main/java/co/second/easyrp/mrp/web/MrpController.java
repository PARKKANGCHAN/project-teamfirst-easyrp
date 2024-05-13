package co.second.easyrp.mrp.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import co.second.easyrp.inventorymgmt.service.InventoryMgmtService;
import co.second.easyrp.inventorymgmt.service.InventoryMgmtVO;
import co.second.easyrp.mps.service.MpsService;
import co.second.easyrp.mps.service.MpsVO;
import co.second.easyrp.mrp.service.MrpService;
import co.second.easyrp.mrp.service.MrpVO;

@Controller
public class MrpController {
	@Autowired MrpService mrpService;
	@Autowired MpsService mpsService;
	@Autowired InventoryMgmtService inventoryMgmtService;
	
	//소요량전개 관리 페이지
	@GetMapping("/mrpmanagement")
	public String mrpManagement(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "수주") String searchPlan,
            @RequestParam(required = false) String searchProdCod,
            @RequestParam(required = false) String searchProdName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date preSearchDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date postSearchDate,
            Model model) {
		//List<MrpVO> mrpTable = mrpService.mrpSelectListAll(page, size, searchPlan, searchProdCod, searchProdName, preSearchDate, postSearchDate);
        //int totalRecords = mrpService.countMrpTables(searchPlan, searchProdCod, searchProdName, preSearchDate, postSearchDate);
        //int totalPages = (int) Math.ceil((double) totalRecords / size);

        int pageGroupSize = 10;
        int currentPageGroup = (page - 1) / pageGroupSize;
        int startPage = currentPageGroup * pageGroupSize + 1;
        //int endPage = Math.min(totalPages, (currentPageGroup + 1) * pageGroupSize);

        model.addAttribute("searchPlan", searchPlan);
        model.addAttribute("searchProdCod", searchProdCod);
        model.addAttribute("searchProdName", searchProdName);
        model.addAttribute("preSearchDate", preSearchDate);
        model.addAttribute("postSearchDate", postSearchDate);
        //model.addAttribute("mrpTable", mrpTable);
        model.addAttribute("currentPage", page);
        //model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        //model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        
		return "easyrp/mrp/mrpmanagement";
	}
	
	@RequestMapping("mrpinsert")
	public String mrpInsert() {
		return "easyrp/mrp/mrpinsert";
	}
	
	@RequestMapping("mrpinsertfn")
	public String mrpInsertFn(List<MrpVO> mrpVos) {
		int maxNumber = mrpService.selectMaxCod() + 1;
		mrpService.mrpInsert(mrpVos);
		
		return "redirect:/mrpmanagement";
	}
	
    @GetMapping("/api/get-mpsval")
    @ResponseBody
    public List<MpsVO> getMpsValues() {
        return mpsService.mpsSelectListAllModal();
    }
    
    @GetMapping("/api/get-mrpdeployment")
    @ResponseBody
    public List<MrpVO> getMrpDeployment(String productCod) {
    	List<MrpVO> bomList = mrpService.selectBom(productCod);
    	List<MrpVO> mrpVoList = new ArrayList<MrpVO>();
    	InventoryMgmtVO inventoryMgmtVo = new InventoryMgmtVO();
    	
    	for(int i=0; i<bomList.size(); i++) {
    		MrpVO mrpVo = new MrpVO();
    		int maxNumber = mrpService.selectMaxCod() + 2 + i;
    		String newCode = String.format("%03d", maxNumber);
    		inventoryMgmtVo = inventoryMgmtService.getData(bomList.get(i).getInvCod());
    		
    		mrpVo.setCod("mrp" + newCode);
    		mrpVo.setInvCod(bomList.get(i).getInvCod());
    		mrpVo.setProdname(inventoryMgmtVo.getName());
    		mrpVo.setSpec(inventoryMgmtVo.getSpec());
    		mrpVo.setUnitName(inventoryMgmtVo.getUnitName());
    		mrpVo.setAccount(inventoryMgmtVo.getAccount());
    		mrpVo.setInvQty(bomList.get(i).getInvQty());
    		
    		mrpVoList.add(mrpVo);
    	}
    	
    	MrpVO mrpVo = new MrpVO();
    	int maxNumber = mrpService.selectMaxCod() + 1;
		String newCode = String.format("%03d", maxNumber);
    	mrpVo.setCod("mrp" + newCode);
    	mrpVoList.add(0, mrpVo);
    	
    	System.out.println("mrplist: " + mrpVoList);
        return mrpVoList;
    }
}
