package zgbzhyjy.sampsystem.util;

/**
 * easyui的datagrid向后台传递参数使用的model
 * 
 */
public class DataGrid {
	private int page = 1;// 当前页
	private int rows = 20;// 每页显示记录数
	
	private int pages ; //总页数
	
	private String sort;// 排序字段名
	private String order;// 按什么排序(asc,desc)

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}
}
