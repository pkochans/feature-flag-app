package biz.kochanski.featureflag.controller;

import biz.kochanski.featureflag.dto.FeatureFlagDto;
import biz.kochanski.featureflag.service.FeatureFlagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Set;

@RestController
public class FeatureFlagController {
    private final FeatureFlagService featureFlagService;

    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/v1/features")
    public FeatureFlagDto createFeature(@RequestParam @NotBlank String name) {
        return featureFlagService.createFeature(name);
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/v1/features/{id}")
    public FeatureFlagDto switchGlobalFeature(@PathVariable @NotNull Long id, @RequestParam Boolean enabled) {
        return featureFlagService.switchGlobalFeature(id, enabled);
    }

    @RolesAllowed("ADMIN")
    @PutMapping("/v1/features/{id}/user/{username}")
    public FeatureFlagDto switchUserFeature(@PathVariable @NotNull Long id, @PathVariable @NotBlank String username, @RequestParam Boolean enabled) {
        return featureFlagService.switchUserFeature(id, enabled, username);
    }

    @RolesAllowed("USER")
    @GetMapping("/v1/features")
    public Set<FeatureFlagDto> getAllEnabledFeatures(@RequestParam Boolean enabled, Principal principal) {
        return featureFlagService.getAllEnabledFeatures(enabled, principal.getName());
    }
}
