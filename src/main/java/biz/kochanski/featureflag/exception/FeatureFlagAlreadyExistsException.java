package biz.kochanski.featureflag.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FeatureFlagAlreadyExistsException extends RuntimeException {
    public FeatureFlagAlreadyExistsException(String name) {
        super("Feature with name " + name + " already exists");
    }
}
