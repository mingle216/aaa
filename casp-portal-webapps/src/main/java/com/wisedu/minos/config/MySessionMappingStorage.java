package com.wisedu.minos.config;

import org.jasig.cas.client.session.SessionMappingStorage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.SessionRepository;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

public class MySessionMappingStorage implements SessionMappingStorage {

    private StringRedisTemplate stringRedisTemplate;

    private final String PRE_FIX = "ST:";

    private SessionRepository sessionRepository;

    public MySessionMappingStorage(SessionRepository sessionRepository, StringRedisTemplate stringRedisTemplate) {
        this.sessionRepository = sessionRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public HttpSession removeSessionByMappingId(String st) {
        String sessionId = stringRedisTemplate.opsForValue().get(PRE_FIX + st);
        stringRedisTemplate.delete(PRE_FIX + st);
        sessionRepository.deleteById(sessionId);
        return null;
    }

    @Override
    public void removeBySessionById(String s) {

    }

    @Override
    public void addSessionById(String st, HttpSession httpSession) {
        stringRedisTemplate.opsForValue().set(PRE_FIX + st, httpSession.getId(), 8, TimeUnit.HOURS);
    }
}
