package com.michaelvol.bankingapp.HelloWorld;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HelloWorldController {

    @GetMapping("/hello-world")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/hello-person")
    public ResponseEntity<String> helloWorldCustom(@RequestParam(required = false) String name) {
        if(name == null || name.isEmpty()) {
            return ResponseEntity.ok("Hello World!");
        }
        return ResponseEntity.ok("Hello " + name + "!");
    }
}
