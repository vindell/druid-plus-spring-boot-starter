package com.alibaba.druid.spring.boot.ds.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.druid.spring.boot.ds.DataSourceContextHolder;
import com.alibaba.druid.spring.boot.ds.annotation.DruidRepository;

/**
 * 
 * @className	： DruidRepositorySwitchAspect
 * @description	： 数据源自动切换切面
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年11月9日 下午12:52:31
 * @version 	V1.0
 */
@Aspect
@Component
public class DruidRepositorySwitchAspect {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Pointcut("@annotation(com.zfsoft.boot.druid.annotation.DruidRepository) and @annotation(repository)")
	public void switchRepository() {
	}

	@Around("switchRepository()")
	public Object around(ProceedingJoinPoint joinPoint, DruidRepository repository) throws Throwable {
		String oldRepository = DataSourceContextHolder.getDatabaseName();
    	try {
    		DataSourceContextHolder.setDatabaseName(repository.value());
    		return joinPoint.proceed();
        } finally {
        	if (logger.isDebugEnabled()) {
        		logger.debug("invoke(ProceedingJoinPoint) - end"); //$NON-NLS-1$
            }
    		DataSourceContextHolder.setDatabaseName(oldRepository);
        }
    }  
	
}
