package com.example.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloWorldController {
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index(@RequestParam String name) {
        List<byte[]> aa = new ArrayList<>();

        for (int i=0;i < 1000000;i++) {
            byte[] a = new byte[1024];
            aa.add(a);
        }

        return "hello " + name;
    }

}
