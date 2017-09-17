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
		// ����SolrQuery����
		SolrQuery query = new SolrQuery();
		
		//����ؼ��ֲ�ѯ
		if (StringUtils.isNotEmpty(queryString)){
			query.setQuery(queryString);
		} else {
			query.setQuery("*:*");
		}
		
		//��������1:Ŀ¼
		if (StringUtils.isNotEmpty(catalogName)){
			query.addFilterQuery("product_catalog_name:"+catalogName);
		}
		
		//��������2���۸�
		if (StringUtils.isNotEmpty(price)){
			String[] prices = price.split("-");
			if (prices.length == 2){
				query.addFilterQuery("product_price:[" + prices[0] + " TO " + prices[1]+ "]");

			}
		}
		
		//�ж�����
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
		
		//����Ĭ����
		query.set("df","product_keywords");
		// ���ø�����Ϣ
		query.setHighlight(true);
		query.addHighlightField("product_name");
		query.setHighlightSimplePre("<font style=\"color:red\" >");
		query.setHighlightSimplePost("</font>");
		
		//��ʼ��ѯ
		QueryResponse response = HttpSolrServer.query(query);
		//�õ���ѯ���
		SolrDocumentList documentList = response.getResults();
		
		//��¼����
		long total = documentList.getNumFound();
		List<Products> products = new ArrayList<>();
		Products prod;

		// ��ȡ������Ϣ
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument doc : documentList) {
			prod = new Products();

			// ��ƷID
			prod.setPid(doc.get("id").toString());

			List<String> list = highlighting.get(doc.get("id")).get("product_name");
			// ��Ʒ����
			if (list != null)
				prod.setName(list.get(0));
			else {
				prod.setName(doc.get("product_name").toString());
			}

			// ��Ʒ�۸�
			prod.setPrice(Float.parseFloat(doc.get("product_price").toString()));
			// ��ƷͼƬ��ַ
			prod.setPicture(doc.get("product_picture").toString());

			products.add(prod);
		}

		// ��װResultModel����
		ResultModel rm = new ResultModel();
		rm.setProductList(products);
		rm.setCurrentPage(page);
		rm.setTotal(total);

		int pageCount = (int) (total / 20);

		if (total % 20 > 0)
			pageCount++;
		// ������ҳ��
		rm.setTotalPage(pageCount);

		return rm;
	}

		
}
