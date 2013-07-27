package com.wise.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class WebService {
	/**
	 * ��ȡ���еĹ���·��
	 * @param url  ���ʵ�ַ
	 * @param nameSpace  �����ռ�
	 * @param methodName  webService�ӿ�
	 * @param timeout   ���ӳ�ʱ
	 * @return xml
	 * @throws Exception
	 */
	public static String SoapGetRoadList(String url , String nameSpace ,String methodName , int timeout) throws Exception{
		
		String soapAction = nameSpace + methodName;  
		 
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
	    envelope.bodyOut = rpc;  
	    
	    envelope.dotNet = false;   
        // �������ӳ�ʱʱ��Ϊ20��   
	    HttpTransportSE transportSE = new HttpTransportSE(url, timeout);
	    
	    transportSE.call(soapAction, envelope);
	    
    	String result = envelope.bodyIn.toString();
    	
		return result;
	}
	
	
	/**
	 * ͨ������·�߻�ȡվ����Ϣ
	 * @param url  ���ʵ�ַ
	 * @param nameSpace  �����ռ�
	 * @param methodName  webService�ӿ�
	 * @param roadName   ����·��
	 * @param timeout   ���ӳ�ʱ
	 * @return xml
	 * @throws Exception
	 */
	public static SoapObject SoapGetRoadStationInfo(String url , String nameSpace ,String methodName, String roadName, int timeout) throws Exception{
		
		//��ʼ��SOAP����
    	SoapObject request = new SoapObject(nameSpace, methodName);
    	//���ò���
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
	 * ��ȡ��һ������ʱ��
	 * @param url
	 * @param nameSpace
	 * @param methodName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static String SoapGetNearCar(String url , String nameSpace ,String methodName ,int stationId, int timeout) throws Exception{
		
		//��ʼ��SOAP����
    	SoapObject request = new SoapObject(nameSpace, methodName);
    	//���ò���
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
	 * ��ȡվ������Ϣ�����㳵����վʱ��
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
	 * ��ȡ�յ�����
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
	 * ��ȡ���µ�֪ͨ
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
	 * ��ѯ������·վ��λ����Ϣ
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
}