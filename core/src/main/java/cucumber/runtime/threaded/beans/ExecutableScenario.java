package cucumber.runtime.threaded.beans;

import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.threaded.PlaybackFormatter;

public class ExecutableScenario {

    private CucumberScenario scenario;
    private final PlaybackFormatter playbackFormatter = new PlaybackFormatter();
    private CucumberScenarioOutline optionalScenarioOutline;

    public CucumberScenario getScenario() {
        return scenario;
    }

    public void setScenario(CucumberScenario scenario) {
        this.scenario = scenario;
    }

    public PlaybackFormatter getPlaybackFormatter() {
        return playbackFormatter;
    }

    public CucumberScenarioOutline getOptionalScenarioOutline() {
        return optionalScenarioOutline;
    }

    public void setOptionalScenarioOutline(CucumberScenarioOutline optionalScenarioOutline) {
        this.optionalScenarioOutline = optionalScenarioOutline;
    }

    @Override
    public String toString() {
        return "ExecutableScenario { scenario=" + (scenario == null ? null : scenario.getVisualName()) + ", optionalScenarioOutline="
                + (optionalScenarioOutline == null ? null : optionalScenarioOutline.getVisualName()) + " }";
    }

}
