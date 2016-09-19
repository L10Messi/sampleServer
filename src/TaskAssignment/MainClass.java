package TaskAssignment;

import javax.xml.namespace.QName;   
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;  
import org.apache.axis.client.Service;  
import org.apache.axis.encoding.XMLType;  
public class MainClass {
	public static void main(String[] args) throws Exception{
		
		SamplingProcess obj = new SamplingProcess();
		//obj.GenSamplingPlan(30, 5, 0.01, 0, 0, 0, 1, "PA1120150514003002",0, "TASK01","SID0001", "SID0001000", "尼龙11压力管", "厚度", 0, 0, "c001,c002,c003,c004,c005"); 
		
		String a = "2";
		System.out.println("\n"+a.split(",")[0]);
		
		System.out.println("----------begin-------------");
		//obj.saveQualChrtResult("SID0001|SID0001000|151220220418|1", "TASK01", 0, "SID0001", "SID0001000", 0, 0, "PA1120150514003002", "USER_AA", "INSPECTION RESULT DETAIL");
//		obj.updateRejectedCount("TASK01", "SID0001", "SID0001000", 10, 0, 0, "PA1120150514003002");
		obj.updateRejectedCount("20160302012", "Client001", "Client001004", 10, 2, 0, "Client001004001");
		//int aa = obj.getRejectedCount("PA1120150514003002", "SID0001", "SID0001000", 0, 0, "TASK01");
		//System.out.println(aa);
		System.out.println("----------end-------------");
		
	}
	// x, "1", "0"
	public static void testPIR(){
		Object[] params = {"0", "0", "0", "0", "0", "0", "0", "3",
							"Q", "5", "0", "1", "3", "R", "5", "0","1", "3", "Q", "5",
							 "0","1", "3","1", "0"};
		Service service = new Service();  
        Call call;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(  
	                "http://10.42.0.23/sampling/service1.asmx"));  
	        call.setUseSOAPAction(true);   
	        call.setReturnType(XMLType.XSD_STRING);  
	        call.setOperationName(new QName("http://tempuri.org/", "ProcessInspectionResult"));  
	        String[] paramsName = {"s_lotNo","s_sampleNo","s_currStringency","s_i_needReduced","s_i_ssMethod",
					"s_switchScore","s_acj","s_normalPlanType","normalCharacterCode","normalSampleSize",
					"normalAc","normalRe","s_tightenedPlanType","tightenedCharacterCode","tightenedSampleSize",
					"tightenedAc","tightenedRe","s_reducedPlanType","reducedCharacterCode","reducedSampleSize",
					"reducedAc","reducedRe","s_nonconformities","s_acceptLot","s_reLotCumNum"
					};
	        //"s_acceptLot","s_reLotCumNum"
	        for(String paramName : paramsName){
	        	call.addParameter(new QName("http://tempuri.org/",paramName), XMLType.XSD_STRING, ParameterMode.IN);
	        }
	        call.setSOAPActionURI("http://tempuri.org/ProcessInspectionResult");  
	        String retVal1 = (String) call.invoke(params);  
	        System.out.println(retVal1);  
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	public static void test(){
		Object[] params = {"100000","6","0.01","0","0","0","1"};
		
        Service service = new Service();  
        Call call;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(  
	                "http://10.42.0.23/sampling/service1.asmx"));  
	        call.setUseSOAPAction(true);   
	        call.setReturnType(XMLType.XSD_STRING);  
	        call.setOperationName(new QName("http://tempuri.org/", "CreateSamplingPlan"));  
	        call.addParameter(new QName("http://tempuri.org/","s_lotSize"), XMLType.XSD_STRING, ParameterMode.IN);
	        call.addParameter(new QName("http://tempuri.org/","s_il"), XMLType.XSD_STRING, ParameterMode.IN);
	        call.addParameter(new QName("http://tempuri.org/","s_aql"), XMLType.XSD_STRING, ParameterMode.IN);
	        call.addParameter(new QName("http://tempuri.org/","s_normalPlanType"), XMLType.XSD_STRING, ParameterMode.IN);
	        call.addParameter(new QName("http://tempuri.org/","s_tightenedPlanType"), XMLType.XSD_STRING, ParameterMode.IN);
	        call.addParameter(new QName("http://tempuri.org/","s_reducedPlanType"), XMLType.XSD_STRING, ParameterMode.IN);
	        call.addParameter(new QName("http://tempuri.org/","s_needReduced"), XMLType.XSD_STRING, ParameterMode.IN);
//	        call.addParameter(new QName("http://tempuri.org/", "a"), XMLType.XSD_STRING, ParameterMode.IN);
//	       call.addParameter(new QName("http://tempuri.org/", "aql"), XMLType.XSD_STRING, ParameterMode.IN);
	        call.setSOAPActionURI("http://tempuri.org/CreateSamplingPlan");  
	        String retVal1 = (String) call.invoke(params);  
	        System.out.println(retVal1);  
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
	}
	
	
}
