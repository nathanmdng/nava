package com.nava;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.nava.service.NavaService;

@SpringBootApplication
public class Application {
	
   public static void main(String[] args) {
      ApplicationContext ctx = SpringApplication.run(Application.class, args);
      NavaService bean = ctx.getBean(NavaService.class);
      List<ResponseEntity<String>> responses = bean.start("booleanmeasures");
      for (ResponseEntity<String> response : responses) {
    	  	System.out.println("received response " + response.getBody() + " status code " + response.getStatusCodeValue());
      }
   }
   
   @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
   
}