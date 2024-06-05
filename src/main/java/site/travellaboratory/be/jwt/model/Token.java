package site.travellaboratory.be.jwt.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    private @NotNull String token;

    private @NotNull LocalDateTime expiredAt;
}

