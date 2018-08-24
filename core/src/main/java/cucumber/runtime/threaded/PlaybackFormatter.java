package cucumber.runtime.threaded;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

public class PlaybackFormatter implements Reporter, Formatter {

    private static final class EventRecord {
        private final Method method;
        private final Object[] arguments;

        public EventRecord(Method method, Object[] arguments) {
            this.method = method;
            this.arguments = arguments;
        }

        public Method getMethod() {
            return method;
        }

        public Object[] getArguments() {
            return arguments;
        }
    }

    private final List<EventRecord> events = new ArrayList<EventRecord>();

    public PlaybackFormatter() {
    }

    public void playback(Reporter reporter, Formatter formatter) {
        for (EventRecord event : events) {
            if (Reporter.class.isAssignableFrom(event.getMethod().getDeclaringClass())) {
                invoke(event.getMethod(), reporter, event.getArguments());
            } else if (Formatter.class.isAssignableFrom(event.getMethod().getDeclaringClass())) {
                invoke(event.getMethod(), formatter, event.getArguments());
            }
        }
        events.clear();
    }

    private static void invoke(Method method, Object object, Object[] arguments) {
        try {
            method.invoke(object, arguments);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    private static void writeLog(String logLine) {
//        System.out.println("[PlaybackFormatter] " + logLine);
//    }

    private void addEventRecord(Class<?> iface, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        try {
            events.add(new EventRecord(iface.getMethod(methodName, parameterTypes), arguments));
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Formatter:

    @Override
    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
//        writeLog("Formatter.syntaxError(" + state + ", " + event + ", " + legalEvents + ", " + uri + ", " + line + ")");
        addEventRecord(Formatter.class, "syntaxError", //
                new Class[] {String.class, String.class, List.class, String.class, Integer.class }, //
                new Object[] {state, event, legalEvents, uri, line });
    }

    @Override
    public void uri(String uri) {
//        writeLog("Formatter.uri(" + uri + ")");
        addEventRecord(Formatter.class, "uri", //
                new Class[] {String.class }, //
                new Object[] {uri });
    }

    @Override
    public void feature(Feature feature) {
//        writeLog("Formatter.feature(" + feature + ")");
        addEventRecord(Formatter.class, "feature", //
                new Class[] {Feature.class }, //
                new Object[] {feature });
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {
//        writeLog("Formatter.scenarioOutline(" + scenarioOutline + ")");
        addEventRecord(Formatter.class, "scenarioOutline", //
                new Class[] {ScenarioOutline.class }, //
                new Object[] {scenarioOutline });
    }

    @Override
    public void examples(Examples examples) {
//        writeLog("Formatter.examples(" + examples + ")");
        addEventRecord(Formatter.class, "examples", //
                new Class[] {Examples.class }, //
                new Object[] {examples });
    }

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {
//        writeLog("Formatter.startOfScenarioLifeCycle(" + scenario + ")");
        addEventRecord(Formatter.class, "startOfScenarioLifeCycle", //
                new Class[] {Scenario.class }, //
                new Object[] {scenario });
    }

    @Override
    public void background(Background background) {
//        writeLog("Formatter.background(" + background + ")");
        addEventRecord(Formatter.class, "background", //
                new Class[] {Background.class }, //
                new Object[] {background });
    }

    @Override
    public void scenario(Scenario scenario) {
//        writeLog("Formatter.scenario(" + scenario + ")");
        addEventRecord(Formatter.class, "scenario", //
                new Class[] {Scenario.class }, //
                new Object[] {scenario });
    }

    @Override
    public void step(Step step) {
//        writeLog("Formatter.step(" + step + ")");
        addEventRecord(Formatter.class, "step", //
                new Class[] {Step.class }, //
                new Object[] {step });
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
//        writeLog("Formatter.endOfScenarioLifeCycle(" + scenario + ")");
        addEventRecord(Formatter.class, "endOfScenarioLifeCycle", //
                new Class[] {Scenario.class }, //
                new Object[] {scenario });
    }

    @Override
    public void done() {
//        writeLog("Formatter.done()");
        addEventRecord(Formatter.class, "done", //
                new Class[] {}, //
                new Object[] {});
    }

    @Override
    public void close() {
//        writeLog("Formatter.close()");
        addEventRecord(Formatter.class, "close", //
                new Class[] {}, //
                new Object[] {});
    }

    @Override
    public void eof() {
//        writeLog("Formatter.eof()");
        addEventRecord(Formatter.class, "eof", //
                new Class[] {}, //
                new Object[] {});
    }

    // Reporter:

    @Override
    public void before(Match match, Result result) {
//        writeLog("Reporter.before(" + match + ", " + result + ")");
        addEventRecord(Reporter.class, "before", //
                new Class[] {Match.class, Result.class }, //
                new Object[] {match, result });
    }

    @Override
    public void result(Result result) {
//        writeLog("Reporter.result(" + result + ")");
        addEventRecord(Reporter.class, "result", //
                new Class[] {Result.class }, //
                new Object[] {result });
    }

    @Override
    public void after(Match match, Result result) {
//        writeLog("Reporter.after(" + match + ", " + result + ")");
        addEventRecord(Reporter.class, "after", //
                new Class[] {Match.class, Result.class }, //
                new Object[] {match, result });
    }

    @Override
    public void match(Match match) {
//        writeLog("Reporter.match(" + match + ")");
        addEventRecord(Reporter.class, "match", //
                new Class[] {Match.class }, //
                new Object[] {match });
    }

    @Override
    public void embedding(String mimeType, byte[] data) {
//        writeLog("Reporter.embedding(" + mimeType + ", " + data + ")");
        addEventRecord(Reporter.class, "embedding", //
                new Class[] {String.class, byte[].class }, //
                new Object[] {mimeType, data });
    }

    @Override
    public void write(String text) {
//        writeLog("Reporter.write(" + text + ")");
        addEventRecord(Reporter.class, "write", //
                new Class[] {String.class }, //
                new Object[] {text });
    }

}
