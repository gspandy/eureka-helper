package com.ls.eureka;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.annotation.JSONField;

public class InstanceInfo{
    private  String instanceId;
    private  String hostName;
    private  String app;
    private  String ipAddr;
    private  InstanceStatus status = InstanceStatus.UP;
    private  InstanceStatus overriddenstatus = InstanceStatus.UNKNOWN;
    private  PortWrapper port;
    private  PortWrapper securePort;
    private  int countryId; // Defaults to US
    private  DataCenterInfo dataCenterInfo;
    private  LeaseInfo leaseInfo;
    private  Map<String, String> metadata = new ConcurrentHashMap<String, String>();
    private  String homePageUrl;
    private  String statusPageUrl;
    private  String healthCheckUrl;
    private  String secureHealthCheckUrl;
    private  String vipAddress;
    private  String secureVipAddress;
    private  Boolean isCoordinatingDiscoveryServer = Boolean.FALSE;
    private  Long lastUpdatedTimestamp = System.currentTimeMillis();
    private  Long lastDirtyTimestamp = System.currentTimeMillis();
    private String statusPageRelativeUrl;
    private String healthCheckRelativeUrl;
    private String vipAddressUnresolved;
    private String secureVipAddressUnresolved;
    private String healthCheckExplicitUrl;
    private  boolean isUnsecurePortEnabled = true;
//    private  ActionType actionType;
    
    
    public enum InstanceStatus {
        UP, // Ready to receive traffic
        DOWN, // Do not send traffic- healthcheck callback failed
        STARTING, // Just about starting- initializations to be done - do not
        // send traffic
        OUT_OF_SERVICE, // Intentionally shutdown for traffic
        UNKNOWN;

        public static InstanceStatus toEnum(String s) {
            for (InstanceStatus e : InstanceStatus.values()) {
                if (e.name().equalsIgnoreCase(s)) {
                    return e;
                }
            }
            return UNKNOWN;
        }
    }

    public enum PortType {
        SECURE, UNSECURE
    }

    public enum ActionType {
        ADDED, // Added in the discovery server
        MODIFIED, // Changed in the discovery server
        DELETED
        // Deleted from the discovery server
    }
    
    /**
     * {@link InstanceInfo} JSON and XML format for port information does not follow the usual conventions, which
     * makes its mapping complicated. This class represents the wire format for port information.
     */
	public static class PortWrapper {
		@JSONField(name="@enabled")
        private boolean enabled;
		@JSONField(name="$")
        private int port;
        
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
    }
	
	public static class DataCenterInfo{
		private String name;
		@JSONField(name="@class")
		private String clazz;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getClazz() {
			return clazz;
		}
		public void setClazz(String clazz) {
			this.clazz = clazz;
		}
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getHomePageUrl() {
		return homePageUrl;
	}

	public void setHomePageUrl(String homePageUrl) {
		this.homePageUrl = homePageUrl;
	}

	public String getStatusPageUrl() {
		return statusPageUrl;
	}

	public void setStatusPageUrl(String statusPageUrl) {
		this.statusPageUrl = statusPageUrl;
	}

	public String getHealthCheckUrl() {
		return healthCheckUrl;
	}

	public void setHealthCheckUrl(String healthCheckUrl) {
		this.healthCheckUrl = healthCheckUrl;
	}

	public String getSecureHealthCheckUrl() {
		return secureHealthCheckUrl;
	}

	public void setSecureHealthCheckUrl(String secureHealthCheckUrl) {
		this.secureHealthCheckUrl = secureHealthCheckUrl;
	}

	public String getVipAddress() {
		return vipAddress;
	}

	public void setVipAddress(String vipAddress) {
		this.vipAddress = vipAddress;
	}

	public String getSecureVipAddress() {
		return secureVipAddress;
	}

	public void setSecureVipAddress(String secureVipAddress) {
		this.secureVipAddress = secureVipAddress;
	}

	public Boolean getIsCoordinatingDiscoveryServer() {
		return isCoordinatingDiscoveryServer;
	}

	public void setIsCoordinatingDiscoveryServer(Boolean isCoordinatingDiscoveryServer) {
		this.isCoordinatingDiscoveryServer = isCoordinatingDiscoveryServer;
	}

	public Long getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	public void setLastUpdatedTimestamp(Long lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	public Long getLastDirtyTimestamp() {
		return lastDirtyTimestamp;
	}

	public void setLastDirtyTimestamp(Long lastDirtyTimestamp) {
		this.lastDirtyTimestamp = lastDirtyTimestamp;
	}

	public String getStatusPageRelativeUrl() {
		return statusPageRelativeUrl;
	}

	public void setStatusPageRelativeUrl(String statusPageRelativeUrl) {
		this.statusPageRelativeUrl = statusPageRelativeUrl;
	}

	public String getHealthCheckRelativeUrl() {
		return healthCheckRelativeUrl;
	}

	public void setHealthCheckRelativeUrl(String healthCheckRelativeUrl) {
		this.healthCheckRelativeUrl = healthCheckRelativeUrl;
	}

	public String getVipAddressUnresolved() {
		return vipAddressUnresolved;
	}

	public void setVipAddressUnresolved(String vipAddressUnresolved) {
		this.vipAddressUnresolved = vipAddressUnresolved;
	}

	public String getSecureVipAddressUnresolved() {
		return secureVipAddressUnresolved;
	}

	public void setSecureVipAddressUnresolved(String secureVipAddressUnresolved) {
		this.secureVipAddressUnresolved = secureVipAddressUnresolved;
	}

	public String getHealthCheckExplicitUrl() {
		return healthCheckExplicitUrl;
	}

	public void setHealthCheckExplicitUrl(String healthCheckExplicitUrl) {
		this.healthCheckExplicitUrl = healthCheckExplicitUrl;
	}

	public boolean isUnsecurePortEnabled() {
		return isUnsecurePortEnabled;
	}

	public void setUnsecurePortEnabled(boolean isUnsecurePortEnabled) {
		this.isUnsecurePortEnabled = isUnsecurePortEnabled;
	}

	public InstanceStatus getStatus() {
		return status;
	}

	public void setStatus(InstanceStatus status) {
		this.status = status;
	}

	public InstanceStatus getOverriddenstatus() {
		return overriddenstatus;
	}

	public void setOverriddenstatus(InstanceStatus overriddenstatus) {
		this.overriddenstatus = overriddenstatus;
	}

	public DataCenterInfo getDataCenterInfo() {
		return dataCenterInfo;
	}

	public void setDataCenterInfo(DataCenterInfo dataCenterInfo) {
		this.dataCenterInfo = dataCenterInfo;
	}

	public LeaseInfo getLeaseInfo() {
		return leaseInfo;
	}

	public void setLeaseInfo(LeaseInfo leaseInfo) {
		this.leaseInfo = leaseInfo;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

//	public ActionType getActionType() {
//		return actionType;
//	}
//
//	public void setActionType(ActionType actionType) {
//		this.actionType = actionType;
//	}

	public PortWrapper getPort() {
		return port;
	}

	public void setPort(PortWrapper port) {
		this.port = port;
	}

	public PortWrapper getSecurePort() {
		return securePort;
	}

	public void setSecurePort(PortWrapper securePort) {
		this.securePort = securePort;
	}

}