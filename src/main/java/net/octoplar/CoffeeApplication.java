package net.octoplar;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CoffeeApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CoffeeApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CoffeeApplication.class, args);
	}

}



//@SpringBootApplication
//@EnableCaching
//public class CoffeeApplication {
//	public static void main(String[] args) {
//		SpringApplication.run(CoffeeApplication.class, args);
//	}
//}
