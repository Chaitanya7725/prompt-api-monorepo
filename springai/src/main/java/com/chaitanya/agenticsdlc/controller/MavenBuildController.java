package com.chaitanya.agenticsdlc.controller;

import com.chaitanya.agenticsdlc.service.MavenBuildService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/build")
public class MavenBuildController {

    private final MavenBuildService mavenBuildService;

    public MavenBuildController(MavenBuildService mavenBuildService) {
        this.mavenBuildService = mavenBuildService;
    }

    @PostMapping(
            value = "/clean",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> clean() {
        MavenBuildService.MavenCommandResult result =
                mavenBuildService.clean();

        return result.successful()
                ? ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(result.output())
                : ResponseEntity.internalServerError()
                .contentType(MediaType.TEXT_PLAIN)
                .body(result.output());
    }
}