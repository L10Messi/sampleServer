package zgbzhyjy.sampsystem.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zgbzhyjy.sampsystem.dao.ProductDao;
import zgbzhyjy.sampsystem.dao.QualChrtDao;
import zgbzhyjy.sampsystem.entity.Product;
import zgbzhyjy.sampsystem.util.PageFinder;
import zgbzhyjy.sampsystem.util.StringUtil;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private QualChrtDao qualChrtDao;
	
	
	/**
	 * 产品保存和更新
	 * @param product
	 */
	public void saveProduct(Product product,String userid){
		if (StringUtil.isNotBlank(product.getPid())) {
			product.setUpdatedDate(new Date());
			product.setUpdatedBy(userid);
			this.productDao.update(product);
		}else{
			product.setCreatedDate(new Date());
			product.setCreatedBy(userid);
			this.productDao.save(product);
		}
	}
	
	/**
	 * 产品列表查询
	 * @param page
	 * @param rows
	 * @param product
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageFinder<Product> queryProductByPage(Product product,int page,int rows){
		String hql = "from Product p where p.isDel = 0 ";
		if(StringUtils.isNotBlank(product.getProductID())){
			hql += " and p.productID like '%"+product.getProductID()+"%'";
		}
		if(StringUtils.isNotBlank(product.getProductName())){
			hql += " and p.productName like '%"+product.getProductName()+"%'";
		}
		hql += "order by p.createdDate";
		return this.productDao.pagedByHQL(hql, page, rows);
	}
	
	/**
	 * 产品删除
	 * @param ids
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public void deleteProduct(String ids) throws Exception{
		String productIds = this.productDao.getParamFormat(ids);
		
		//首先要校验该产品是否还有 质量特性 
		String sql = "SELECT 1 FROM QualChrtInfo WHERE pid IN (" + productIds + ") and isDel=0";
		List listcount = this.qualChrtDao.getListBySQL(sql);
		System.out.println(sql);
		if (listcount != null && listcount.size() > 0) {
			throw new Exception("该产品还存在质量特性，如想删除此产品，先删掉所有质量特性");
		}
		
		//删除指定产品（注；删除只是让 isDel属性值变为1，所以相当于是进行更新）
		String[] pids=ids.split(",");
		for (String id : pids) {
			String dehql = "update Product p set p.isDel = 1";
			dehql += " where  p.pid = '"+id+"'";
			this.productDao.updateIsDel(dehql);
		}
	}
	
	/**
	 * 根据 id进行查询产品
	 * @param id
	 * @return product
	 */
	public Product queryProduct(String pid){
		return this.productDao.get(pid, Product.class);
	}
	
	
}
