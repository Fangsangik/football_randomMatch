package com.side.football_project.domain.address.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddressController {

    @GetMapping("/address")
    public String address() {
        // 주소 페이지로 이동
        return "address";
    }
}
