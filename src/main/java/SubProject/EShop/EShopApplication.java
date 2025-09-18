package SubProject.EShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class EShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShopApplication.class, args);
	}

}
