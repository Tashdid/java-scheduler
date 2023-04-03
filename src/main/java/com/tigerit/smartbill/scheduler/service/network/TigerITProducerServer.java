package com.tigerit.smartbill.scheduler.service.network;

import com.tigerit.smartbill.common.network.BaseServer;
import com.tigerit.smartbill.common.values.APIValues;
import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;

public class TigerITProducerServer extends BaseServer {
	
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
	
	public TigerITProducerServer() {
		if (customAppProperties != null) {
			if (serverIp == null) serverIp = customAppProperties.getProducerServer();
			if (serverPort == null) serverPort = customAppProperties.getProducerPort();
			
			if (serverContextPath.isEmpty()) serverContextPath = APIValues.ProducerServer.CONTEXT_PATH;
			
	        if (serverUsername == null) serverUsername = customAppProperties.getProducerUsername();
	        if (serverPassword == null) serverPassword = customAppProperties.getProducerPassword();
		}
	}*/
	
	public static void configure(CustomAppProperties customAppProperties) {
		if (customAppProperties != null) {
			if (serverIp == null) {
				serverIp = customAppProperties.getProducerServer();
			}
			
			if (serverPort == null) {
				serverPort = customAppProperties.getProducerPort();
			}
			
			if (serverContextPath.isEmpty()) serverContextPath = APIValues.ProducerServer.CONTEXT_PATH;
			
			if (serverUsername == null) {
				serverUsername = customAppProperties.getProducerUsername();
			}
			
			if (serverPassword == null) {
				serverPassword = customAppProperties.getProducerPassword();
			}
		}
	}
}