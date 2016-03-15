package cucumber.runtime.threaded.beans;

import java.util.ArrayList;
import java.util.List;

import cucumber.runtime.model.CucumberFeature;

public class ExecutionFile {

    private CucumberFeature feature;
    private final List<ExecutableScenario> scenarios = new ArrayList<ExecutableScenario>();

    public CucumberFeature getFeature() {
        return feature;
    }

    public void setFeature(CucumberFeature feature) {
        this.feature = feature;
    }

    public List<ExecutableScenario> getScenarios() {
        return scenarios;
    }

    @Override
    public String toString() {
        return "ExecutionFile { feature=" + (feature == null ? null : feature.getPath()) + ", scenarios=" + scenarios + " }";
    }

}
