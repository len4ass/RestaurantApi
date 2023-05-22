package ru.len4ass.api.validators;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern REGEX_PATTERN = Pattern.compile(EMAIL_PATTERN);
    private static final String INVALID_CONSTRAINTS = """
            Your email doesn't match one (or more) of the following criteria:
            1) Maximum of 64 characters are allowed for the local part.
            2) Email must not contain consecutive dots.
            3) Email must not contain hyphens and dots at the start and the end of domain part.
            4) Email must not contain dots at the start and the end of local port.
            """;

    public static String getInvalidConstraints() {
        return INVALID_CONSTRAINTS;
    }

    public static boolean isValid(String email) {
        var matcher = REGEX_PATTERN.matcher(email);
        return matcher.matches();
    }
}
