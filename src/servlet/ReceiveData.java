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

import TaskAssignment.SampProcess;
import TaskAssignment.SamplingProcess;
import bean.SampleResult;
import bean.VsamplingTask;


/**
 * Servlet implementation class ReceiveData
 */
@WebServlet("/ReceiveData")
public class ReceiveData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReceiveData() {
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
		
		String dataReceived=null;
		ArrayList vSamples=null;
		SampleResult sampleResult=null;
		
		//接收客户端传来的数据
		ObjectInputStream ois = null;
		try {			
			ois = new ObjectInputStream(request.getInputStream());				
			sampleResult=(SampleResult)ois.readObject();	
		} catch (Exception e) {
			System.out.println("transmission error!");
			e.printStackTrace();
		} finally {
			ois.close();
		}
		
		
		//-------------解析得到服务器需要的参数-------------
		dataReceived=sampleResult.dataSent;
		vSamples=sampleResult.vSamples;	
		
		//以下为服务器吴刚做的模块需要客户端回传的信息
//		System.out.println(dataReceived);//测试
//		System.out.println(vSamples);
		
		
		//来自原结果集的信息		
		//产品id
		String productID= ((VsamplingTask)(vSamples.get(0))).getProductID();
		//质量特性id
		String qualChrtID= ((VsamplingTask)(vSamples.get(0))).getQualChrtID();
		//抽样计划id
		String samplingPlanID= ((VsamplingTask)(vSamples.get(0))).getSamplingPlanID();
		//检验员（客户端id）
		String taskID= ((VsamplingTask)(vSamples.get(0))).getTaskID();
		//抽样号
        int sampleNo= Integer.parseInt(((VsamplingTask)(vSamples.get(0))).getSampleNo());
		//抽样号
		int lotNo= Integer.parseInt(((VsamplingTask)(vSamples.get(0))).getLotNo());
		//客户端
		String userID= ((VsamplingTask)(vSamples.get(0))).getInspectedBy();
	//------------------------------------------------------------------
		
		//返回服务器调用吴刚模块
//		SamplingProcess samplingProcess=new SamplingProcess();
//		samplingProcess.updateRejectedCount(taskID, productID, qualChrtID, countUnqualified[0], lotNo, sampleNo, samplingPlanID);
		SampProcess sp=new SampProcess();
		//调用程序存储终端接收的结果。
		dataReceived +="#10#";
//		String ss[]=dataReceived.split("#");
		sp.handleResult(vSamples, taskID, productID, qualChrtID, lotNo, sampleNo, samplingPlanID, userID, dataReceived);
//		for (int i=0,j=0;i<vSamples.size()&j<ss.length; i++,j++) {
//			String inspectionResultDetail=ss[j];
//			String serialID=((VsamplingTask)(vSamples.get(i))).getSerialID();
////			System.out.print(i);
////			System.out.println(inspectionResultDetail);
////			System.out.println(serialID);
////			samplingProcess.saveQualChrtResult(serialID, taskID, productID, qualChrtID, lotNo, sampleNo, samplingPlanID, userID, inspectionResultDetail);
//		}
		
		//向客户端返回确认数据已收到信息/////////////////////////////////////		
		ObjectOutputStream oos=null;
		try {
			oos=new ObjectOutputStream(response.getOutputStream());
			oos.writeObject("Data accepted!");
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
