package com.ig.poc.secure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class Application   {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
	
	/*
	 * To test manually whether third party secure site can be target for your load balancing or not. 
	TODO while uncommenting 
	1. Add implements CommandLineRunner to Aplication
	2. add below imports 
		import org.springframework.boot.CommandLineRunner;
		import org.springframework.cloud.netflix.zuul.filters.ProxyRouteLocator;
		import com.netflix.zuul.ZuulFilter;
		import org.springframework.web.client.RestTemplate;


	@Autowired
	private ProxyRouteLocator routeLocator;
	@Bean
	ZuulFilter getFilter() {
		return new PreDecorationFilter(routeLocator, true);
	}
	@Override
	public void run(String... args) throws Exception {
		final String uri = "https://api.com/v1/products?latitude=37.33233139577873&longitude=-122.0312186703086&server_token=sUjg_HqcDtJp1lMPYwdNAf0RyvBNwm-wITJ9U3qK";
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri, String.class);
	    System.out.println(result);
	}
	*/
}