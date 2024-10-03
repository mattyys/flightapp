package org.tokioschool.flightapp.helloworld.service;

import org.tokioschool.flightapp.helloworld.dto.HiMessageResponseDTO;

public interface HelloWorldService {

    HiMessageResponseDTO getHiMessage(String name);
}
