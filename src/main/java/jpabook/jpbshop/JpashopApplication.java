package jpabook.jpbshop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

  public static void main(String[] args) {
    SpringApplication.run(JpashopApplication.class, args);
  }

  @Bean
  public Hibernate5Module getHibernate5Module() {
    Hibernate5Module module = new Hibernate5Module();

    //    module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);

    return module;
  }
}
