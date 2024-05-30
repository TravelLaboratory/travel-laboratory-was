package site.travellaboratory.be.tmp.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.tmp.repository.TmpEntity;
import site.travellaboratory.be.tmp.repository.TmpRepository;

@RestController
@RequestMapping("/tmp")
@RequiredArgsConstructor
public class TmpController {

    private final TmpRepository tmpRepository;

    @GetMapping
    public BaseResponse initSave() {
        TmpEntity entity = TmpEntity.builder()
            .name("test")
            .build();

        // 테스트여서 service 제외
        TmpEntity savedEntity = tmpRepository.save(entity);

        return BaseResponse.fromDomain(savedEntity);
    }

    public record BaseResponse(@NotNull Long id, @NotNull String name) {
        public static BaseResponse fromDomain(@NotNull TmpEntity tmpEntity) {
            return new BaseResponse(
                tmpEntity.getId(), tmpEntity.getName()
            );
        }
    }
}
