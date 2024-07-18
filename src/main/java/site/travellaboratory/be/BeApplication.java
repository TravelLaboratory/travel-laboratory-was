package site.travellaboratory.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import site.travellaboratory.be.article.domain._schedule.request.ArticleScheduleRequest;

@SpringBootApplication
public class BeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeApplication.class, args);
	}

}
