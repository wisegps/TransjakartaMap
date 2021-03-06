package com.wise.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class WebService {
	/**
	 * 获取所有的公交路线
	 * @param url  访问地址
	 * @param nameSpace  命名空间
	 * @param methodName  webService接口
	 * @param timeout   链接超时
	 * @return xml
	 * @throws Exception
	 */
	public static String SoapGetRoadList(String url , String nameSpace ,String methodName , int timeout) throws Exception{
		
		String soapAction = nameSpace + methodName;  
		 
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
	    envelope.bodyOut = rpc;  
	    
	    envelope.dotNet = false;   
        // 设置连接超时时间为20秒   
	    HttpTransportSE transportSE = new HttpTransportSE(url, timeout);
	    
	    transportSE.call(soapAction, envelope);
	    
    	String result = envelope.bodyIn.toString();
    	
		return result;
	}
	
	
	/**
	 * 通过公交路线获取站点信息
	 * @param url  访问地址
	 * @param nameSpace  命名空间
	 * @param methodName  webService接口
	 * @param roadName   公交路线
	 * @param timeout   链接超时
	 * @return xml
	 * @throws Exception
	 */
	public static SoapObject SoapGetRoadStationInfo(String url , String nameSpace ,String methodName, String roadName, int timeout) throws Exception{
		
		//初始化SOAP对象
    	SoapObject request = new SoapObject(nameSpace, methodName);
    	//调用参数
    	request.addProperty("p_strRoadName", roadName);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		SoapObject result = (SoapObject)envelope.getResponse();
		
		return result;
	}
	/**
	 * 获取下一辆到达时间
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String SoapGetNearCar(String url , String nameSpace ,String methodName ,int stationId, int timeout) throws Exception{
		
		//初始化SOAP对象
    	SoapObject request = new SoapObject(nameSpace, methodName);
    	//调用参数
    	request.addProperty("p_intPointID", stationId);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		SoapObject result = (SoapObject)envelope.getResponse();
		
		return result.toString();
	}
	/**
	 * 获取站点间的信息，计算车辆到站时间
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param p_intVehiclePointID
	 * @param p_intCurrPointID
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String GetNeedStation(String url , String nameSpace ,String methodName ,int p_intVehiclePointID,int p_intCurrPointID, int timeout) throws Exception{
		SoapObject request = new SoapObject(nameSpace, methodName);
    	request.addProperty("p_intVehiclePointID", p_intVehiclePointID);
    	request.addProperty("p_intCurrPointID", p_intCurrPointID);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		SoapObject result = (SoapObject)envelope.getResponse();		
		return result.toString();
	}
	/**
	 * 获取终点名称
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param p_intPointID
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String GetDestination2(String url , String nameSpace ,String methodName ,int p_intPointID, int timeout) throws Exception{
		SoapObject request = new SoapObject(nameSpace, methodName);
    	request.addProperty("p_intPointID", p_intPointID);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		return envelope.bodyIn.toString();
	}
	/**
	 * 获取最新的通知
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String GetAnnouncementInfo(String url , String nameSpace ,String methodName , int timeout) throws Exception{
		SoapObject request = new SoapObject(nameSpace, methodName);
    	request.addProperty("min", 0);
    	request.addProperty("max", 1);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		SoapObject result = (SoapObject)envelope.getResponse();		
		return result.toString();
	}
	/**
	 * 查询公交线路站点位置信息
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param p_strRoadName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String GetRoadStations(String url , String nameSpace ,String methodName ,String p_strRoadName, int timeout) throws Exception{
		SoapObject request = new SoapObject(nameSpace, methodName);
    	request.addProperty("p_strRoadName", p_strRoadName);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		SoapObject result = (SoapObject)envelope.getResponse();		
		return result.toString();
	}
	/**
	 * 根据站点获取线路
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param p_strRoadName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String GetStationsRoad(String url , String nameSpace ,String methodName ,String p_strRoadName, int timeout) throws Exception{
		SoapObject request = new SoapObject(nameSpace, methodName);
    	request.addProperty("p_strName", p_strRoadName);
    	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    	envelope.setOutputSoapObject(request);
    	envelope.dotNet = true;
    	HttpTransportSE ht = new HttpTransportSE(url,timeout);
    	ht.call(nameSpace + methodName, envelope);
		SoapObject result = (SoapObject)envelope.getResponse();		
		return result.toString();
	}
}