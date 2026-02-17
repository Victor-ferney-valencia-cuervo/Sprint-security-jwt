package com.librarysystem.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Void> rootRedirect() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/books");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
