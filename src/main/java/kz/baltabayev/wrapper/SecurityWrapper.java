package kz.baltabayev.wrapper;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record SecurityWrapper(String login,
                              String password) {
}
