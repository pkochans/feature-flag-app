package biz.kochanski.featureflag.service;

import biz.kochanski.featureflag.dto.FeatureFlagDto;
import biz.kochanski.featureflag.entity.GlobalFeatureFlag;
import biz.kochanski.featureflag.entity.UserFeatureFlag;
import biz.kochanski.featureflag.exception.FeatureFlagAlreadyExistsException;
import biz.kochanski.featureflag.exception.MissingFeatureFlagException;
import biz.kochanski.featureflag.repository.GlobalFeatureRepository;
import biz.kochanski.featureflag.repository.UserFeatureRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Boolean.FALSE;

@Service
public class FeatureFlagService {
    private final GlobalFeatureRepository globalFeatureRepository;
    private final UserFeatureRepository userFeatureRepository;

    public FeatureFlagService(GlobalFeatureRepository globalFeatureRepository, UserFeatureRepository userFeatureRepository) {
        this.globalFeatureRepository = globalFeatureRepository;
        this.userFeatureRepository = userFeatureRepository;
    }

    public FeatureFlagDto createFeature(String name) {
        Optional<GlobalFeatureFlag> existing = globalFeatureRepository.getByName(name);
        if (existing.isPresent()) {
            throw new FeatureFlagAlreadyExistsException(name);
        } else {
            GlobalFeatureFlag globalFeatureFlag = globalFeatureRepository.save(new GlobalFeatureFlag(name, FALSE));
            return FeatureFlagDto.from(globalFeatureFlag);
        }
    }

    public FeatureFlagDto switchGlobalFeature(Long id, Boolean enabled) {
        GlobalFeatureFlag globalFeatureFlag = getGlobalFeature(id);
        globalFeatureFlag.setEnabled(enabled);
        GlobalFeatureFlag updatedFeatureFlag = globalFeatureRepository.save(globalFeatureFlag);
        return FeatureFlagDto.from(updatedFeatureFlag);
    }

    public Set<FeatureFlagDto> getAllEnabledFeatures(Boolean enabled, String username) {
        Collection<UserFeatureFlag> userEnabledFeatures = userFeatureRepository.getAllByUsernameAndEnabled(username, enabled);
        Collection<GlobalFeatureFlag> globalEnabledFeatures = globalFeatureRepository.getAllByEnabled(enabled);
        return Stream.concat(
                userEnabledFeatures.stream().map(FeatureFlagDto::from),
                globalEnabledFeatures.stream().map(FeatureFlagDto::from)
        ).collect(Collectors.toSet());
    }

    public FeatureFlagDto switchUserFeature(Long globalFeatureId, Boolean enabled, String username) {
        GlobalFeatureFlag globalFeature = getGlobalFeature(globalFeatureId);
        UserFeatureFlag userFeatureFlag = userFeatureRepository.save(new UserFeatureFlag(enabled, username, globalFeature));
        return FeatureFlagDto.from(userFeatureFlag);
    }

    private GlobalFeatureFlag getGlobalFeature(Long id) {
        return globalFeatureRepository.findById(id).orElseThrow(MissingFeatureFlagException::new);
    }
}
