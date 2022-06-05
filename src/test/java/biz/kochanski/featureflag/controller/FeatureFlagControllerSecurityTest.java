package biz.kochanski.featureflag.controller;

import biz.kochanski.featureflag.configuration.BasicAuthConfiguration;
import biz.kochanski.featureflag.service.FeatureFlagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FeatureFlagController.class)
@Import(BasicAuthConfiguration.class)
class FeatureFlagControllerSecurityTest {
    private static final String CREATE_FEATURE_FLAG_URL = "/v1/features?name=my-feature";
    private static final String ENABLE_USER_FEATURE_FLAG_URL = "/v1/features/1/user/user?enabled=true";
    private static final String ENABLE_GLOBAL_FEATURE_FLAG_URL = "/v1/features/2?enabled=true";
    private static final String GET_FEATURE_FLAGS_URL = "/v1/features?enabled=true";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private FeatureFlagController controller;
    @MockBean
    private FeatureFlagService service;

    @Test
    @WithUserDetails(value = "admin")
    void shouldAllowAdminToCreateFeatureFlag() throws Exception {
        mvc.perform(post(CREATE_FEATURE_FLAG_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user")
    void shouldNotAllowUserToCreateFeatureFlag() throws Exception {
        mvc.perform(post(CREATE_FEATURE_FLAG_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin")
    void shouldAllowAdminToEnableUserFeatureFlag() throws Exception {
        mvc.perform(put(ENABLE_USER_FEATURE_FLAG_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user")
    void shouldNotAllowUserToEnableUserFeatureFlag() throws Exception {
        mvc.perform(put(ENABLE_USER_FEATURE_FLAG_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin")
    void shouldAllowAdminToEnableGlobalFeatureFlag() throws Exception {
        mvc.perform(put(ENABLE_GLOBAL_FEATURE_FLAG_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user")
    void shouldNotAllowUserToEnableGlobalFeatureFlag() throws Exception {
        mvc.perform(put(ENABLE_GLOBAL_FEATURE_FLAG_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user")
    void shouldAllowUserToGetFeatureFlags() throws Exception {
        mvc.perform(get(GET_FEATURE_FLAGS_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "admin")
    void shouldAllowAdminToGetFeatureFlags() throws Exception {
        mvc.perform(get(GET_FEATURE_FLAGS_URL))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "nobody")
    void shouldNotAllowNobodyToGetFeatureFlags() throws Exception {
        mvc.perform(get(GET_FEATURE_FLAGS_URL))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource
    @WithAnonymousUser
    void shouldNotAllowAnonymousToAnyEndpoint(HttpMethod method, String url) throws Exception {
        mvc.perform(request(method, url))
                .andExpect(status().isUnauthorized());
    }

    private static List<Arguments> shouldNotAllowAnonymousToAnyEndpoint() {
        return List.of(
                Arguments.of(POST, CREATE_FEATURE_FLAG_URL),
                Arguments.of(PUT, ENABLE_USER_FEATURE_FLAG_URL),
                Arguments.of(PUT, ENABLE_GLOBAL_FEATURE_FLAG_URL),
                Arguments.of(GET, GET_FEATURE_FLAGS_URL)
        );
    }

}
