/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.ai.studio.admin.builder.config;

import com.alibaba.cloud.ai.studio.admin.builder.resolver.ApiModelAttributeMethodArgumentResolver;
import org.jetbrains.annotations.NotNull;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.util.List;

/**
 * Web configuration class for Spring MVC. Handles CORS, resource mapping, custom
 * argument resolvers, and view controllers.
 *
 * @since 1.0.0.3
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new ApiModelAttributeMethodArgumentResolver());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("X-AGENTSCOPE-WORKSPACE", "Authorization", "Content-Type", "X-Requested-With", "Accept",
					"Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
			.allowCredentials(true)
			.maxAge(3600);
	}

	/**
	 * Configures static resource handling and SPA routing. Maps all requests to
	 * classpath:/static/ directory. Returns index.html for non-existent resources to
	 * support SPA routing. Excludes /console/v1, /api/v1, and /api/ paths from static resource
	 * handling.
	 * @param registry Resource handler registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Configure static resource mapping with cache
		registry.addResourceHandler("/static/**")
			.addResourceLocations("classpath:/static/")
			.setCachePeriod(3600);

		// Configure frontend resource mapping - prioritize static files
		registry.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/")
			.resourceChain(true)
			.addResolver(new PathResourceResolver() {
				@Override
				protected Resource getResource(@NotNull String resourcePath, @NotNull Resource location)
						throws IOException {
					// Exclude /console/v1, /api/v1, and /api/ prefixed requests
					if (resourcePath.startsWith("console/v1/") || resourcePath.startsWith("api/v1/")
							|| resourcePath.startsWith("api/")) {
						return null;
					}

					// Try to get the requested resource
					Resource requestedResource = location.createRelative(resourcePath);

					// If the requested file exists and is readable, return it directly
					if (requestedResource.exists() && requestedResource.isReadable()) {
						return requestedResource;
					}

					// For all other paths (frontend routes), return index.html
					return new ClassPathResource("/static/index.html");
				}
			});
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// Configure root path redirect to index.html
		registry.addViewController("/")
			.setViewName("forward:/index.html");
	}

}
