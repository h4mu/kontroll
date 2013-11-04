package io.github.h4mu.kontroll.run;

import io.github.h4mu.kontroll.domain.Loader;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Configuration
public class LoaderRunner {
	public static void main(String[] args) {
		String url = "http://www.bkk.hu/gtfs/budapest_gtfs.zip";
		if (args.length < 1) {
			System.out.println("Usage: LoaderRunner <url of GTFS zip>\nUsing \"" + url + "\" as default url.");
		} else {
			url = args[0];
		}
		try(ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml")) {
			Loader loader = context.getBean("dataLoader", Loader.class);
			loader.load(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Bean
	public Loader dataLoader() {
		return new Loader(System.out);
	}
}
