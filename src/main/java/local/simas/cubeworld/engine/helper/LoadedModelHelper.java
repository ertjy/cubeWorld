package local.simas.cubeworld.engine.helper;

import local.simas.cubeworld.engine.data.LoadedModel;

import java.util.HashMap;
import java.util.Map;

public class LoadedModelHelper {
    private static Map<Long, LoadedModel> loadedModelMap = new HashMap<>();

    public static void setLoadedModelForType(Long type, LoadedModel loadedModel) {
        if (loadedModelMap.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Loaded model overriding is not allowed, fix double-setting of type '%d'", type));
        }

        loadedModelMap.put(type, loadedModel);
    }

    public static LoadedModel getLoadedModelForType(Long type) {
        if (!loadedModelMap.containsKey(type)) {
            throw new IllegalArgumentException(String.format("Loaded model is not present in the map, set loaded model of type '%d'", type));
        }

        return loadedModelMap.get(type);
    }
}
