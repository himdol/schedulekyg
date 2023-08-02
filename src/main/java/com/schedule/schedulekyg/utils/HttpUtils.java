package com.schedule.schedulekyg.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
    private HttpUtils() {
    }

    public static String path(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getServletPath());
        String pp = request.getPathInfo();
        if (StringUtils.isNotBlank(pp)) {
            sb.append(pp);
        }
        return sb.toString();
    }

    public static String currentUrl(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if (request.isSecure()) {
            sb.append("https://");
        } else {
            sb.append("http://");
        }
        sb.append(request.getServerName());
        int port = request.getServerPort();
        if (80 != port && 443 != port) {
            sb.append(":").append(port);
        }
        sb.append(path(request));
        return sb.toString();
    }

    public static String ip(HttpServletRequest request) {
        String ip = null;
        ip = request.getHeader("X-FORWARDED-FOR");

        if (StringUtils.isBlank(ip)) {
            ip = request.getHeader("HTTP-X-FORWARDED-FOR");
        }

        if (StringUtils.isBlank(ip)) {
            ip = request.getHeader("REMOTE-ADDR");
        }

        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr(); 
        }

        return ip;
    }

    public static boolean isIE(String userAgent) {
        return StringUtils.contains(userAgent, "MSIE") ||
                StringUtils.contains(userAgent, "Trident/");
    }
    
    public static boolean isWebBrowser(String userAgent) {
        return StringUtils.contains(userAgent.toLowerCase(), "win16") || StringUtils.contains(userAgent.toLowerCase(), "win32") ||
        		StringUtils.contains(userAgent.toLowerCase(), "win64") || StringUtils.contains(userAgent.toLowerCase(), "mac");
    }

    public static boolean isMobileBrowser(String userAgent) {
        return StringUtils.contains(userAgent, "AppleWebKit") &&
                (StringUtils.contains(userAgent, "iPhone;") ||
                        StringUtils.contains(userAgent, "iPad;") ||
                        StringUtils.contains(userAgent, "Android") ||StringUtils.contains(userAgent, "iPod") ||
                        StringUtils.contains(userAgent, "BlackBerry") ||StringUtils.contains(userAgent, "LG") ||
                        StringUtils.contains(userAgent, "SAMSUNG") ||StringUtils.contains(userAgent, "SonyEricsson") ||
                        StringUtils.contains(userAgent, "MOT") ||StringUtils.contains(userAgent, "Windows CE")
                        );
    }

    /*
     * TODO: 안드로이드 tablet 제외 추가 필요
     */
    public static boolean isSmartPhone(String userAgent) {
        return StringUtils.contains(userAgent, "AppleWebKit") &&
                (StringUtils.contains(userAgent, "iPhone;") ||
                        StringUtils.contains(userAgent, "Android")) &&
                ! StringUtils.contains(userAgent, "iPad;");
    }
    
    public static String jspCallCurrentUrl(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if (request.isSecure()) {
            sb.append("https://");
        } else {
            sb.append("http://");
        }
        sb.append(request.getServerName());
        int port = request.getServerPort();
        if (80 != port && 443 != port) {
            sb.append(":").append(port);
        }
        sb.append(jspCallPath(request));
        return sb.toString();
    }
    
    public static String jspCallPath(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getAttribute("javax.servlet.forward.servlet_path").toString());
        String pp = null;
        if(request.getAttribute("javax.servlet.forward.path_info") != null) {
        	pp = (String)request.getAttribute("javax.servlet.forward.path_info");
        }
        if (StringUtils.isNotBlank(pp)) {
            sb.append(pp);
        }
        return sb.toString();
    }
    
    public static boolean isIPhone(String userAgent) {
        return StringUtils.contains(userAgent, "AppleWebKit") &&
                (StringUtils.contains(userAgent, "iPhone;") ||
                        StringUtils.contains(userAgent, "iPad;") ||
                        StringUtils.contains(userAgent, "iPod;") 
                        );
    }
    
    /**
     * 앱 접속 체크
     */
    public static boolean isApp(String userAgent) {
    	boolean rst = false;
    	//OSULLOC_IOS, OSULLOC_ANDROID
    	if(userAgent != null) {
    		if(StringUtils.contains(userAgent.toUpperCase(), "OSULLOC_IOS") || StringUtils.contains(userAgent.toUpperCase(), "OSULLOC_ANDROID")) {
    			rst = true;
    		}
    	}
    	return rst;
    }
    
    /**
     * OS 타입별 앱 접속 체크
     */
    public static boolean isAppOsType(String userAgent, String osType) {
    	boolean rst = false;
    	//OSULLOC_IOS, OSULLOC_ANDROID
    	if(userAgent != null) {
    		if (StringUtils.isNotBlank(osType)) {
    			System.out.println("userAgent: "+userAgent);
    			if("ios".equals(osType)) {
    				if(StringUtils.contains(userAgent.toUpperCase(), "OSULLOC_IOS")) {
    					rst = true;
    				}
    			}else if("android".equals(osType)) {
    				if(StringUtils.contains(userAgent.toUpperCase(), "OSULLOC_ANDROID")) {
    					rst = true;
    				}
    			}
    		}
    	}
    	return rst;
    }
    
}
