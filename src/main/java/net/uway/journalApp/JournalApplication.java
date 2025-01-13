package net.uway.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication
@EnableTransactionManagement
public class JournalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalApplication.class, args);

		// After the application has started, open the home page
		//openHomePage();
	}

//	private static void openHomePage() {
//		try {
//			String os = System.getProperty("os.name").toLowerCase();
//			String url = "http://127.0.0.1:8080/index.html";
//			if (os.contains("win")) {
//				// Windows
//				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
//			} else if (os.contains("mac")) {
//				// MacOS
//				Runtime.getRuntime().exec("open " + url);
//			} else if (os.contains("nix") || os.contains("nux")) {
//				// Linux/Unix
//				Runtime.getRuntime().exec("xdg-open " + url);
//			} else {
//				System.out.println("Unsupported OS. Please open the URL manually.");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
