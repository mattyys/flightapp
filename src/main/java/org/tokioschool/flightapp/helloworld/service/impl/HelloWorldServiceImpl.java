package org.tokioschool.flightapp.helloworld.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.helloworld.dto.HiMessageResponseDTO;
import org.tokioschool.flightapp.helloworld.service.HelloWorldService;

import java.util.Optional;

@Service
public class HelloWorldServiceImpl implements HelloWorldService {


    private static final String DEFAULT_HI_PHRASE = "Hello World dude";
    private static final String HI_PHRASE = "Hello World %s";


    @Override
    public HiMessageResponseDTO getHiMessage(String name) {
        String message = Optional.ofNullable(StringUtils.trimToNull(name))
                .map(HI_PHRASE::formatted)
                .orElse(DEFAULT_HI_PHRASE);

        return HiMessageResponseDTO.builder().message(message).build();

    }
}
