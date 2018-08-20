package cucumber.runtime.java.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

class GlueCodeContext {
    public static final GlueCodeContext INSTANCE = new GlueCodeContext();
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final ThreadLocal<Map<String, Object>> objects = ThreadLocal.withInitial(new Supplier<Map<String, Object>>() {
        public Map<String, Object> get() {
            return new HashMap<String, Object>();
        }
    });
    private final ThreadLocal<Map<String, Runnable>> callbacks = ThreadLocal.withInitial(new Supplier<Map<String, Runnable>>() {
        public Map<String, Runnable> get() {
            return new HashMap<String, Runnable>();
        }
    });

    private GlueCodeContext() {
    }

    public void start() {
        cleanUp();
        COUNTER.incrementAndGet();
    }

    public String getId() {
        return "cucumber_glue_" + COUNTER.get();
    }

    public void stop() {
        for (Runnable callback : callbacks.get().values()) {
            callback.run();
        }
        cleanUp();
    }

    public Object get(String name) {
        return objects.get().get(name);
    }

    public void put(String name, Object object) {
        objects.get().put(name, object);
    }

    public Object remove(String name) {
        callbacks.get().remove(name);
        return objects.get().remove(name);
    }

    private void cleanUp() {
        objects.get().clear();
        callbacks.get().clear();
    }

    public void registerDestructionCallback(String name, Runnable callback) {
        callbacks.get().put(name, callback);
    }
}
