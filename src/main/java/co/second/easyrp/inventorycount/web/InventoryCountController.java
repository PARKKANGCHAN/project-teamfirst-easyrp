package co.second.easyrp.inventorycount.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import co.second.easyrp.inventorycount.service.InventoryCountDetailVO;
import co.second.easyrp.inventorycount.service.InventoryCountService;
import co.second.easyrp.inventorycount.service.InventoryCountVO;
import co.second.easyrp.inventorycount.service.ProductInventoryVO;
import co.second.easyrp.inventorycount.service.SearchVO;
import co.second.easyrp.mrp.service.MrpVO;
import co.second.easyrp.warehouse.service.WareHouseService;
import co.second.easyrp.warehouse.service.WareHouseVO;

@Controller
public class InventoryCountController {

	@Autowired
	InventoryCountService inventorycountservice;
	
	@GetMapping("/inventorycount")
	public String inventoryCount(SearchVO searchVO, Model model, 
								 @RequestParam(defaultValue="1") int page,
								 @RequestParam(defaultValue="10") int pageSize,
								 @RequestParam(required = false) String searchCod,
								 @RequestParam(required = false) String searchLocation,
								 @RequestParam(required = false) String searchWarehouse,
								 @RequestParam(required = false) String searchProduct,
								 @RequestParam(required = false) String searchInventory,
								 @RequestParam(required = false) String searchCountClass,
								 @RequestParam(required = false) String searchEmployee,
								 @RequestParam(required = false) String searchAccount,
								 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date preSearchDate,
						         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date postSearchDate) {
		
		searchVO.setSearchCod(searchCod);
		searchVO.setSearchWarehouse(searchWarehouse);
		searchVO.setSearchProduct(searchProduct);
		searchVO.setSearchLocation(searchLocation);
		searchVO.setSearchInventory(searchInventory);
		searchVO.setSearchCountClass(searchCountClass);
		searchVO.setSearchEmployee(searchEmployee);
		searchVO.setSearchAccount(searchAccount);
		searchVO.setOffset((page-1)*pageSize);
		
		List<InventoryCountVO> inventoryCountList=inventorycountservice.inventoryCountList(searchVO);
		
		int totalRecords = inventorycountservice.countInventoryCountLists(searchVO);
		
		int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
		int pageGroupSize = 10;
		
		int currentPageGroup = (pageSize - 1) / pageGroupSize;
		int startPage = currentPageGroup * pageGroupSize + 1;
		int endPage = Math.min(totalPages, (currentPageGroup + 1) * pageGroupSize);
		
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("inventoryCountList", inventoryCountList);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("endPage", endPage);
		model.addAttribute("startPage", startPage);

		return "easyrp/inventory/inventorycount";
		
	}
	
	@RequestMapping("inventorycountinsert")
	public String inventoryCountInsert(Model model) {
		List<WareHouseVO> warehouseinv = warehouseList();
		model.addAttribute("warehouseinv", warehouseinv);
		return "easyrp/inventory/inventorycountinsert";
	}
	
