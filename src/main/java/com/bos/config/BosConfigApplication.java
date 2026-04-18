package com.bos.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BosConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(BosConfigApplication.class, args);
	}

	//docker run -d --name mysql_bos -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=bos_db -p 3306:3306 -v mysql_data:/var/lib/mysql mysql:8.0
	//docker exec -it mysql_bos mysql -uroot -proot123
	//docker start mysql_bos
	//docker stop mysql_tracker
	//docker rm mysql_tracker

}
