package local.simas.engine.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WindowConfig {
    private Integer width;
    private Integer height;
    private String title;
    private Boolean shouldCenter;
}
