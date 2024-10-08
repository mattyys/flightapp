package org.tokioschool.flightapp.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tokioschool.flightapp.core.exception.InternalErrorException;
import org.tokioschool.flightapp.core.exception.NotFoundException;
import org.tokioschool.flightapp.store.dto.ResourceContentDTO;
import org.tokioschool.flightapp.store.dto.ResourceCreateRequestDTO;
import org.tokioschool.flightapp.store.dto.ResourceIdDTO;
import org.tokioschool.flightapp.store.service.StoreService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/api/resources")
@Validated
@Tag(name = "Resources", description = "Resources operations")
public class ResourceApiController {

  private final StoreService storeService;

  @Operation(
      summary = "Get resource by resourceId",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "resource",
            content = @Content(schema = @Schema(implementation = ResourceContentDTO.class)))
      })
  @GetMapping("/{resourceId}")
  public ResponseEntity<ResourceContentDTO> getResourceContent(
      @PathVariable("resourceId") UUID resourceId) {

    ResourceContentDTO resourceContentDTO =
        storeService
            .findResource(resourceId)
            .orElseThrow(
                () -> new NotFoundException("Resource with id:%s not found".formatted(resourceId)));

    return ResponseEntity.ok(resourceContentDTO);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResourceIdDTO> createResource(
      @RequestPart("description") ResourceCreateRequestDTO resourceCreateDescriptionDTO,
      @RequestPart("content") MultipartFile multipartFile) {

    ResourceIdDTO resourceIdDTO =
        storeService
            .saveResource(multipartFile, resourceCreateDescriptionDTO.getDescription())
            .orElseThrow(
                () -> new InternalErrorException("There's been an error, try it again later"));

    return ResponseEntity.status(HttpStatus.CREATED).body(resourceIdDTO);
  }

  @DeleteMapping("/{resourceId}")
  public ResponseEntity<Void> deleteResourceById(@PathVariable("resourceId") UUID resourceId) {

    storeService.deleteResource(resourceId);

    return ResponseEntity.noContent().build();
  }
}
