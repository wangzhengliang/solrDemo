# solrDemo

<h3>利用solr完成商品的搜索</h3>

&nbsp;&nbsp;&nbsp;&nbsp;Solr是apache的顶级开源项目，它是使用java开发 ，基于lucene的全文检索服务器。要利用Solr实现对商品的搜索，需要搭建Solr服务器，不知道怎么搭建的，可以去Wiki查看我搭建Solr过程的笔记。<br />
&nbsp;&nbsp;&nbsp;&nbsp;配置好Solr服务器后，启动服务器，然后可以用：“localhost:8080/solr”，访问到我们搭建好的solr服务器。我们可以用solrj调用solr服务。先模拟一个索引的创建过程：<br>
`HttpSolrServer server = new HttpSolrServer("http"//localhost:8080/solr");`<br>
`//创建Document对象`<br>
`SolrInputDocument doc = new SolrInputDocument();`<br>
`doc.addField("id","c001");`<br>
`doc.addField("name",solr test");`<br>
`//将Document对象添加到索引库中`<br>
`server.add(doc);`<br>
`//提交`<br>
`server.commit();`<br>
&nbsp;&nbsp;&nbsp;&nbsp;删除、修改索引等操作差不多，需要的时候再去翻API。<br>
&nbsp;&nbsp;&nbsp;&nbsp;现在数据库中有3000多条商品记录，我们把这些记录按照一定的规则在Solr中创建了索引。现在我们需要按条件快速的查出数据并在页面显示出来。
<center><div style="color: red">这部分的核心代码在solrDemo库中的com.study.service.impl.ProductServiceImpl中</div></center>
