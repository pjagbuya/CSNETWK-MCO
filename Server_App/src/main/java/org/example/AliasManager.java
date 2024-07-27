package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AliasManager {
    private static final List<String> aliases = new CopyOnWriteArrayList<>(); // Thread-safe list

    public static void addAlias(String alias) {
        synchronized (aliases) {
            if (!aliases.contains(alias)) {
                aliases.add(alias);
            }
        }
    }
    public static void removeAlias(String alias) {
        synchronized (aliases) {
            aliases.remove(alias);
        }
    }

    public static void clearAliases() {
        synchronized (aliases) {
            aliases.clear();
        }
    }

    public static List<String> getAliases() {
        return new ArrayList<>(aliases); // Return a copy to prevent concurrent modification issues
    }
}
