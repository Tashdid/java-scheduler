package com.tigerit.smartbill.scheduler.service.network;

import com.tigerit.smartbill.common.network.BaseServer;
import com.tigerit.smartbill.common.values.APIValues;
import com.tigerit.smartbill.scheduler.config.props.CustomAppProperties;

public class TigerITBillingServer extends BaseServer {

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

    public static void configure(CustomAppProperties customAppProperties) {
        if (customAppProperties != null) {
            if (serverIp == null) {
                serverIp = customAppProperties.getBillingServer();
            }

            if (serverPort == null) {
                serverPort = customAppProperties.getBillingPort();
            }

            if (serverContextPath.isEmpty()) serverContextPath = APIValues.BillingServer.CONTEXT_PATH;

            if (serverUsername == null) {
                serverUsername = customAppProperties.getBillingUsername();
            }

            if (serverPassword == null) {
                serverPassword = customAppProperties.getBillingPassword();
            }
        }
    }
}