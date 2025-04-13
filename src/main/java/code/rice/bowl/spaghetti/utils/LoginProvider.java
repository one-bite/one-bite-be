package code.rice.bowl.spaghetti.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginProvider {
    GOOGLE(0);

    private final int id;
}
