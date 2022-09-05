package com.nowcoder.community.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author byfgg
 * @create 2022-09-04 14:55
 */

public class CookieUtil {

    public static final String getValue(HttpServletRequest request, String ticketName) {
        if (request == null || ticketName == null) {
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(ticketName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
