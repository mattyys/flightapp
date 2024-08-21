package org.tokioschool.flightapp.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tokioschool.flightapp.helloworld.dto.HiMessageResponseDTO;
import org.tokioschool.flightapp.helloworld.service.HelloWorldService;

@RestController
@RequestMapping("/helloworld/api/hi")
@RequiredArgsConstructor
public class HelloWorldApiController {

    private final HelloWorldService helloWorldService;

    @GetMapping
    public ResponseEntity<HiMessageResponseDTO> getHi(
            @RequestParam(value = "name", required = false) String name){
        HiMessageResponseDTO hiMessageResponseDTO = helloWorldService.getHiMessage(name);

        return ResponseEntity.ok(hiMessageResponseDTO);
    }

}
