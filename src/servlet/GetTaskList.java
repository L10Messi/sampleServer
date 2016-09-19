package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

import TaskAssignment.SamplingProcess;
import bean.VsamplingTask;



/**
 * Servlet implementation class GetTaskList
 */
@WebServlet("/GetTaskList")
public class GetTaskList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTaskList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//����������获取任务单测试数据
				/*TaskList taskReceived=new TaskList();
				taskReceived.setRunningNumber("PC00");
				taskReceived.setProductName("ѹ����");
				Vector qualityProperty=new Vector(3);
				//��ʼ��ʸ��
				Vector v=new Vector(4);	
				v.add("1");v.add("�ں�");	v.add("20");v.add("XXXXXXX");//�ں�
				 qualityProperty.add(v);
				 v=new Vector(4);	
				 v.add("2");v.add("�⾶");	v.add("20");v.add("XXXXXXX");//�⾶
				 qualityProperty.add(v);
				 v=new Vector(4);	
				 v.add("3");v.add("��ѹǿ��");	v.add("20");v.add("XXXXXXX");//��ѹǿ��
				 qualityProperty.add(v);	 
				taskReceived.setQualityProperty(qualityProperty);*/
				/////////////////////////////////////////////////////////////
				
				//读取客户端id
				String clientUser="";
				ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(request.getInputStream());			
					clientUser=(String)ois.readObject();
					System.out.println("clientID:"+clientUser);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ois.close();
				}
				
				SamplingProcess samplingProcess=new SamplingProcess();
				ResultSet rs=samplingProcess.sendInspectionInfoByClient(clientUser);
				
				//-------------------------生成结果集对象˵------------
				
				//取出Resultset中的记录。
				ArrayList<VsamplingTask> vSamples=new ArrayList<VsamplingTask>();
//				int count=0;//resultset中记录总数目				
				try{					
				while(rs.next())
				{  	 VsamplingTask vsa=new VsamplingTask();
				     vsa.setInspectedBy(rs.getString("inspectedBy"));
				     vsa.setLotNo(rs.getString("lotNo"));
				     vsa.setProductID(rs.getString("productID"));
				     vsa.setProductName(rs.getString("productName"));
				     vsa.setQualChrtID(rs.getString("qualChrtID"));
				     vsa.setQualChrtInsptionDescription(rs.getString("qualChrtInsptionDescription"));
				     vsa.setQualChrtName(rs.getString("qualChrtName"));
				     vsa.setSampleNo(rs.getString("sampleNo"));
				     vsa.setSamplingPlanID(rs.getString("samplingPlanID"));
				     vsa.setSerialID(rs.getString("serialID"));
				     vsa.setTaskID(rs.getString("taskID"));
				     vSamples.add(vsa);
//				     count++;					 
				}
				}catch(Exception e){
					
				}
				for (VsamplingTask vsamp: vSamples) {
					System.out.println(vsamp.getSerialID()+" "+vsamp.getLotNo());
				}
//				System.out.println(JSONArray.fromObject(vSamples).toString());

				//count=vSamples.size();
				//测试
				//System.out.println("样本数为"+count);
				//---------------------------------------
				
				//保存数据到sesssion中
				//session.setAttribute("countProduct",String.valueOf(count));//样本数
				//session.setAttribute("vSamples",vSamples);	
				//System.out.println((String)session.getAttribute("countProduct"));
				
				
				/*
				TaskList taskReceived=new TaskList();
				taskReceived.setRunningNumber(((VsamplingTask)vSamples.get(0)).getTaskID());
				taskReceived.setProductName(((VsamplingTask)vSamples.get(0)).getProductName());
				Vector qualityProperty=new Vector();
				Vector v=new Vector();
				v.add("1");
				v.add((((VsamplingTask)vSamples.get(0)).getQualChrtName()));
				v.add(String.valueOf(count));
				v.add((((VsamplingTask)vSamples.get(0)).getQualChrtInsptionDescription()));
				qualityProperty.add(v);
				taskReceived.setQualityProperty(qualityProperty);*/
				
				//-------------------------------------------------------------------
				
				//发送任务单给客户端//
				ObjectOutputStream oos=null;
				try {					
					oos=new ObjectOutputStream(response.getOutputStream());
					oos.writeObject(vSamples);
					oos.flush();
					oos.close();		
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					oos.close();
				}
				//////////////////////////////////////////////////////////////
	}

}
