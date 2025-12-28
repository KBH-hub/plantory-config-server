package com.zero.plantoryconfigserver;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootApplication
@EnableConfigServer
public class PlantoryConfigServerApplication {


    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        String b64 = dotenv.get("GIT_SSH_PRIVATE_KEY");
        if (b64 != null) {
            // 1) 따옴표 제거 + 앞뒤 trim
            b64 = b64.trim().replace("\"", "");

            // 2) base64에 섞인 공백/개행 전부 제거 (핵심)
            b64 = b64.replaceAll("\\s+", "");

            // 3) decode
            byte[] decoded = Base64.getDecoder().decode(b64);
            String privateKey = new String(decoded, StandardCharsets.UTF_8);

            // 4) Windows CRLF -> LF 정규화 (가끔 필요)
            privateKey = privateKey.replace("\r\n", "\n");

            // 5) 끝에 newline 보장 (파서가 민감한 경우 있음)
            if (!privateKey.endsWith("\n")) privateKey += "\n";

            // (검증용) 키 전체를 출력하지 말고 헤더s만 확인
            System.out.println("SSH key header: " + privateKey.lines().findFirst().orElse("(empty)"));

            System.setProperty("GIT_SSH_PRIVATE_KEY", privateKey);
        }

        SpringApplication.run(PlantoryConfigServerApplication.class, args);
    }

}
