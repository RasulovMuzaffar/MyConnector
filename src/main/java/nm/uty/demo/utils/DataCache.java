package nm.uty.demo.utils;

import nm.uty.demo.pojo.Train;
import nm.uty.demo.pojo.Wagon;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataCache {

    private Map<String, Train> indexesWagons = new HashMap<>();
    private final Set<String> indexes = new HashSet<>();
    private final Set<String> successSendIndexes = new HashSet<>();

    public void setIndexes(String index) {
        indexes.add(index);
    }

    public Set<String> getIndexes() {
        return indexes;
    }

    public void delIndex(String index) {
        indexes.remove(index);
    }

    public Map<String, Train> getIndexesWagons() {
        return indexesWagons;
    }

    public void setIndexesWagons(Map<String, Train> indexesWagons) {
        this.indexesWagons = indexesWagons;
    }

    public Set<String> getSuccessSendIndexes() {
        return successSendIndexes;
    }

    public void setSuccessSendIndexes(String index) {
        successSendIndexes.add(index);
    }
}