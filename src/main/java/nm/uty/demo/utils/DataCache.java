package nm.uty.demo.utils;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataCache {

    //        private Map<Integer, String> usersBotStates = new HashMap<>();
    private Set<String> indexes = new HashSet<>();

    public void setIndexes(String index) {
        indexes.add(index);
    }

    public Set<String> getIndexes() {
        return indexes;
    }

    public void delIndex(String index) {
        indexes.remove(index);
    }
}