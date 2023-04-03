package com.tigerit.smartbill.scheduler.service.network;

import com.tigerit.smartbill.common.network.BaseServer;
import com.tigerit.smartbill.common.values.APIValues;
import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;

public class TigerITCommunicationServer extends BaseServer {

	//static protected String serverProtocol = "http";
	static protected String serverIp = null;
	static protected String serverPort = null;
	static protected String serverContextPath = "";
	static protected String serverUsername = null;
	static protected String serverPassword = null;

	//@Override
	//public String getServerProtocol() { return serverProtocol; }
	
	@Override
	public String getServerIp() { return serverIp; }
	
	@Override
	public String getServerPort() { return serverPort; }
	
	@Override
	public String getServerContextPath() { return serverContextPath; }
	
	@Override
	public String getServerUsername() { return serverUsername; }
	
	@Override
	public String getServerPassword() { return serverPassword; }
	
	/*@Autowired
	CustomAppProperties customAppProperties;
	
	public TigerITCommunicationServer() {
		if (customAppProperties != null) {
			if (serverIp == null) serverIp = customAppProperties.getCommunicationServer();
			if (serverPort == null) serverPort = customAppProperties.getCommunicationPort();
			
			if (serverContextPath.isEmpty()) serverContextPath = APIValues.CommunicationServer.CONTEXT_PATH;
			
	        if (serverUsername == null) serverUsername = customAppProperties.getCommunicationUsername();
	        if (serverPassword == null) serverPassword = customAppProperties.getCommunicationPassword();
		}
	}*/

	public static void configure(CustomAppProperties customAppProperties) {
		if (customAppProperties != null) {
			if (serverIp == null) {
				serverIp = customAppProperties.getCommunicationServer();
			}
			
			if (serverPort == null) {
				serverPort = customAppProperties.getCommunicationPort();
			}
			
			if (serverContextPath.isEmpty()) serverContextPath = APIValues.CommunicationServer.CONTEXT_PATH;
			
			if (serverUsername == null) {
				serverUsername = customAppProperties.getCommunicationUsername();
			}
			
			if (serverPassword == null) {
				serverPassword = customAppProperties.getCommunicationPassword();
			}
		}
	}
	/*


	public Object getData(String endPoint) {
		if (serverIp == null || serverPort == null || endPoint == null) return null;

		RestTemplate restTemplate = new RestTemplate();
		if (serverUsername != null && serverPassword != null) {
			restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(serverUsername, serverPassword));
		}

		String postUrl = serverProtocol + "://" + serverIp + ":" + serverPort;
		postUrl += APIValues.CommunicationServer.CONTEXT_PATH;;
		postUrl += endPoint;

		Object payload = null;
		try {
			ResponseEntity<GenericAPIResponse> response = restTemplate.exchange(postUrl, HttpMethod.GET, null, GenericAPIResponse.class);
			payload = ResponseConverter.convertResponseToPayload(response);
		}
		catch (Exception ex) {
			log.error(ex.getMessage());
		}

		if (payload != null) log.info("payload=" + payload);

		return payload;
	}

	public Object postData(String endPoint, HttpEntity<?> postData) {
		if (serverIp == null || serverPort == null || endPoint == null) return null;

		RestTemplate restTemplate = new RestTemplate();
		if (serverUsername != null && serverPassword != null) {
			restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(serverUsername, serverPassword));
		}

		String postUrl = serverProtocol + "://" + serverIp + ":" + serverPort;
		postUrl += APIValues.CommunicationServer.CONTEXT_PATH;;
		postUrl += endPoint;

		Object payload = null;
		try {
			ResponseEntity<GenericAPIResponse> response = restTemplate.exchange(postUrl, HttpMethod.POST, postData, GenericAPIResponse.class);
			payload = ResponseConverter.convertResponseToPayload(response);
		}
		catch (Exception ex) {
			log.error(ex.getMessage());
		}

		if (payload != null) log.info("payload=" + payload);

		return payload;
	}

	 */
}