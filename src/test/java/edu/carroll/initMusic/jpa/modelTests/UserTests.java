package edu.carroll.initMusic.jpa.modelTests;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.springframework.boot.test.context.SpringBootTest;

public class UserTests {
    private static final String username = "username";
    private static final String password = "password";
    private static final String firstName = "John";
    private static final String lastName = "Doe";
    private static final String email = "john.doe@example.com";
    private static final String country = "United States";

    public static void main(String[] args){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
}
