package org.tokioschool.flightapp.flight.mvc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("flight")
public class IndexMvcController {

    @GetMapping
    public String getIndex(){
        return "flight/index";
    }


}
