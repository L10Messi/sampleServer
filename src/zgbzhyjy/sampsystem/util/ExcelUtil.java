package zgbzhyjy.sampsystem.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ExcelUtil {
	private static final Logger logger = Logger.getLogger(ExcelUtil.class);
	
	/**
	 * 
	 * @Author 文超群
	 * @since 2016-7-21 
	 * @version 1.0
	 * @return 从数据库中导出Excel
	 * @param FileName,Title,List<T>
	 * @throws Exception 
	 * */
	public static void exportExcel(String FileName,String[] Title , List<?> listContent) throws Exception {  
		  // 以下开始输出到EXCEL  
		  try {      
			  FileName = FileName+"("+DateUtil.getMachingCurrentTime()+")";
			  String src="/Users/LRover/Downloads/"+FileName+".xls";
		   //定义输出流，以便打开保存对话框  
//		   OutputStream os = response.getOutputStream();// 取得输出流 
		   
//		   response.reset();// 清空输出流        
//		   response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("GB2312"),"ISO8859-1"));  
		// 设定输出文件头        
//		   response.setContentType("application/msexcel");// 定义输出类型      
		   //定义输出流，以便打开保存对话框_______________________end  
		  
			  File file = new File(src);
	          file.createNewFile();
	          OutputStream os = new FileOutputStream(file);
		   /** **********创建工作簿************ */  
		   WritableWorkbook workbook = Workbook.createWorkbook(os);  
		  
		   /** **********创建工作表************ */  
		  
		   WritableSheet sheet = workbook.createSheet("Sheet1", 0);  
		  
//		   /** **********设置纵横打印（默认为纵打）、打印纸***************** */  
//		   jxl.SheetSettings sheetset = sheet.getSettings();  
//		   sheetset.setProtected(false);  

//		   /** ************设置单元格字体************** */  
//		   WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);  
//		   WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);  
		  
		   /** ************以下设置三种单元格样式，灵活备用************ */  
//		   // 用于标题居中  
//		   WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);  
//		   wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条  
//		   wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
//		   wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐  
//		   wcf_center.setWrap(false); // 文字是否换行  
		     
//		   // 用于正文居左  
//		   WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);  
//		   wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条  
//		   wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐  
//		   wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐  
//		   wcf_left.setWrap(false); // 文字是否换行     
		   
		  
		   //** ***************以下是EXCEL开头大标题，暂时省略********************* */  
//		   sheet.mergeCells(0, 0, colWidth, 0);  
//		   sheet.addCell(new Label(0, 0, "XX报表", wcf_center));  
//		   /*** ***************以下是EXCEL第一行列标题********************* */  
		   for (int i = 0; i < Title.length; i++) {  
		    sheet.addCell(new Label(i+1, 0,Title[i]));  
		   }   
		   ///** ***************以下是EXCEL正文数据********************* */  
		   Field[] fields=null;  
		   int i=1;  
		   for(Object obj:listContent){  
		       fields=obj.getClass().getDeclaredFields();  
		       int j=0;  
		       for(Field v:fields){  
		           v.setAccessible(true);  
		           Object va=v.get(obj);  
		           if(va==null){  
		               va="";  
		           }  
		           sheet.addCell(new Label(j, i,va.toString()));  
		           j++;  
		       }  
		       i++;  
		   }  
		   /** **********将以上缓存中的内容写到EXCEL文件中******** */  
		   workbook.write();  
//		   /** *********关闭文件************* */  
		   workbook.close();     
		  
		  } catch (Exception e) {  
			  logger.error("导出失败！"+e.getMessage());
			  throw new Exception("导出失败"); 
		  }  
		 }  
	
	
	/**
	 * 
	 * @Author 文超群
	 * @since 2016-7-21 
	 * @version 1.0
	 * @return 导出Excel(只能导出本页的)
	 * @param src,JSONObject 
	 * */
	@SuppressWarnings("unchecked")
    public static void createExcel(String FileName, JSONObject json) throws Exception {
		FileName = FileName+"("+DateUtil.getMachingCurrentTime()+")";
		String src="/Users/LRover/Downloads/"+FileName+".xls";
        try {
            // 新建文件
            File file = new File(src);
            file.createNewFile();
            // 创建工作薄
            OutputStream outputStream = new FileOutputStream(file);
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(outputStream);
            // 创建新的一页
            WritableSheet sheet = writableWorkbook.createSheet("sheet1", 0);
            // 得到data对应的JSONArray
            JSONArray jsonArray = json.getJSONArray("rows");
            //单元格对象
            Label label;
            //列数计数
            int column = 0; 

            // 将第一行信息加到页中。
            JSONObject first = jsonArray.getJSONObject(0);
            Iterator<String> iterator = first.keys(); // 得到第一项的key集合
            while (iterator.hasNext()) { // 遍历key集合
                String key = (String) iterator.next(); // 得到key
                label = new Label(column++, 0, key); // 第一个参数是单元格所在列,第二个参数是单元格所在行,第三个参数是值
                sheet.addCell(label); // 将单元格加到页
            }
            System.out.println(jsonArray.size());
            // 遍历jsonArray
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = jsonArray.getJSONObject(i); // 得到数组的每项
                iterator = item.keys(); // 得到key集合
                column = 0;// 从第0列开始放
                while (iterator.hasNext()) {
                    String key = iterator.next(); // 得到key
                    String value = item.getString(key); // 得到key对应的value
                    label = new Label(column++, (i + 1), value); // 第一个参数是单元格所在列,第二个参数是单元格所在行,第三个参数是值
                    sheet.addCell(label); // 将单元格加到页
                }
            }
            writableWorkbook.write(); // 加入到文件中
            writableWorkbook.close(); // 关闭文件，释放资源
        } catch (Exception e) {
        	logger.error("导出失败！"+e.getMessage());
        	throw new Exception("导出失败");
        }
    }

}
