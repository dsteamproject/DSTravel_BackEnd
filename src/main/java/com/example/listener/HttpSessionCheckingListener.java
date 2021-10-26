package com.example.listener;

import java.util.Date;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class HttpSessionCheckingListener implements HttpSessionListener {
    // private Logger logger = LoggerFactory.getLogger(this.getClass());

    static private int activeSessions = 0;

    public static int getActiveSessions() {
        return activeSessions;
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        activeSessions++;
        System.out.println("SessionCnt:" + activeSessions + " Session ID ".concat(event.getSession().getId())
                .concat(" created at ").concat(new Date().toString()));
        // logger.debug("SessionCnt:" + activeSessions + " Session ID
        // ".concat(event.getSession().getId()).concat(" created at ").concat(new
        // Date().toString()));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        activeSessions--;
        // logger.debug("SessionCnt:" + activeSessions + " Session ID
        // ".concat(event.getSession().getId()).concat(" destroyed at ").concat(new
        // Date().toString()));

    }
}
