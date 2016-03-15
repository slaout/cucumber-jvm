package cucumber.runtime.threaded;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.model.CucumberTagStatement;
import cucumber.runtime.threaded.beans.ExecutableScenario;
import cucumber.runtime.threaded.beans.ExecutionEntry;
import cucumber.runtime.threaded.beans.ExecutionFile;
import cucumber.runtime.threaded.beans.ExecutionModel;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Tag;

public class ThreadedRuntime {

    private static final String TAG_SYNCHRONIZED = "@synchronized-";

    public static void run(final List<CucumberFeature> features, final Formatter formatter, final Reporter reporter, final Runtime runtime,
            int threadCount) {

        ExecutionModel model = buildModel(features);
        System.out.println("============");
        System.out.println(model);
        System.out.println("============");

        PlaybackFormatter playback = new PlaybackFormatter();

        if (threadCount > 1) {
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            // Add @synchronized-* first, as they are the least parallelizable
            for (Entry<String, ExecutionEntry> entry : model.getEntries().entrySet()) {
                if (entry.getKey().startsWith(TAG_SYNCHRONIZED)) {
                    System.out.println("Planning " + entry.getValue() + "...");
                    executor.execute(new ScenarioExecutionRunnable(runtime, entry.getValue()));
                }
            }
            // Then, the others, broken into pieces@synchronized-* first, as they are the least parallelizable
            ExecutionEntry nonSynchronizedEntry = model.getEntries().get("");
            if (nonSynchronizedEntry != null) {
                for (ExecutableScenario executableScenario : nonSynchronizedEntry.getScenarios()) {
                    ExecutionEntry executionEntry = new ExecutionEntry();
                    executionEntry.getScenarios().add(executableScenario);
                    System.out.println("Planning " + executionEntry + "...");
                    executor.execute(new ScenarioExecutionRunnable(runtime, executionEntry));
                }
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
            System.out.println("Finished all threads");

            // Merge all formatter outputs

            // Mimic CucumberFeature.run(Formatter, Reporter, Runtime)
            for (ExecutionFile executionFile : model.getFiles()) {
                playback.uri(executionFile.getFeature().getPath());
                playback.feature(executionFile.getFeature().getGherkinFeature());

                CucumberScenarioOutline lastOutline = null;
                for (ExecutableScenario executableScenario : executionFile.getScenarios()) {
                    if (executableScenario.getOptionalScenarioOutline() != lastOutline) {
                        lastOutline = executableScenario.getOptionalScenarioOutline();
                        if (lastOutline != null) {
                            lastOutline.formatOutlineScenario(playback);
                            for (CucumberExamples cucumberExamples : lastOutline.getCucumberExamplesList()) {
                                cucumberExamples.format(playback);
                            }
                        }
                    }

                    executableScenario.getPlaybackFormatter().playback(playback, playback);
                }

                playback.eof();
            }

        } else {
            for (CucumberFeature cucumberFeature : features) {
                cucumberFeature.run(playback, playback, runtime);
            }
        }

        ProxyFormatter proxy = new ProxyFormatter(reporter, formatter);
        playback.playback(proxy, proxy);
    }

    /**
     * @param features take all scenarios (grouped or not in scenario outlines) and add then to the model, both as an ordered list of scenarios
     *            per feature files, and as a list of queues to run scenarios in parallel threads
     * @return the model used to run all scenarios in parallel threads
     */
    private static ExecutionModel buildModel(List<CucumberFeature> features) {
        ExecutionModel model = new ExecutionModel();

        for (CucumberFeature feature : features) {
            ExecutionFile executionFile = new ExecutionFile();
            executionFile.setFeature(feature);
            model.getFiles().add(executionFile);

            for (CucumberTagStatement cucumberTagStatement : feature.getFeatureElements()) {
                if (cucumberTagStatement instanceof CucumberScenarioOutline) {
                    CucumberScenarioOutline outline = (CucumberScenarioOutline) cucumberTagStatement;
                    for (CucumberExamples examples : outline.getCucumberExamplesList()) {
                        for (CucumberScenario scenario : examples.createExampleScenarios()) {
                            appendScenarioToModel(model, executionFile, scenario, outline);
                        }
                    }
                } else if (cucumberTagStatement instanceof CucumberScenario) {
                    CucumberScenario scenario = (CucumberScenario) cucumberTagStatement;
                    appendScenarioToModel(model, executionFile, scenario, null);
                }
            }
        }

        return model;
    }

    /**
     * @param model add the scenario in an unordered queue of model.getEntries() based on its {@code @synchronized-*} tag
     * @param executionFile add the scenario to executionFile.getScenarios(), in the order it appear in the .feature file
     * @param scenario the scenario to add to the model
     * @param outline the optional (can be null) outline attached to the scenario
     */
    private static void appendScenarioToModel(ExecutionModel model, ExecutionFile executionFile, CucumberScenario scenario,
            CucumberScenarioOutline outline) {
        // Create an execution for this scenario
        ExecutableScenario executableScenario = new ExecutableScenario();
        executableScenario.setScenario(scenario);
        executableScenario.setOptionalScenarioOutline(outline);

        // Add the scenario in an unordered queue based on its @synchronized-* tag
        String tagName = getSynchronizedTag(scenario);
        ExecutionEntry entry = model.getEntries().get(tagName);
        if (entry == null) {
            entry = new ExecutionEntry();
            model.getEntries().put(tagName, entry);
        }
        entry.getScenarios().add(executableScenario);

        // Keep track of the order of scenarios in the feature file
        executionFile.getScenarios().add(executableScenario);
    }

    /**
     * @param scenario a Cucumber scenario
     * @return the tag name starting with "@synchronized-", or "" if no such tag is attached to the scenario
     * @throws UnsupportedOperationException if two or more such tags are found on the scenario
     */
    private static String getSynchronizedTag(CucumberScenario scenario) {
        String tagName = "";
        List<Tag> tags = scenario.getGherkinModel().getTags();
        if (tags != null) {
            for (Tag tag : tags) {
                if (tag.getName().startsWith(TAG_SYNCHRONIZED)) {
                    if (tagName.isEmpty()) {
                        tagName = tag.getName();
                    } else {
                        throw new UnsupportedOperationException("A scenario cannot have two @synchronized- tags: " + tagName + " & " + tag
                                .getName());
                    }
                }
            }
        }
        return tagName;
    }

}
