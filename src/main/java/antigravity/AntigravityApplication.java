package antigravity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
public class AntigravityApplication {

  public static void main(String[] args) {
    SpringApplication.run(AntigravityApplication.class, args);
  }
}
