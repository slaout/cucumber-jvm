package cucumber.runtime.threaded;

import java.util.List;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

public class ProxyFormatter implements Reporter, Formatter {

    private final Reporter reporter;
    private final Formatter formatter;

    public ProxyFormatter(Reporter reporter, Formatter formatter) {
        this.reporter = reporter;
        this.formatter = formatter;
    }

//    private static void writeLog(String logLine) {
//        System.out.println(logLine);
//    }

    // Formatter:

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
//        writeLog("Formatter.syntaxError(" + state + ", " + event + ", " + legalEvents + ", " + uri + ", " + line + ")");
        formatter.syntaxError(state, event, legalEvents, uri, line);
    }

    @Override
    public void uri(String uri) {
//        writeLog("Formatter.uri(" + uri + ")");
        formatter.uri(uri);
    }

    @Override
    public void feature(Feature feature) {
//        writeLog("Formatter.feature(" + feature + ")");
        formatter.feature(feature);
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {
//        writeLog("Formatter.scenarioOutline(" + scenarioOutline + ")");
        formatter.scenarioOutline(scenarioOutline);
    }

    @Override
    public void examples(Examples examples) {
//        writeLog("Formatter.examples(" + examples + ")");
        formatter.examples(examples);
    }

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
//        writeLog("Formatter.startOfScenarioLifeCycle(" + scenario + ")");
        formatter.startOfScenarioLifeCycle(scenario);
    }

    @Override
    public void background(Background background) {
//        writeLog("Formatter.background(" + background + ")");
        formatter.background(background);
    }

    @Override
    public void scenario(Scenario scenario) {
//        writeLog("Formatter.scenario(" + scenario + ")");
        formatter.scenario(scenario);
    }

    @Override
    public void step(Step step) {
//        writeLog("Formatter.step(" + step + ")");
        formatter.step(step);
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
//        writeLog("Formatter.endOfScenarioLifeCycle(" + scenario + ")");
        formatter.endOfScenarioLifeCycle(scenario);
    }

    @Override
    public void done() {
//        writeLog("Formatter.done()");
        formatter.done();
    }

    @Override
    public void close() {
//        writeLog("Formatter.close()");
        formatter.close();
    }

    @Override
    public void eof() {
//        writeLog("Formatter.eof()");
        formatter.eof();
    }

    // Reporter:

    @Override
    public void before(Match match, Result result) {
//        writeLog("Reporter.before(" + match + ", " + result + ")");
        reporter.before(match, result);
    }

    @Override
    public void result(Result result) {
//        writeLog("Reporter.result(" + result + ")");
        reporter.result(result);
    }

    @Override
    public void after(Match match, Result result) {
//        writeLog("Reporter.after(" + match + ", " + result + ")");
        reporter.after(match, result);
    }

    @Override
    public void match(Match match) {
//        writeLog("Reporter.match(" + match + ")");
        reporter.match(match);
    }

    @Override
    public void embedding(String mimeType, byte[] data) {
//        writeLog("Reporter.embedding(" + mimeType + ", " + data + ")");
        reporter.embedding(mimeType, data);
    }

    @Override
    public void write(String text) {
//        writeLog("Reporter.write(" + text + ")");
        reporter.write(text);
    }

}
