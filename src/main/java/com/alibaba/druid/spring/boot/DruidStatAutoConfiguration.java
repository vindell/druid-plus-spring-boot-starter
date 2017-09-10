package com.alibaba.druid.spring.boot;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;

/**
 * 
 * @className ： DruidStatAutoConfiguration
 * @description ：开启SQL监控和防御SQL注入攻击功能
 * @author ： <a href="https://github.com/vindell">vindell</a>
 * @date ： 2017年8月19日 下午4:39:23
 * @version V1.0
 */
@Configuration
@ConditionalOnBean( DruidDataSource.class )
@ConditionalOnClass({ DruidDataSource.class, StatViewServlet.class })
@ConditionalOnProperty(prefix = DruidStatProperties.PREFIX, value = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({ DruidStatProperties.class })
public class DruidStatAutoConfiguration {

	/**
	 * 
	 * 注册一个Druid内置的StatViewServlet，用于展示Druid的统计信息。
	 * Druid内置提供了一个StatViewServlet用于展示Druid的统计信息。这个StatViewServlet的用途包括： -
	 * 提供监控信息展示的html页面 - 提供监控信息的JSON API 注意：使用StatViewServlet，建议使用druid 0.2.6以上版本。
	 */
	@Bean
	@ConditionalOnMissingBean(value = StatViewServlet.class)
	public ServletRegistrationBean druidServletRegistrationBean(DruidStatProperties statProperties) {
		if (StringUtils.isEmpty(statProperties.getServletPath())) {
			statProperties.setServletPath("/druid/*");
		}
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				statProperties.getServletPath());
		// 添加初始化参数：initParams

		// 是否能够重置数据(禁用HTML页面上的“Reset All”功能)
		servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_RESET_ENABLE,
				statProperties.getResetEnable().toString());

		// 用户对象在Session中存储值所用的Key
		if (StringUtils.isNotEmpty(statProperties.getSessionUserKey())) {
			servletRegistrationBean.addInitParameter(StatViewServlet.SESSION_USER_KEY,
					statProperties.getSessionUserKey());
		}

		// 登录查看信息的账号密码.
		if (StringUtils.isNotEmpty(statProperties.getLoginUsername())
				&& StringUtils.isNotEmpty(statProperties.getLoginPassword())) {
			servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_USERNAME,
					statProperties.getLoginUsername());
			servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_PASSWORD,
					statProperties.getLoginPassword());
		}

		// 白名单 (没有配置或者为空，则允许所有访问)
		if (StringUtils.isNotEmpty(statProperties.getAllow())) {
			servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_ALLOW, statProperties.getAllow());
		}
		// IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not
		// permitted to view this page.
		if (StringUtils.isNotEmpty(statProperties.getDeny())) {
			servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_DENY, statProperties.getDeny());
		}

		// 运行访问的IP
		if(StringUtils.isNotEmpty(statProperties.getRemoteAddress())) {
			servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_REMOTE_ADDR, statProperties.getRemoteAddress());
		}

		// JMX配置
		if(StringUtils.isNotEmpty(statProperties.getJmxUrl()) 
				&& StringUtils.isNotEmpty(statProperties.getJmxUsername()) 
				&& StringUtils.isNotEmpty(statProperties.getJmxPassword())) {
			
			servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_JMX_URL, statProperties.getJmxUrl());
			servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_JMX_USERNAME,
					statProperties.getJmxUsername());
			servletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_JMX_PASSWORD,
					statProperties.getJmxPassword());
		}

		return servletRegistrationBean;
	}

	/**
	 * 注册一个：filterRegistrationBean,添加请求过滤规则
	 */
	@Bean
	@ConditionalOnMissingBean(value = WebStatFilter.class)
	public FilterRegistrationBean duridFilterRegistrationBean(DruidStatProperties statProperties) {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 添加过滤规则.
		if (StringUtils.isEmpty(statProperties.getUrlPatterns())) {
			statProperties.setUrlPatterns("/*");
		}
		filterRegistrationBean.addUrlPatterns(statProperties.getUrlPatterns());

		// Session监控配置
		if (StringUtils.isEmpty(statProperties.getExclusions())) {
			
		}
		filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_PROFILE_ENABLE,
				statProperties.getProfileEnable().toString());
		filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_SESSION_STAT_ENABLE,
				statProperties.getSessionStatEnable().toString());
		filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_SESSION_STAT_MAX_COUNT,
				statProperties.getSessionStatMaxCount().toString());

		// 添加不需要忽略的格式信息.
		if (StringUtils.isEmpty(statProperties.getExclusions())) {
			statProperties.setExclusions("*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		}
		// 设置忽略请求
		filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_EXCLUSIONS, statProperties.getExclusions());

		if (StringUtils.isNotEmpty(statProperties.getPrincipalSessionName())) {
			filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_PRINCIPAL_SESSION_NAME,
					statProperties.getPrincipalSessionName());
		}
		if (StringUtils.isNotEmpty(statProperties.getPrincipalCookieName())) {
			filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_PRINCIPAL_COOKIE_NAME,
					statProperties.getPrincipalCookieName());
		}
		if (StringUtils.isNotEmpty(statProperties.getRealIpHeader())) {
			filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_REAL_IP_HEADER,
					statProperties.getRealIpHeader());
		}

		return filterRegistrationBean;
	}

	/*
	 * 6. Spring关联监控配置 Druid提供了Spring和Jdbc的关联监控。
	 * com.alibaba.druid.support.spring.stat.DruidStatInterceptor是一个标准的Spring
	 * MethodInterceptor。 可以灵活进行AOP配置。在DruidConfiguration中配置：
	 */

	/**
	 * 监听Spring 1.定义拦截器 2.定义切入点 3.定义通知类
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(value = DruidStatInterceptor.class)
	public DruidStatInterceptor druidStatInterceptor(DruidStatProperties statProperties) {
		return new DruidStatInterceptor();
	}

	@Bean("druidStatPointcut")
	@ConditionalOnMissingBean(name = "druidStatPointcut")
	public JdkRegexpMethodPointcut druidStatPointcut(DruidStatProperties statProperties) {
		JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();
		druidStatPointcut.setPatterns(statProperties.getPointcutPatterns());
		return druidStatPointcut;
	}

	@Bean("druidStatAdvisor")
	@ConditionalOnMissingBean(name = "druidStatAdvisor")
	public Advisor druidStatAdvisor(DruidStatProperties statProperties) {
		return new DefaultPointcutAdvisor(druidStatPointcut(statProperties), druidStatInterceptor(statProperties));
	}

}
