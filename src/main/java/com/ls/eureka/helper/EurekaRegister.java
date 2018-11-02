package com.ls.eureka.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ls.eureka.Application;
import com.ls.eureka.InstanceInfo;
import com.ls.http.HttpUtils;

public class EurekaRegister {
	private Log log = LogFactory.getLog(EurekaRegister.class);
	private String grabURL = "http://10.206.20.197:8761"; // 拉取应用的注册中心地址
	private String registerURL = "http://localhost:8761"; // 将要注册的注册中心地址
	private Set<String> registerAppNames = new HashSet<String>();;
	List<Application> registedApps = new ArrayList<>();
	private Long renewIntervalTime = 30 * 1000L; // 心跳时间
	
	public EurekaRegister(){
		
		this(null);
	}
	
	public EurekaRegister(String configPath){
		try {
			readProperties(configPath);
		} catch (IOException e1) {
			throw new RuntimeException("读取配置文件application.properties失败!");
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						EurekaRegister.this.renew();
					} catch (ClientProtocolException e) {
						log.error("发送心跳检测报错！", e);
					} catch (IOException e) {
						log.error("发送心跳检测报错！", e);
					}
					
					try {
						Thread.sleep(renewIntervalTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void readProperties(String configPath) throws IOException{
		InputStream is = null;
		try {
			File file = new File("application.properties");
			if(configPath != null){
				is = new FileInputStream(configPath);
			}else if(file.exists()){
				is = new FileInputStream(file);
			}else{
				is = EurekaRegister.class.getClassLoader().getResourceAsStream("application.properties");
			}
			Properties prop = new Properties();
			prop.load(is);
			
			this.grabURL = prop.getProperty("eureka.helper.grabURL");
			this.registerURL = prop.getProperty("eureka.helper.registerURL");
			String apps = prop.getProperty("eureka.helper.registerAppNames");
			if(apps != null){
				registerAppNames.addAll(Arrays.asList(apps.split(",")));
			}
			log.info("需要注册的微服务个数：" + registerAppNames.size());
			
		} catch (Exception e) {
			throw e;
		}finally {
			if(is != null){
				is.close();
			}
		}
	}
	
	public List<Application> getApps() throws ClientProtocolException, IOException{
		String url = grabURL + "/eureka/apps";
		String responseStr = HttpUtils.get(url);
		JSONObject responseObj = JSON.parseObject(responseStr);
		
		JSONObject application = responseObj.getJSONObject("applications");
		JSONArray applications = application.getJSONArray("application");
		
		List<Application> apps = new ArrayList<>();
		if(applications != null && applications.size() > 0){
			for(int i = 0; i < applications.size(); i++){
				apps.add(formatApplication(applications.getObject(i, Application.class)));
			}
		}
		log.info("拉取应用数量：" + apps.size());
		
		return apps;
	}
	
	private Application formatApplication(Application application){
		if(application == null){
			return null;
		}
		
		if(application.getInstance() != null && application.getInstance().size() > 0){
			for (InstanceInfo ii : application.getInstance()) {
				ii.setHomePageUrl(ii.getHomePageUrl().replace(ii.getHostName(), ii.getIpAddr()));
				ii.setStatusPageUrl(ii.getStatusPageUrl().replace(ii.getHostName(), ii.getIpAddr()));
				ii.setHostName(ii.getIpAddr());
			}
		}
		
		return application;
	}
	
	public void registe(InstanceInfo instanceInfo) throws ClientProtocolException, IOException{
		String url = registerURL + "/eureka/apps/" + instanceInfo.getApp();
		JSONObject body = new JSONObject();
		body.put("instance", instanceInfo);
		HttpUtils.postJSON(url, body.toJSONString());
		log.info("注册服务：" + instanceInfo.getInstanceId());
	}
	
	public void initRegiste() throws ClientProtocolException, IOException{
		List<Application> apps = getApps();
		
		if(apps != null && apps.size() > 0){
			for (Application application : apps) {
				if(registerAppNames.contains(application.getName())){
					for(InstanceInfo instanceInfo : application.getInstance()){
						registe(instanceInfo);
					}
					registedApps.add(application);
				}
			}
		}
	}
	
	public void renew() throws ClientProtocolException, IOException{
		if(registedApps != null && registedApps.size() > 0){
			for (Application application : registedApps) {
				for (InstanceInfo ii : application.getInstance()) {
					renew(ii);
				}
			}
		}
	}
	
	public void renew(InstanceInfo instanceInfo) throws ClientProtocolException, IOException{
//		http://localhost:8761/eureka/apps/EGOV-MANA-UI/DESKTOP-BLOVI7D:egov-mana-ui?status=UP&lastDirtyTimestamp=1540816001325
		String url = registerURL + "/eureka/apps/" + instanceInfo.getApp() + "/" + instanceInfo.getInstanceId() //
				+ "?status=UP&lastDirtyTimestamp=" + instanceInfo.getLastDirtyTimestamp(); //
		HttpUtils.put(url);
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String configPath = null;
		if(args != null && args.length > 0){
			int len = args.length;
			for (int i = 0; i < len; i++) {
				if("-config".equals(args[i]) && i < len - 1){
					configPath = args[i + 1];
				}
			}
		}
		
		EurekaRegister eurekaRegister = new EurekaRegister(configPath);
		eurekaRegister.initRegiste();
	}
}
