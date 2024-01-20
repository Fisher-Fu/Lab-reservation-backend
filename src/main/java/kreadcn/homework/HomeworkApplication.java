package kreadcn.homework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(proxyBeanMethods = false)
@MapperScan(basePackages = "kreadcn.homework.dao", sqlSessionTemplateRef = "sqlSessionTemplate")
@EnableScheduling
@EnableCaching
public class HomeworkApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(HomeworkApplication.class, args);
    }

    @Override
    public void run(String... args) {

    }
}
