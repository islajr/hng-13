package org.project.dynamicprofile.controller;

import org.project.dynamicprofile.dto.response.MainResponse;
import org.project.dynamicprofile.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/me")
    public ResponseEntity<MainResponse> generateMainResponse() {
        return mainService.generateMainResponse();
    }
}
