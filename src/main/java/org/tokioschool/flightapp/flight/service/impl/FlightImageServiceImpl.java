package org.tokioschool.flightapp.flight.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.tokioschool.flightapp.flight.domain.FlightImage;
import org.tokioschool.flightapp.flight.dto.FlightImageResourceDTO;
import org.tokioschool.flightapp.flight.service.FlightImageService;
import org.tokioschool.flightapp.flight.store.dto.ResourceContentDTO;
import org.tokioschool.flightapp.flight.store.dto.ResourceIdDTO;
import org.tokioschool.flightapp.flight.store.service.StoreService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightImageServiceImpl implements FlightImageService {

  private final StoreService storeService;

  @Override
  public FlightImage saveImage(MultipartFile multipartFile) {

    ResourceIdDTO resourceIdDTO =
        storeService
            .saveResource(multipartFile, "flight-app")
            .orElseThrow(() -> new IllegalArgumentException("Resource nos saved in store"));

    return FlightImage.builder()
        .contentType(multipartFile.getContentType())
        .size((int) multipartFile.getSize())
        .resourceId(resourceIdDTO.getResourceId())
        .filename(multipartFile.getOriginalFilename())
        .build();
  }

  @Override
  public FlightImageResourceDTO getIMage(UUID resourceId) {

    ResourceContentDTO resourceContentDTO =
        storeService
            .findResource(resourceId)
            .orElseThrow(() -> new IllegalArgumentException("Resource not found in store"));

    return FlightImageResourceDTO.builder()
        .contentType(resourceContentDTO.getContentType())
        .content(resourceContentDTO.getContent())
        .filename(resourceContentDTO.getFilename())
        .size(resourceContentDTO.getSize())
        .build();
  }

  @Override
  public void deleteImage(UUID resourceId) {
    storeService.deleteResource(resourceId);
  }
}
