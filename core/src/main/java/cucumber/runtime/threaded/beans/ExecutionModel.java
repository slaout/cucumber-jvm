package cucumber.runtime.threaded.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionModel {

    private final List<ExecutionFile> files = new ArrayList<ExecutionFile>();
    private final Map<String, ExecutionEntry> entries = new HashMap<String, ExecutionEntry>();

    /**
     * @return the list of files, with execution entries in the order they appear on the file
     */
    public List<ExecutionFile> getFiles() {
        return files;
    }

    /**
     * @return executable entries, out of order, but sorted by {@code @synchronized-*} tags ("" for no such tag)
     */
    public Map<String, ExecutionEntry> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "ExecutionModel { files=" + files + ", entries=" + entries + " }";
    }

}
