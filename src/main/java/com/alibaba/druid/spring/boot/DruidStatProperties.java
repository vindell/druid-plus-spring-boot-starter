package com.alibaba.druid.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.datasource.druid.stat")
public class DruidStatProperties {

	protected Boolean enabled = false;

	/**
	 * StatViewServlet 参数
	 */
	protected String servletPath;
	protected String sessionUserKey;
	protected String loginUsername;
	protected String loginPassword;
	protected String allow;
	protected String deny;
	protected String remoteAddress;
	protected Boolean resetEnable;
	protected String jmxUrl;
	protected String jmxUsername;
	protected String jmxPassword;

	/**
	 * WebStatFilter 参数
	 */
	protected String urlPatterns;
	protected Boolean profileEnable = false;
	protected Boolean sessionStatEnable = true;
	protected Boolean sessionStatMaxCount;
	protected String exclusions;
	protected String principalSessionName;
	protected String principalCookieName;
	protected String realIpHeader;

	/**
	 * JdkRegexpMethodPointcut 参数
	 */
	protected String[] pointcutPatterns;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	public String getSessionUserKey() {
		return sessionUserKey;
	}

	public void setSessionUserKey(String sessionUserKey) {
		this.sessionUserKey = sessionUserKey;
	}

	public String getLoginUsername() {
		return loginUsername;
	}

	public void setLoginUsername(String loginUsername) {
		this.loginUsername = loginUsername;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getAllow() {
		return allow;
	}

	public void setAllow(String allow) {
		this.allow = allow;
	}

	public String getDeny() {
		return deny;
	}

	public void setDeny(String deny) {
		this.deny = deny;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public Boolean getResetEnable() {
		return resetEnable;
	}

	public void setResetEnable(Boolean resetEnable) {
		this.resetEnable = resetEnable;
	}

	public String getJmxUrl() {
		return jmxUrl;
	}

	public void setJmxUrl(String jmxUrl) {
		this.jmxUrl = jmxUrl;
	}

	public String getJmxUsername() {
		return jmxUsername;
	}

	public void setJmxUsername(String jmxUsername) {
		this.jmxUsername = jmxUsername;
	}

	public String getJmxPassword() {
		return jmxPassword;
	}

	public void setJmxPassword(String jmxPassword) {
		this.jmxPassword = jmxPassword;
	}

	public String getUrlPatterns() {
		return urlPatterns;
	}

	public void setUrlPatterns(String urlPatterns) {
		this.urlPatterns = urlPatterns;
	}

	public Boolean getProfileEnable() {
		return profileEnable;
	}

	public void setProfileEnable(Boolean profileEnable) {
		this.profileEnable = profileEnable;
	}

	public Boolean getSessionStatEnable() {
		return sessionStatEnable;
	}

	public void setSessionStatEnable(Boolean sessionStatEnable) {
		this.sessionStatEnable = sessionStatEnable;
	}

	public Boolean getSessionStatMaxCount() {
		return sessionStatMaxCount;
	}

	public void setSessionStatMaxCount(Boolean sessionStatMaxCount) {
		this.sessionStatMaxCount = sessionStatMaxCount;
	}

	public String getExclusions() {
		return exclusions;
	}

	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}

	public String getPrincipalSessionName() {
		return principalSessionName;
	}

	public void setPrincipalSessionName(String principalSessionName) {
		this.principalSessionName = principalSessionName;
	}

	public String getPrincipalCookieName() {
		return principalCookieName;
	}

	public void setPrincipalCookieName(String principalCookieName) {
		this.principalCookieName = principalCookieName;
	}

	public String getRealIpHeader() {
		return realIpHeader;
	}

	public void setRealIpHeader(String realIpHeader) {
		this.realIpHeader = realIpHeader;
	}

	public String[] getPointcutPatterns() {
		return pointcutPatterns;
	}

	public void setPointcutPatterns(String[] pointcutPatterns) {
		this.pointcutPatterns = pointcutPatterns;
	}

}