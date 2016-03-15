package cucumber.runtime.threaded.beans;

import java.util.ArrayList;
import java.util.List;

public class ExecutionEntry {

    private final List<ExecutableScenario> scenarios = new ArrayList<ExecutableScenario>();

    public List<ExecutableScenario> getScenarios() {
        return scenarios;
    }

    @Override
    public String toString() {
        return "ExecutionEntry { " + scenarios + " }";
    }

}
