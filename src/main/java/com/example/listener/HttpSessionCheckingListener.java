// package com.example.listener;

// import javax.servlet.annotation.WebListener;

// import javax.servlet.http.HttpSession;
// import javax.servlet.http.HttpSessionEvent;
// import javax.servlet.http.HttpSessionListener;

// @WebListener
// public class HttpSessionCheckingListener implements HttpSessionListener {
// // private Logger logger = LoggerFactory.getLogger(this.getClass());

// static private int activeSessions = 0;

// public static int getActiveSessions() {
// return activeSessions;
// }

// @Override
// public void sessionCreated(HttpSessionEvent event) {
// HttpSession session = event.getSession();
// System.out.println("[MySessionListener] Session created:" + session);
// session.setAttribute("foo", "bar");
// // WebApplicationContext wac =
// //
// WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
// // HttpServletRequest req = ((SerevletRequestAttributes)
// // RequestContextHolder.currentRequestAttributes())
// // activeSessions++;
// // System.out.println("SessionCnt:" + activeSessions + " Session ID
// // ".concat(event.getSession().getId())
// // .concat(" created at ").concat(new Date().toString()));
// // logger.debug("SessionCnt:" + activeSessions + " Session ID
// // ".concat(event.getSession().getId()).concat(" created at ").concat(new
// // Date().toString()));
// }

// @Override
// public void sessionDestroyed(HttpSessionEvent event) {
// HttpSession session = event.getSession();
// System.out.println("[MySessionListener] Session invalidated:" + session);
// // activeSessions--;
// // logger.debug("SessionCnt:" + activeSessions + " Session ID
// // ".concat(event.getSession().getId()).concat(" destroyed at ").concat(new
// // Date().toString()));

// }
// }
