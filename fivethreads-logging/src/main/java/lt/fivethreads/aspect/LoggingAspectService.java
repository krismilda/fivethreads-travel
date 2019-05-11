package lt.fivethreads.aspect;

import lt.fivethreads.entities.response.JwtResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspectService {

    @Around("execution(* lt.fivethreads.controller..*(..))")
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
        Object return_value = null;
        try {
            StringBuilder startMessageStringBuffer = new StringBuilder();
            startMessageStringBuffer.append("Start method: ");
            startMessageStringBuffer.append("\nUsername: ");
            startMessageStringBuffer.append(SecurityContextHolder.getContext().getAuthentication().getName());
            startMessageStringBuffer.append("\nRole: ");
            startMessageStringBuffer.append(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            startMessageStringBuffer.append("\nMethod: ");
            startMessageStringBuffer.append(joinPoint.getSignature().getName());
            startMessageStringBuffer.append("(");
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                startMessageStringBuffer.append(arg).append(",");
            }
            if (args.length > 0) {
                startMessageStringBuffer.deleteCharAt(startMessageStringBuffer.length() - 1);
            }
            startMessageStringBuffer.append(")");
            logger.debug(startMessageStringBuffer.toString());
            return_value = joinPoint.proceed();
            String endMessageStringBuffer = "Finish method: " +
                    joinPoint.getSignature().getName()
                    + "\nUsername: "
                    + SecurityContextHolder.getContext().getAuthentication().getName()
                    + "\nRole: "
                    + SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                    + "\nReturn value: "
                    + return_value;

            logger.debug(endMessageStringBuffer);

        } catch (Throwable ex)
        {
            logger.error("", ex);
            throw ex;
        }
        return return_value;
    }
}
