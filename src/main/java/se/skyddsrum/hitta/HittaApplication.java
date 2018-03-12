package se.skyddsrum.hitta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import se.skyddsrum.hitta.remote.MsbDataDownloader;
import se.skyddsrum.hitta.remote.impl.MsbDataDownloaderImpl;
import se.skyddsrum.hitta.storage.ShelterRepository;

@SpringBootApplication
public class HittaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HittaApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public MsbDataDownloader msbDataDownloader(RestTemplate restTemplate, ShelterRepository shelterRepository) {
		return new MsbDataDownloaderImpl(restTemplate, shelterRepository);
	}
}
