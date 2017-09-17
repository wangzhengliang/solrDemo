package com.study.service;

import com.study.po.ResultModel;

public interface ProductService {
	
	ResultModel getProducts(String queryString,String catalogName,String price,String sort,Integer page) throws Exception;
}
