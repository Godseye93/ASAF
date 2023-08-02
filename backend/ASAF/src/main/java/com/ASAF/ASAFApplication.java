package com.ASAF;

import com.ASAF.config.FirebaseCloudMessagingInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class ASAFApplication {

	public static void main(String[] args) {
		SpringApplication.run(ASAFApplication.class, args);
	}

	// FirebaseCloudMessagingInitializer 객체를 빈으로 생성하여 초기화 코드 실행
	@Bean
	public CommandLineRunner initFirebaseCloudMessaging() {
		return args -> {
			FirebaseCloudMessagingInitializer firebaseCloudMessagingInitializer = new FirebaseCloudMessagingInitializer();
			try {
				firebaseCloudMessagingInitializer.initializeFirebaseApp();
				System.out.println("Firebase Cloud Messaging 초기화 완료");
			} catch (IOException e) {
				System.err.println("Firebase Cloud Messaging 초기화 실패: " + e.getMessage());
			}
		};
	}
}
