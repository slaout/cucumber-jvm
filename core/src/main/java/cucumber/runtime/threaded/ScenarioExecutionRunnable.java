package cucumber.runtime.threaded;

import cucumber.runtime.Runtime;
import cucumber.runtime.threaded.beans.ExecutableScenario;
import cucumber.runtime.threaded.beans.ExecutionEntry;

public class ScenarioExecutionRunnable implements Runnable {

    private final Runtime runtime;
    private final ExecutionEntry executionEntry;

    public ScenarioExecutionRunnable(Runtime runtime, ExecutionEntry executionEntry) {
        this.runtime = runtime;
        this.executionEntry = executionEntry;
    }

    @Override
    public void run() {
//        System.out.println(" ** " + Thread.currentThread().getName() + ": start");
        // Run the scenarios that must be run in an anomic manner
        for (ExecutableScenario executableScenario : executionEntry.getScenarios()) {
            PlaybackFormatter playback = executableScenario.getPlaybackFormatter();
            executableScenario.getScenario().run(playback, playback, runtime);
        }
//        System.out.println(" ** " + Thread.currentThread().getName() + ": stop");
    }

}
