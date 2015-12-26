package com.ig.poc.secure;

/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.netflix.zuul.filters.ProxyRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ProxyRouteLocator.ProxyRouteSpec;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UrlPathHelper;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulHeaders;
import com.netflix.zuul.context.RequestContext;

@Slf4j
public class PreDecorationFilter extends ZuulFilter {

	private ProxyRouteLocator routeLocator;

	private boolean addProxyHeaders;

	private UrlPathHelper urlPathHelper = new UrlPathHelper();

	public PreDecorationFilter(ProxyRouteLocator routeLocator, boolean addProxyHeaders) {
		this.routeLocator = routeLocator;
		this.addProxyHeaders = addProxyHeaders;
	}

	@Override
	public int filterOrder() {
		return 5;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		
		
		return true;//!ctx.containsKey("forward.to");
	}
/*
	public Object run() {
		 final String uri = "https://api.uber.com/v1/products?latitude=37.33233139577873&longitude=-122.0312186703086&server_token=sUjg_HqcDtJp1lMPYwdNAf0RyvBNwm-wITJ9U3qK";
		    RestTemplate restTemplate = new RestTemplate();
		    String result = restTemplate.getForObject(uri, String.class);
		    System.out.println(result);
		return result;
	}
	*/
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
				
		
		String params[] ={"BLUE"};		
		ctx.getRequest().getParameterMap().put("TP_THEME", params);
		
		
		
		try{
		BufferedReader br = new BufferedReader(new  InputStreamReader(ctx.getRequest().getInputStream()));
	    StringBuffer json = new StringBuffer();
	    String line="";
	    while ((line = br.readLine()) != null)
	    {
	    	json.append(line);
	    }

	    System.out.println("-------------------------->"+json+"<--------------------------------");
	    
		}catch(Exception e){
	        	e.printStackTrace();
	        }
		final String requestURI = this.urlPathHelper
				.getPathWithinApplication(ctx.getRequest());
		ProxyRouteSpec route = this.routeLocator.getMatchingRoute(requestURI);
		if (route != null) {
			String location = route.getLocation();
			if (location != null) {
				ctx.put("requestURI", route.getPath());
				ctx.put("proxy", route.getId());

				if (route.getRetryable() != null) {
					ctx.put("retryable", route.getRetryable());
				}

				if (location.startsWith("http:") || location.startsWith("https:")) {
					ctx.setRouteHost(getUrl(location));
					ctx.addOriginResponseHeader("X-Zuul-Service", location);
				}
				else if (location.startsWith("forward:")) {
					ctx.set("forward.to", StringUtils.cleanPath(
							location.substring("forward:".length()) + route.getPath()));
					ctx.setRouteHost(null);
					return null;
				}
				else {
					// set serviceId for use in filters.route.RibbonRequest
					ctx.set("serviceId", location);
					ctx.setRouteHost(null);
					ctx.addOriginResponseHeader("X-Zuul-ServiceId", location);
				}
				
				if (this.addProxyHeaders) {
					ctx.addZuulRequestHeader("X-Forwarded-Host",
							ctx.getRequest().getServerName() + ":"
									+ String.valueOf(ctx.getRequest().getServerPort()));
					ctx.addZuulRequestHeader(ZuulHeaders.X_FORWARDED_PROTO,
							ctx.getRequest().getScheme());
					if (StringUtils.hasText(route.getPrefix())) {
						ctx.addZuulRequestHeader("X-Forwarded-Prefix", route.getPrefix());
					}
				}
				
			}
		}
		else {
			//log.warn("No route found for uri: " + requestURI);
			ctx.set("forward.to", requestURI);
		}
		
		return null;
	}

	private URL getUrl(String target) {
		try {
			return new URL(target);
		}
		catch (MalformedURLException ex) {
			throw new IllegalStateException("Target URL is malformed", ex);
		}
	}
}