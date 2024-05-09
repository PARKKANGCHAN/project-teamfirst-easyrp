package co.second.easyrp.mps.service.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import co.second.easyrp.commontable.service.CommonTableVO;
import co.second.easyrp.mps.service.MpsService;
import co.second.easyrp.mps.service.MpsVO;
import co.second.easyrp.orderdetail.service.OrderdetailService;
import co.second.easyrp.orderdetail.service.OrderdetailVO;

@Controller
public class MpsController {
	@Autowired MpsService mpsService;
	@Autowired OrderdetailService orderdetailService;
	
	//주계획 관리 페이지	
    @GetMapping("/mpsmanagement")
    public String mpsManagement(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "수주") String searchPlan,
                              @RequestParam(required = false) String searchProdCod,
                              @RequestParam(required = false) String searchProdName,
                              @RequestParam(required = false) String searchClient,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date preSearchDate,
                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date postSearchDate,
                              Model model) {
        List<MpsVO> mpsTable = mpsService.mpsSelectListAll(page, size, searchPlan, searchProdCod, searchProdName, searchClient, preSearchDate, postSearchDate);
        int totalRecords = mpsService.countMpsTables(searchPlan, searchProdCod, searchProdName, searchClient, preSearchDate, postSearchDate);
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        int pageGroupSize = 10;
        int currentPageGroup = (page - 1) / pageGroupSize;
        int startPage = currentPageGroup * pageGroupSize + 1;
        int endPage = Math.min(totalPages, (currentPageGroup + 1) * pageGroupSize);

        model.addAttribute("searchPlan", searchPlan);
        model.addAttribute("searchProdCod", searchProdCod);
        model.addAttribute("searchProdName", searchProdName);
        model.addAttribute("searchClient", searchClient);
        model.addAttribute("preSearchDate", preSearchDate);
        model.addAttribute("postSearchDate", postSearchDate);
        model.addAttribute("mpsTable", mpsTable);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        return "easyrp/mps/mpsmanagement";
    }
	
	//주문적용 페이지
	//사용자가 선택한 수주 정보 리스트를 보여주고, 수주에 대한 계획일을 설정하는 페이지
	@RequestMapping("mpsinsert")
	public String mpsOrderList(Model model) {
		return "easyrp/mps/mpsinsert";
	}

	//주계획을 등록하는 함수
	@RequestMapping("mpsinsertfn")
	public String mpsCompleted(Model model, MpsVO mpsVo) {
		int maxNumber = mpsService.selectMaxCod() + 1;
		String newCode = String.format("%03d", maxNumber);
		mpsVo.setCod("mps" + newCode);
		mpsService.mpsInsert(mpsVo);
		mpsService.orderdetailMpsStateUpdate(mpsVo);
		return "redirect:/mpsmanagement";
	}
	
	@GetMapping("/mpsupdate")
	public String mpsUpdate(Model model, @RequestParam("cod") String cod) {
		MpsVO mpsVo = new MpsVO();
		mpsVo.setCod(cod);
		model.addAttribute("mpsData", mpsService.mpsSelect(mpsVo));
		return "easyrp/mps/mpsupdate";
	}
	
	@PostMapping("/mpsupdatefn")
	public String mpsUpdateFn(MpsVO mpsVo) {
		System.out.println(mpsVo);
		mpsService.mpsUpdate(mpsVo);
		return "redirect:/mpsmanagement";
	}

	@GetMapping("/mpsdeletefn")
	public String mpsDeleteFn(MpsVO mpsVo, @RequestParam("cod") String cod) {
		mpsVo.setCod(cod);
		mpsService.mpsDelete(mpsVo);
		return "redirect:/mpsmanagement";
	}
	
    @GetMapping("/api/get-ov")
    @ResponseBody
    public List<OrderdetailVO> getOrderdetailValues() {
        return orderdetailService.orderdetailSelectListAll();
	}
}
