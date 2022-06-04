package biz.kochanski.featureflag;

import biz.kochanski.featureflag.dto.FeatureFlagDto;
import biz.kochanski.featureflag.exception.FeatureFlagAlreadyExistsException;
import biz.kochanski.featureflag.exception.MissingFeatureFlagException;
import biz.kochanski.featureflag.service.FeatureFlagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class FeatureFlagServiceComponentTest {
    private static final String USER_NAME = "user";
    private static final String OTHER_USER_NAME = "nobody";
    private static final String FEATURE_NAME = "feature1";
    private static final String OTHER_FEATURE_NAME = "feature2";
    private static final long MISSING_FEATURE_ID = 99L;

    @Autowired
    private FeatureFlagService service;

    @Test
    void shouldGetBothGlobalAndUserEnabledFeatures() {
        FeatureFlagDto featureEnabledGlobally = service.createFeature(FEATURE_NAME);
        FeatureFlagDto featureEnabledForUser = service.createFeature(OTHER_FEATURE_NAME);
        service.switchGlobalFeature(featureEnabledGlobally.id(), true);
        service.switchUserFeature(featureEnabledForUser.id(), true, USER_NAME);

        Set<FeatureFlagDto> allEnabledFeatures = service.getAllEnabledFeatures(true, USER_NAME);

        assertThat(getIds(allEnabledFeatures)).contains(featureEnabledGlobally.id(), featureEnabledForUser.id());
    }

    @Test
    void shouldNotGetDisabledFeature() {
        FeatureFlagDto featureDisabledGlobally = service.createFeature(FEATURE_NAME);

        Set<FeatureFlagDto> allEnabledFeatures = service.getAllEnabledFeatures(true, USER_NAME);

        assertThat(getIds(allEnabledFeatures)).doesNotContain(featureDisabledGlobally.id());
    }

    @Test
    void shouldNotGetOtherUserFeatures() {
        FeatureFlagDto otherUserFeature = service.createFeature(FEATURE_NAME);
        service.switchUserFeature(otherUserFeature.id(), true, USER_NAME);

        Set<FeatureFlagDto> allEnabledFeatures = service.getAllEnabledFeatures(true, OTHER_USER_NAME);

        assertThat(getIds(allEnabledFeatures)).doesNotContain(otherUserFeature.id());
    }

    @Test
    void shouldThrowFeatureFlagAlreadyExistsException() {
        service.createFeature(FEATURE_NAME);

        assertThatThrownBy(() -> service.createFeature(FEATURE_NAME))
                .isInstanceOf(FeatureFlagAlreadyExistsException.class);
    }

    @Test
    void shouldThrowForMissingGlobalFeature() {
        assertThatThrownBy(() -> service.switchGlobalFeature(MISSING_FEATURE_ID, true))
                .isInstanceOf(MissingFeatureFlagException.class);
    }

    @Test
    void shouldThrowForMissingGlobalFeatureByUser() {
        assertThatThrownBy(() -> service.switchUserFeature(MISSING_FEATURE_ID, true, USER_NAME))
                .isInstanceOf(MissingFeatureFlagException.class);
    }

    private static Set<Long> getIds(Set<FeatureFlagDto> features) {
        return features.stream().map(FeatureFlagDto::id).collect(Collectors.toSet());
    }

}
