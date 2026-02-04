package prodotti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(
        basePackages = "prodotti.repository.audit"
)
public class StartProdottiMS {
	public static void main(String[] args) {
	    SpringApplication.run(StartProdottiMS.class, args);
	  }
}
