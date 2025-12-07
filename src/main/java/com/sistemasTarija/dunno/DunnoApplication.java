package com.sistemasTarija.dunno;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class DunnoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DunnoApplication.class, args);
	}
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/La_Paz"));
    }
}
