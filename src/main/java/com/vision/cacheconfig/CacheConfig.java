package com.vision.cacheconfig;

import java.net.URISyntaxException;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
	
	@Bean
	public CacheManager cacheManager() throws URISyntaxException {
		 CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
	        javax.cache.CacheManager jCacheManager = provider.getCacheManager(
	                getClass().getResource("/ehcache.xml").toURI(),
	                getClass().getClassLoader()
	        );
	        return new JCacheCacheManager(jCacheManager);
		
		
		
	}
	
	

}
