package cucumber.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation provides the same options as the cucumber command line, {@link cucumber.api.cli.Main}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CucumberOptions {
    /**
     * @return true if this is a dry run
     */
    boolean dryRun() default false;

    /**
     * @return true if strict mode is enabled (fail if there are undefined or pending steps)
     */
    boolean strict() default false;

    /**
     * @return the paths to the feature(s)
     */
    String[] features() default {};

    /**
     * @return where to look for glue code (stepdefs and hooks)
     */
    String[] glue() default {};

    /**
     * @return what tags in the features should be executed
     */
    String[] tags() default {};

    /**
     * @return what formatter(s) to use
     * @deprecated use {@link #plugin()}
     */
    @Deprecated
    String[] format() default {};

    /**
     * @return what plugins(s) to use
     */
    String[] plugin() default {};

    /**
     * @return whether or not to use monochrome output
     */
    boolean monochrome() default false;

    /**
     * @return the number of parallel threads to run scenarios on (consider using tags starting with {@code @synchronized-} on scenarios to make
     *         sure a group of scenarios do not run concurrently)
     */
    int threads() default 1;

    /**
     * Specify a patternfilter for features or scenarios
     *
     * @return a list of patterns
     */
    String[] name() default {};

    /**
     * @return what format should the snippets use. underscore, camelcase
     */
    SnippetType snippets() default SnippetType.UNDERSCORE;
}
