package local.simas.cubeworld.engine.loader;

import local.simas.cubeworld.engine.helper.FileHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RawTextureLoader {
    public BufferedImage loadRawTextureFromFile(String fileName) throws IOException {
        return ImageIO.read(FileHelper.getResourceAsStream(fileName));
    }
}
