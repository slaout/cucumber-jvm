package cucumber.api.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ContinueNextStepsFor {
    /**
     * @return list of throwable classes that can be thrown by the stepdef to
     *         mark the step as failed but continue to execute the next steps
     *         of the current scenario anyway
     */
    Class<? extends Throwable>[] value();
}
