package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {ReleaseDateValidator.class}
)
public @interface ReleaseDate {
    String message() default "{Дата релиза — не раньше 28 декабря 1895 года}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
