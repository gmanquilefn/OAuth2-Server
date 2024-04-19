package cl.gfmn.authserver.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CronService {

    private static final Logger logger = LoggerFactory.getLogger(CronService.class);

    private final JdbcClient jdbcClient;

    @Scheduled(cron = "0 0 */1 * * *")
    public void cleanUpExpiredTokens() {
        int rows = jdbcClient.sql("DELETE FROM oauth2_authorization " +
                "WHERE (authorization_code_expires_at < :now) " +
                "OR (access_token_expires_at < :now) " +
                "OR (oidc_id_token_expires_at < :now) " +
                "OR (refresh_token_expires_at < :now) " +
                "OR (device_code_expires_at < :now) " +
                "OR (user_code_expires_at < :now)")
                .param("now", LocalDateTime.now())
                .update();

        logger.info("Clean expired tokens cron job, rows affected = {}", rows);
    }
}

