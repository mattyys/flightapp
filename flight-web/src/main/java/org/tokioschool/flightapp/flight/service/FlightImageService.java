package org.tokioschool.flightapp.flight.service;

import org.springframework.web.multipart.MultipartFile;
import org.tokioschool.flightapp.flight.domain.FlightImage;
import org.tokioschool.flightapp.flight.dto.FlightImageResourceDTO;

import java.util.UUID;

public interface FlightImageService {

    FlightImage saveImage(MultipartFile multipartFile);

    FlightImageResourceDTO getImage(UUID resourceId);

    void deleteImage(UUID resourceId);
}
