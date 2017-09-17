package com.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.po.ResultModel;
import com.study.service.ProductService;

@Controller
public class ProductController {
	
	private ProductService productService;
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	
	@RequestMapping("/list")
	public String list(String queryString,String catalogName,String price,String sort,Integer page,Model model) throws Exception{
		
		ResultModel rm = productService.getProducts(queryString, catalogName, price, sort, page);
		
		//将查询结果放到request域中
		model.addAttribute("result", rm);
		
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalogName", catalogName);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);
		model.addAttribute("page", page);
		
		return "product_list";
	}

}
