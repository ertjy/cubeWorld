package local.simas.cubeworld.engine.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHelper {
    public static String getResourceAsString(String filename) throws IOException {
        ClassLoader classLoader = FileHelper.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new FileNotFoundException(String.format("File with the specified path was not found: %s", filename));
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }

            return stringBuilder.toString();
        }
    }
    public static InputStream getResourceAsStream(String filename) throws IOException {
        ClassLoader classLoader = FileHelper.class.getClassLoader();
        return classLoader.getResourceAsStream(filename);
    }
}
