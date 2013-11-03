package io.github.h4mu.kontroll.run;

import io.github.h4mu.kontroll.domain.Loader;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
public class LoaderRunner {
	public static void main(String[] args) {
		try(ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml")) {
			Loader loader = context.getBean("dataLoader", Loader.class);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Bean
	public Loader dataLoader() {
		return new Loader("http://www.bkk.hu/gtfs/budapest_gtfs.zip", System.out);
	}
}
