package com.treszyk.campaigns;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdCampaignManagerApplication {

  public static void main(String[] args) {
    try {
      String directory = "./";
      if (java.nio.file.Files.exists(java.nio.file.Paths.get("../.env"))) {
        directory = "../";
      }
      Dotenv dotenv =
          Dotenv.configure().directory(directory).ignoreIfMalformed().ignoreIfMissing().load();

      dotenv
          .entries()
          .forEach(
              entry -> {
                if (System.getProperty(entry.getKey()) == null
                    && System.getenv(entry.getKey()) == null) {
                  System.setProperty(entry.getKey(), entry.getValue());
                }
              });
    } catch (Exception e) {
      // Ignored if .env file is missing in production environments
    }

    SpringApplication.run(AdCampaignManagerApplication.class, args);
  }
}
