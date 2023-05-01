package local.simas.cubeworld.engine.config;

import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WindowConfig {
    public Integer width;
    public Integer height;
    private String title;
    private Boolean shouldCenter;

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
