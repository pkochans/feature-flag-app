package biz.kochanski.featureflag.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class MissingFeatureFlagException extends IllegalArgumentException {
}
