package org.tokioschool.flightapp.flight.store.service;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import org.tokioschool.flightapp.flight.store.dto.ResourceContentDTO;
import org.tokioschool.flightapp.flight.store.dto.ResourceIdDTO;

import java.util.Optional;
import java.util.UUID;

public interface StoreService {

    Optional<ResourceIdDTO> saveResource(MultipartFile multipartFile, @Nullable String description);

    Optional<ResourceContentDTO> findResource(UUID resourceId);

    void deleteResource(UUID resourceId);
}
