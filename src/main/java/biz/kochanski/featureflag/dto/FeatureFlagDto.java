package biz.kochanski.featureflag.dto;

import biz.kochanski.featureflag.entity.GlobalFeatureFlag;
import biz.kochanski.featureflag.entity.UserFeatureFlag;

public record FeatureFlagDto(
        Long id,
        String name,
        Boolean enabled
) {
    public static FeatureFlagDto from(GlobalFeatureFlag flag) {
        return new FeatureFlagDto(flag.getGlobalFeatureId(), flag.getName(), flag.getEnabled());
    }

    public static FeatureFlagDto from(UserFeatureFlag flag) {
        return new FeatureFlagDto(flag.getGlobalFeatureFlag().getGlobalFeatureId(), flag.getGlobalFeatureFlag().getName(), flag.getEnabled());
    }
}
