package org.tokioschool.flightapp.helloworld.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.tokioschool.flightapp.helloworld.dto.HiMessageResponseDTO;
import org.tokioschool.flightapp.helloworld.service.HelloWorldService;

@Controller
@RequiredArgsConstructor
public class HelloWorldMvcController {

  private final HelloWorldService helloWorldService;

  @GetMapping("/helloworld")
  public ModelAndView getHi(@RequestParam(name = "name", required = false) String name) {

    HiMessageResponseDTO hiMessageResponseDTO = helloWorldService.getHiMessage(name);

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("helloworld/index");
    modelAndView.addObject("message", hiMessageResponseDTO.getMessage());
    return modelAndView;
  }
}