	@RequestMapping("/api/get-prodinvinsert")
	public String inventoryCountInsertFn( HttpServletRequest httpservletrequest, HttpSession httpsession) {
		
//		InventoryCountVO inventorycountvo = new InventoryCountVO();	
		String[] prodInvCod;
		String[] countqty;
		String[] note;
		String warehouse;
		
		
		prodInvCod= httpservletrequest.getParameterValues("prodInvCod");
		countqty = httpservletrequest.getParameterValues("countqty");
		note = httpservletrequest.getParameterValues("note");
		warehouse= httpservletrequest.getParameter("warehouse");
		System.out.println(warehouse);
		
				System.out.println(Arrays.toString(countqty));
				System.out.println(Arrays.toString(note));
				System.out.println(Arrays.toString(prodInvCod));
		
		InventoryCountDetailVO inventorycountdetailvo = new InventoryCountDetailVO();
		InventoryCountVO inventorycountvo = new InventoryCountVO();		
		
		int maxNumber = inventorycountservice.selectMaxCod() + 1;
    	String newCode = String.format("%03d", maxNumber);
    	
    	SimpleDateFormat countdate = new SimpleDateFormat("dd-MM-yyyy");
    	
    	if(warehouse != null) {
    		inventorycountvo.setWarehouse(warehouse);
    	}
    	
    	inventorycountvo.setCod(newCode);
    	inventorycountvo.setEmployeeCod((String) httpsession.getAttribute("empCode"));
    	inventorycountvo.setCountDate(countdate);
    	inventorycountvo.setInvDate(countdate);
    	inventorycountvo.setWarehouseCod(inventorycountservice.wareHouseCod(warehouse));
    	inventorycountvo.setCountclass("정기");
    	
		
		if(prodInvCod != null) {
			for(int i=0; i<prodInvCod.length; i++) {
				
				boolean prodinvcods = prodInvCod[i].contains("prd");
				if(prodinvcods = true) {
					inventorycountdetailvo.setProductCod(prodInvCod[i]);
					inventorycountdetailvo.setAccount("완제품");
				}else {
					inventorycountdetailvo.setInventoryCod(prodInvCod[i]);
					inventorycountdetailvo.setAccount("원자재");
				}
				inventorycountdetailvo.setQty(Integer.parseInt(countqty[i]));	
				inventorycountdetailvo.setNote(note[i]);
				inventorycountdetailvo.setUnitCod(1);
				inventorycountdetailvo.setDiffQty(inventorycountdetailvo.getDiffQty());
				
		List<ProductInventoryVO> result = new ArrayList<>();
		List<ProductInventoryVO> prodinvs = inventorycountservice.getAllSelectedCountList(prodInvCod[i]);
		result.addAll(prodinvs);
		System.out.println(result.toString());
		
//		inventorycountservice.insertInventoryCount(inventorycountvo);
//		inventorycountservice.insertCountDetailList(inventorycountdetailvo);
		}
			
			}
			
//		inventorycountvo.setCod(cod);
//		inventorycountvo = inventorycountservice.selectInventoryCountList(inventorycountvo);
//		InventoryCountDetailVO inventorycountdetailvo = new InventoryCountDetailVO();
//		inventorycountdetailvo = inventorycountservice.selectedInventoryCountDetailList(inventorycountdetailvo);
//		
//    	int maxNumber = inventorycountservice.selectMaxCod() + 1;
//    	String newCode = String.format("%03d", maxNumber);
//    	
//    	SimpleDateFormat countdate = new SimpleDateFormat("dd-MM-yyyy");
//    	
//    	inventorycountvo.setCod(newCode);
//    	inventorycountvo.setCountDate(inventorycountvo.getCountDate());
//    	inventorycountvo.setEmployeeCod((String) httpsession.getAttribute("empCode"));
//    	inventorycountvo.setCountDate(countdate);
//    	inventorycountvo.setInvDate(countdate);
//    	inventorycountvo.setCountclass("정기");
//    	inventorycountvo.setWarehouse(warehouse);
//    	
//    	inventorycountdetailvo.setInventorycountCod(newCode);
//    	inventorycountdetailvo.setLocationCod(locationCod);
//    	inventorycountdetailvo.setAccount(account);
//    	inventorycountdetailvo.setNote(note);
//    	inventorycountdetailvo.setDiffQty(diffQty);
//    	inventorycountdetailvo.setProductCod(productCod);
//    	inventorycountdetailvo.setInventoryCod(inventoryCod);
//	
//		inventorycountservice.insertInventoryCount(inventorycountvo);
//		inventorycountservice.insertCountDetailList(inventorycountdetailvo);
	
		return "redirect:/inventorycount";
	}
	
	
	@GetMapping("/api/get-prodinv")
	@ResponseBody
	public List<ProductInventoryVO> getAllProdInv(HttpServletRequest httpservletrequest){
		
		String warehouse = httpservletrequest.getParameter("warehouse");
		
		
		WareHouseVO warehousevo = new WareHouseVO();
		
		
		warehousevo.setName(warehouse);
		
		
		System.out.println(inventorycountservice.selectedWarehouseList(warehousevo));
		

		return inventorycountservice.getAllProdInvWarehouse(warehousevo);
	}

	public List<WareHouseVO> warehouseList() {
	        return inventorycountservice.warehouseList();
	    }
	
	@GetMapping("/api/get-prodinvlist")
	@ResponseBody
	public List<ProductInventoryVO> getAccount(HttpServletRequest httpservletrequest){
		
		String prodinv = httpservletrequest.getParameter("prodinv");
		String warehouse = httpservletrequest.getParameter("warehouse");
		

		ProductInventoryVO prodinvvo= new ProductInventoryVO();
		
		prodinvvo.setAccount(prodinv);
		prodinvvo.setWarehouse(warehouse);
		System.out.println(prodinv);
	//System.out.println(inventorycountservice.getProdInvAccount(prodinvvo));
		
		return inventorycountservice.getProdInvAccount(prodinvvo);
	}
//	@GetMapping("/api/get-count")
//	@ResponseBody
//	public List<ProductInventoryVO> getCount(HttpServletRequest httpservletrequest){
//		
//		String[] itemList;
//		
//		itemList = httpservletrequest.getParameterValues("itemList");
//		List<ProductInventoryVO> result = new ArrayList<>();
//		
//
//		if(itemList != null) {
//			for(int i=0; i<itemList.length; i++) {
//		List<ProductInventoryVO> prodinvs = inventorycountservice.getAllSelectedCountList(itemList[i]);
//		result.addAll(prodinvs);
//			}
//			 System.out.println(result.toString());
//		}
//		return result;
//	}
	
}
