package com.ig.poc.secure;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@RestController
public class TestController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> test() {
		final String uri = "https://api.uber.com/v1/products?latitude=37.33233139577873&longitude=-122.0312186703086&server_token=sUjg_HqcDtJp1lMPYwdNAf0RyvBNwm-wITJ9U3qK";
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri, String.class);
	    System.out.println(result);
			return new ResponseEntity<>(result,HttpStatus.OK);
		}
		

}
