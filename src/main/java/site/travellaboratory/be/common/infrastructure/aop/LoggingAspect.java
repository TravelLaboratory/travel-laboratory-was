package site.travellaboratory.be.common.infrastructure.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * S3Uploader의 업로드 메서드 실행 전후에 로깅을 수행.
     * 업로드가 성공하면 성공 로그, 실패하면 실패 로그를 기록합니다.
     */
    @Around("execution(* site.travellaboratory.be.common.infrastructure.aws.S3Uploader.uploadImageFile(..))")
    public Object logS3Upload(ProceedingJoinPoint joinPoint) throws Throwable {
        String fileName = (String) joinPoint.getArgs()[0];
        String folder = (String) joinPoint.getArgs()[2];



        log.info("Starting S3 upload for file: {} in folder: {}", fileName, folder);

        try {
            Object result = joinPoint.proceed(); // 실제 메서드 호출
            log.info("S3 upload successful for file: {}. File URL: {}", fileName, result);
            return result;
        } catch (Exception e) {
            // 에러 발생 시 스택 트레이스를 축소하여 로그
            log.error("S3 upload failed for file: {}. Error: {}", fileName, e.getMessage());
            throw e; // 예외는 재던져서 상위에서 처리하도록 함
        }
    }

    /**
     * S3Uploader의 메서드에서 예외가 발생했을 때 예외 정보를 로깅.
     */
    @AfterThrowing(pointcut = "execution(* site.travellaboratory.be.common.infrastructure.aws.S3Uploader.*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        String fileName = (String) joinPoint.getArgs()[0];
        log.error("Exception in S3Uploader method: {} for file: {}. Cause: {}",
            joinPoint.getSignature().getName(), fileName, ex.getMessage()); // 상세 스택 트레이스 생략
    }
}
