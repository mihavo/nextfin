package com.nextfin.account.service.security.session;

import com.nextfin.config.cache.RedisConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MultiSessionRepository implements SessionRepository<Session> {

    private final RedisIndexedSessionRepository redisRepository;

    private final MapSessionRepository mapSessionRepository;

    @Override
    public Session createSession() {
        try {
            if (RedisConfig.isCachingEnabled()) {
                return redisRepository.createSession();
            }
            return mapSessionRepository.createSession();
        } catch (Exception e) {
            return mapSessionRepository.createSession();
        }
    }

    @Override
    public void save(Session session) {
        try {
            if (Objects.requireNonNull(session) instanceof RedisIndexedSessionRepository.RedisSession redisSession) {
                redisRepository.save(redisSession);
                return;
            }
            mapSessionRepository.save((MapSession) session);
        } catch (Exception e) {
            //fallback: convert and save to map repository 
            mapSessionRepository.save(new MapSession(session));
        }
    }

    @Override
    public Session findById(String id) {
        Session byId;
        try {
            byId = redisRepository.findById(id);
        } catch (Exception e) {
            byId = mapSessionRepository.findById(id);
        }
        return byId;
    }

    @Override
    public void deleteById(String id) {
        try {
            redisRepository.deleteById(id);
        } catch (Exception e) {
            mapSessionRepository.deleteById(id);
        }
    }
}
