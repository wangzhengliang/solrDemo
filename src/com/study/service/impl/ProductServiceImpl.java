package com.study.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;

import com.study.po.Products;
import com.study.po.ResultModel;
import com.study.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	private HttpSolrServer HttpSolrServer;
	public void setHttpSolrServer(HttpSolrServer httpSolrServer) {
		HttpSolrServer = httpSolrServer;
	}
	
	@Override
	public ResultModel getProducts(String queryString, String catalogName, String price, String sort, Integer page)
			throws Exception {
		// 创建SolrQuery对象
		SolrQuery query = new SolrQuery();
		
		//输入关键字查询
		if (StringUtils.isNotEmpty(queryString)){
			query.setQuery(queryString);
		} else {
			query.setQuery("*:*");
		}
		
		//过滤条件1:目录
		if (StringUtils.isNotEmpty(catalogName)){
			query.addFilterQuery("product_catalog_name:"+catalogName);
		}
		
		//过滤条件2：价格
		if (StringUtils.isNotEmpty(price)){
			String[] prices = price.split("-");
			if (prices.length == 2){
				query.addFilterQuery("product_price:[" + prices[0] + " TO " + prices[1]+ "]");

			}
		}
		
		//判断排序
		if ("1".equals(sort)){
			query.setSort("product_price",ORDER.desc);
		} else {
			query.setSort("product_price",ORDER.asc);
		}
		
		if (page == null){
			page = 1;
		}
		
		query.setStart((page-1)*20);
		query.setRows(20);
		
		//设置默认域
		query.set("df","product_keywords");
		// 设置高亮信息
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<font style=\"color:red\" >");
		query.setHighlightSimplePost("</font>");
		
		//开始查询
		QueryResponse response = HttpSolrServer.query(query);
		//得到查询结果
		SolrDocumentList documentList = response.getResults();
		
		//记录总数
		long total = documentList.getNumFound();
		List<Products> products = new ArrayList<>();
		Products prod;

		// 获取高亮信息
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument doc : documentList) {
			prod = new Products();

			// 商品ID
			prod.setPid(doc.get("id").toString());

			List<String> list = highlighting.get(doc.get("id")).get("product_name");
			// 商品名称
			if (list != null)
				prod.setName(list.get(0));
			else {
				prod.setName(doc.get("product_name").toString());
			}

			// 商品价格
			prod.setPrice(Float.parseFloat(doc.get("product_price").toString()));
			// 商品图片地址
			prod.setPicture(doc.get("product_picture").toString());

			products.add(prod);
		}

		// 封装ResultModel对象
		ResultModel rm = new ResultModel();
		rm.setProductList(products);
		rm.setCurrentPage(page);
		rm.setTotal(total);

		int pageCount = (int) (total / 20);

		if (total % 20 > 0)
			pageCount++;
		// 设置总页数
		rm.setTotalPage(pageCount);

		return rm;
	}

		
}
