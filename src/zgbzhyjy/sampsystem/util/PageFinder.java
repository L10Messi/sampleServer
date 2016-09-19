package zgbzhyjy.sampsystem.util;



import java.io.Serializable;
import java.util.List;

/**
 * 分页对象. 包含当前页数据及分页信息如总记录数.
 * 
 * 
 */
@SuppressWarnings("serial")
public class PageFinder<T> implements Serializable {

	private static int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 每页的记录数
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 当前页中存放的记录,类型一般为List
	 */
	private List<T> rows;

	/**
	 * 总记录数
	 */
	private int total;

	/**
	 * 页数
	 */
	private int pageCount;

	/**
	 * 跳转页数
	 */
	private int pageNo;

	/**
	 * 是否有上一页
	 */
	private boolean hasPrevious = false;

	/**
	 * 是否有下一页
	 */
	private boolean hasNext = false;
	
	private List<T> footer;

	public PageFinder(int pageNo, int total) {
		this.pageNo = pageNo;
		this.total = total;
		this.pageCount = getTotalPageCount();
		refresh();
	}

	/**
	 * 构造方法，
	 */
	public PageFinder(int pageNo, int pageSize, int total) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = total;
		this.pageCount = getTotalPageCount();
		refresh();
	}

	/**
	 * 构造方法，构造所有
	 */
	public PageFinder(int pageNo, int pageSize, int total, List<T> rows) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = total;
		this.pageCount = getTotalPageCount();
		this.rows = rows;
		refresh();
	}

	/**
	 * 取总页数.
	 */
	private final int getTotalPageCount() {
		if (total % pageSize == 0){
			return total / pageSize;
		}
		else{
			return total / pageSize + 1;
		}
	}

	/**
	 * 刷新当前分页对象数据
	 */
	private void refresh() {
		if (pageCount <= 1) {
			hasPrevious = false;
			hasNext = false;
		} else if (pageNo == 1) {
			hasPrevious = false;
			hasNext = true;
		} else if (pageNo == pageCount) {
			hasPrevious = true;
			hasNext = false;
		} else {
			hasPrevious = true;
			hasNext = true;
		}
	}

	/**
	 * 取每页数据容量.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 取当前页中的记录.
	 */
	public Object getResult() {
		return rows;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}

	public void setHasPrevious(boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取跳转页第一条数据在数据集的位置.
	 */
	public int getStartOfPage() {
		return (pageNo - 1) * pageSize;
	}

	public List<T> getFooter() {
		return footer;
	}

	public void setFooter(List<T> footer) {
		this.footer = footer;
	}
	
	
}