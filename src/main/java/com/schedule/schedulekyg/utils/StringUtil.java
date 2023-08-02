package com.schedule.schedulekyg.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

	static final Logger log = LoggerFactory.getLogger(StringUtil.class);

    public static String join(String separator, String... items) {
        StringBuffer sb = new StringBuffer();
        if (ArrayUtils.isNotEmpty(items)) {
            for (String i : items) {
                sb.append(separator);
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public static String getUrlKey(String title) {
        String reg = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        String spaces = "\\s{2,}";
        String urlKey = title.replaceAll(reg, "");
        urlKey = urlKey.trim();
        urlKey = urlKey.replaceAll(spaces, " ");
        urlKey = urlKey.replaceAll("\\p{Z}", "-");

        return urlKey;
    }

    public static String nextUrlKey(String nKey, String key) {
        if (StringUtils.equals(nKey, key)) {
            int suffixNumber = 1;
            int separatorIndex = key.lastIndexOf("_");
            if (separatorIndex > -1) {
                try {
                    suffixNumber = Integer.parseInt(key.substring(separatorIndex + 1));
                    suffixNumber++;
                } catch (Exception e) {
                }
            }
            return nKey.concat("_").concat(Integer.toString(suffixNumber));

        } else {
            return nKey;
        }

    }

    // 브라우저 확인
    public static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.contains("Firefox")) {
            return "Firefox";
        } else if (header.contains("Chrome")) {
            return "Chrome";
        } else if (header.contains("Opera")) {
            return "Opera";
        } else if (header.contains("Safari")) {
            return "Safari";
        } else {
            return "MSIE";
        }
    }

    // d : date, p : pattern
    public static String dateFormat(Date d, String p) {
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        return sdf.format(d);
    }

    public static String dateFormat(String p) {
        return dateFormat(new Date(), p);
    }

    // date string, format
    public static Date parse(String d, String f) {
        SimpleDateFormat sdf = new SimpleDateFormat(f);
        try {
            return sdf.parse(d);
        } catch (ParseException e) {
        }
        return null;
    }

    private static final String DEFAULT_INPUT_DATE_FORMAT = "yyyy-MM-dd";

    public static Date parse(String d) {
        return parse(d, DEFAULT_INPUT_DATE_FORMAT);
    }


    public static <T, U> Map<T, U> putDefaultValues(Map<T, U> target, T[] keys, U defaultValue) {
        for (T key : keys) {
            if (!target.containsKey(key)) {
                target.put(key, defaultValue);
            }
        }
        return target;
    }

    /*
     * 2016.03.01 00:00:00 시점의 timestamp. generateSequence는 이 시점 이후로부터
     * timestamp를 이용하여 sequence를 생성 이유는 sequence 값이 너무 커지는 것을 방지하기 위해서 1년의 총
     * second 수가 대략 3천만(31,536,000)이므로 대략 앞으로 70년 정도 사용 가능 :)
     */
    private static final long STARTING_TIMESTAMP;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2016, 2, 1, 0, 0, 0);
        STARTING_TIMESTAMP = cal.getTime().getTime();
    }

    public static int generateSequence() {
        long time = System.currentTimeMillis() - STARTING_TIMESTAMP;
        int ret = (int) (time / 1000);
        return ret;
    }

    public static int ceil(int dividend, int divisor) {
        if (dividend % divisor > 0) {
            return dividend / divisor + 1;
        }
        return dividend / divisor;
    }

    public static int round(int dividend, int divisor) {
        return (dividend * 10 / divisor + 5) / 10;
    }

    public static int round(long dividend, long divisor) {
        return (int) ((dividend * 10 / divisor + 5) / 10);
    }

    private static int secsPerMin = 60;
    private static int minsPerHour = 60;
    private static int hoursPerDay = 24;
    private static int milli = 1000;

    public static int dday(Date d) {
        long date = d.getTime();
        long today = (new Date()).getTime();

        long diffMilliSeconds = date - today;

        long diffHours = diffMilliSeconds / (minsPerHour * secsPerMin * milli);
        long diffDays = diffHours / hoursPerDay;
        if ( diffDays == 0 ){
            return 0 ;
        } else {
            int mod = (int) diffHours % hoursPerDay;
            if (mod > 0) {
                diffDays = diffDays + 1;
            }
//        diffDays = diffDays + 1; // 종료일은 하루 더해줘야
            return (int) diffDays;
        }
    }

    public static String joinWith(String deli, Object[] arr, String prefix, String postfix) {
        int idx = 0;
        StringBuilder sb = new StringBuilder();
        for (Object o : arr) {
            if (null == o) {
                continue;
            }
            if (idx > 0) {
                sb.append(deli);
            }
            if (StringUtils.isNotEmpty(prefix)) {
                sb.append(prefix);
            }
            sb.append(o);
            if (StringUtils.isNotEmpty(postfix)) {
                sb.append(postfix);
            }
            idx++;
        }
        return sb.toString();
    }

    public static String cleanHtml(String html) {
        return StringUtils.isEmpty(html) ? "" : html.replaceAll("\\<[^>]*>", "");
    }

    public static String fixMobileNo(String src) {
        if (StringUtils.isNotBlank(src)) {
            if (StringUtils.startsWith(src, "01")) {
                int len = src.length();
                if (10 == len) {
                    return StringUtils.join(new String[] { src.substring(0, 3), src.substring(3, 6), src.substring(6) }, "-");
                } else if (11 == len) {
                    return StringUtils.join(new String[] { src.substring(0, 3), src.substring(3, 7), src.substring(7) }, "-");
                }
            }
        }
        return src;
    }

    public static boolean equalsMobileNo(String m1, String m2) {
        String mm1 = StringUtils.remove(m1, "-");
        String mm2 = StringUtils.remove(m2, "-");
        return StringUtils.equals(mm1, mm2);
    }

    /**
     * 안전하게 정수를 파싱. 에러가 나면 def 값을 반환
     */
    public static int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 공백 문자 제거
     */
    public static String stripSpaces(String src) {
        if (StringUtils.isEmpty(src)) {
            return "";
        }
        return StringUtils.remove(src, ' ');
    }


    /**
     * sting ellipsis
     */
    public static String ellipsis(String text, int length){
        String ellipsisString = "...";
        String outputString = text;

        if(text.length()>0 && length>0){
            if(text.length()>length){
                outputString = text.substring(0, length);
                outputString += ellipsisString;
            }
        }
        return outputString;
    }

    /**
     * 전화번호 masking(국번 부분만 '*'로 변경)
     */
    public static String maskedPhoneNo(String src) {
        String phoneNo = fixMobileNo(src);
        if (StringUtils.isNoneBlank(phoneNo)) {
            String phoneNoSection[] = phoneNo.split("-");
            if(phoneNoSection.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < phoneNoSection.length; i++) {
                    if (i == 1) {
                        for(int j = 0; j < phoneNoSection[i].length(); j++) {
                            sb.append("*");
                        }
                    } else {
                        sb.append(phoneNoSection[i]);
                    }

                    if (phoneNoSection.length != i + 1) {
                        sb.append("-");
                    }
                }
                String maskedPhoneNo = sb.toString();
                return maskedPhoneNo;
            } else {
                return phoneNo;
            }
        }else {
            return phoneNo;
        }
    }

    /**
     * 이름 masking(두번째 자리 '*'로 변경)
     */
    public static String maskedName(String name) {
        String maskedName = "";
        if (name.length() >= 2) {
            StringBuilder sb = new StringBuilder();
            sb.append(name.substring(0, 1));
            sb.append("*");
            sb.append(name.substring(2));
            maskedName = sb.toString();
        } else {
            maskedName = name.concat("*");
        }
        return maskedName;
    }
    /**
     * cstmid 마스킹(네번째 자리 '*'로 변경)
     * @return
     */
    public static String maskedId(String cstmid) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < cstmid.length(); i++) {
            if(i <= 2) {
                sb.append(cstmid.charAt(i));
            } else {
                sb.append("*");
            }
        }
        return sb.toString();
    }


    public static String format2(String d) {
    	String result = "";
    	if(StringUtils.isNotEmpty(d)){
	    	if(d.length() == 8){
	    		result = d.substring(0,4)+"-"+d.substring(4,6)+"-"+d.substring(6,8);
	    	}else{
	    		result = d;
	    	}
    	}
    	return result;
    }

    public static String cutByLength(String str,int max) {
    	String result = "";
    	int length = str.length();
    	if(length<max){
    		return str;
    	}else{
    		result = str.substring(0, max);
    		return result;
    	}
    }

    public static String URLRequestCall(String requrl, String reqType, String param){
    	StringBuilder out = new StringBuilder();
    	try{

    		URL url = new URL(requrl);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setInstanceFollowRedirects(false);
			log.debug("52Line :::: requrl::"+requrl);
			if (conn == null) {
				log.debug("53Line :::: Connection Null");
				return "";
			}else{
				conn.setConnectTimeout(10000);
				conn.setRequestMethod(reqType);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				if (!"GET".equals(reqType)){
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					OutputStream os = conn.getOutputStream();
			        os.write(param.getBytes());
				}
				int resCode = conn.getResponseCode();
				log.debug(HttpURLConnection.HTTP_OK + " == > 62Line :::: " + resCode);
				if (resCode < HttpURLConnection.HTTP_BAD_REQUEST) {
					BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					String line = null;
					while(true){
						line = read.readLine();
						if (line == null) break;

						out.append(line + "\n");
					}

					log.debug(" == > 63Line :::: " + out.toString());
					read.close();
					conn.disconnect();
				} else {
				     /* error from server */
					BufferedReader read = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

					String line = null;
					while(true){
						line = read.readLine();
						if (line == null) break;

						out.append(line + "\n");
					}
					log.debug(" == > 64Line :::: " + out.toString());
					read.close();
					conn.disconnect();
				}
			}
    	}catch(Exception e){
    		log.debug("에러:"+e.getMessage());
    		//e.getStackTrace();
    	}
    	return out.toString();
    }

    public static String URLRequestCallPOST(String requrl , String[] reqParam , Map<Object , Object> param){
    	StringBuilder out = new StringBuilder();
    	try{

    		URL url = new URL(requrl);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setInstanceFollowRedirects(false);

			if (conn == null) {
				System.out.println("53Line :::: Connection Null");
				return "";
			}else{
				conn.setConnectTimeout(10000);
				conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);

				int resCode = conn.getResponseCode();

				System.out.println(HttpURLConnection.HTTP_OK + " == > 62Line :::: " + resCode);

				if (resCode == HttpURLConnection.HTTP_OK){
					BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					String line = null;

					while(true){
						line = read.readLine();
						if (line == null) break;

						out.append(line + "\n");
					}

					read.close();
					conn.disconnect();
				}else{
					return Integer.toString(resCode);
				}
			}
    	}catch(Exception e){
    		//e.getStackTrace();
    	}
    	return out.toString();
    }


	/**
	 * 현금 합산 dctype 다건으로 변경
	 * String  > String array
	 * 확인 메소스
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static int useArraysBinarySearch(String[] arr, String targetValue) {
		String sortArr[] = arr;
		Arrays.sort(sortArr);
		int a = Arrays.binarySearch(sortArr, targetValue);

		return a;

	}
	    /**
	 * ex) 서울 서초구 서초2동 138-7 -> 서울 서초구 **동
	 *
	 * @param str
	 * @return
	 */
	public static String maskingAddr(String str) {
		String rtn = "";
		if (str == null) {
			rtn = str;
		} else {
			String temp = str.trim();
			String regex = " ";
			String[] arr = temp.split(regex);
			String[] match = new String[] { "동", "리", "로", "길", "가" }; // 마스킹 대상주소 부분
			boolean bMatch = false;
			String masking = "";
			int dongIdx = -1;
			int tempIdx = 0;

			for (int i = 0; i < arr.length; i++) {
				if (bMatch == false) {
					for (int j = 0; j < match.length; j++) {
						tempIdx = arr[i].lastIndexOf(match[j]);

						// 마지막 글자가 동,리,로,길,가 로 끝나는지 확인
						if (tempIdx > -1 && tempIdx == arr[i].length() - 1 && bMatch == false) {
							for (int k = 0; k < arr[i].length(); k++) {
								if (k == tempIdx) {
									masking += arr[i].substring(k, k + 1);
								} else {
									masking += "*";
								}
							}
							bMatch = true;
							dongIdx = i;
						}
					}
				}

				if (i > 0) {
					rtn += regex;
				}

				if (dongIdx == -1) {
					rtn += arr[i];
				} else if (dongIdx == i) {
					rtn += masking;
				}
			}
		}
		return rtn;
	}

	public static String filter(String value) {
		if (value == null) {
			return "";
		}
		StringBuffer result = new StringBuffer(value.length());
		for (int i=0; i<value.length(); ++i) {
			switch (value.charAt(i)) {
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '"':
					result.append("&quot;");
					break;
				case '\'':
					result.append("&#39;");
					break;
				case '%':
					result.append("&#37;");
					break;
				case ';':
					result.append("&#59;");
					break;
				case '(':
					result.append("&#40;");
					break;
				case ')':
					result.append("&#41;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '+':
					result.append("&#43;");
					break;
				default:
					result.append(value.charAt(i));
					break;
			}
		}
		return result.toString();
	}

	public static String nvl(String str, String defaultStr) {
        return str == null ? defaultStr : str;
	}

	/**
	 * 콤마 추가 20190422 리뉴얼
	 * @param number
	 * @return
	 */
	public static String addComma(Integer number) {
		if(number == null) number = 0;
		return new DecimalFormat("#,###").format(number);
	}

	/**
	 * 콤마 추가 20190422 리뉴얼
	 * @param number
	 * @return
	 */
	public static String addComma(Long number) {
		if(number == null) number = 0L;
		return new DecimalFormat("#,###").format(number);
	}

	/**
	 * 첫글짜만 대문자
	 * @param str
	 * @return String
	 */
	public static String capFirst(String str) {
    	String result = "";
    	if(str != null && str.length() > 0) {
    		result = str.substring(0, 1).toUpperCase()+str.substring(1);
    	}
    	return result;

    }


	/**
	 * 문자열이 null인경우 대체 문자를 변환한다.
	 * @param object
	 * @return str(변환 문자열)
	 */
	public static String nullToStr(Object object) {
		return nullToStr(object, "");
	}

	/**
	 * 문자열이 null인경우 대체 문자를 변환한다.
	 * @param object
	 * @return str(변환 문자열)
	 */
	public static String nullToStr(Object object, String convertStr) {
		String tempStr = convertStr == null ? "" : convertStr;
		String str = String.valueOf(object);

		if(str == null || str == "null" || str.length() == 0) {
			return tempStr;
		}

		return str;
	}

	public static int toInt(Object object) {
		return Integer.valueOf(nullToStr(object, "0"));
	}


	public static String sendRESTApi(String sendUrl, String sendType, String sendParam) {
		StringBuilder sb = new StringBuilder();

		try {
			URL url;
			if ("GET".equals(sendType)) {
				url = new URL(sendUrl + "?"+sendParam);
			}else {
				url = new URL(sendUrl);
			}

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			conn.setRequestMethod(sendType);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);

			if ("POST".equals(sendType)){
				OutputStream os = conn.getOutputStream();
		        os.write(sendParam.getBytes());
			}


			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

				BufferedReader br = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				conn.disconnect();
				log.debug(":::::::::::::::::::: sendRESTApi : " + sb.toString());

			}else {
				conn.disconnect();
			}
		} catch (Exception e) {
			log.debug("에러:"+e.getMessage());
		}

		return sb.toString();
	}


	public static String replaceString(String str)	{
		String returnValue = "";
		if(  str== null ){
			returnValue="" ;
		} else {
		  	str = str.replaceAll("&"  , "&amp;");
		    	str = str.replaceAll("<"  , "&lt;");
			str = str.replaceAll(">"  , "&gt;");
		  	str = str.replaceAll("\'" , "&apos;");
		  	str = str.replaceAll("\"" , "&quot;");
		  	str = str.replaceAll("%22"  , "-");
		  	str = str.replaceAll("%2C"  , "");
		  	str = str.replaceAll("%2"  , "");
		  	str = str.replaceAll("'", "''");
		  	str = str.replaceAll("\"", "-");
		  	str = str.replaceAll("onmouse", "-");
		  	str = str.replaceAll("alert", "-");
		  	returnValue = str;
		}
		return returnValue ;
	}

	public static String cleanXSS(String value) {
		if(value == null) return null;
	    //You'll need to remove the spaces from the html entities below
	    value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	    value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
	    value = value.replaceAll("'", "&#39;");
	    value = value.replaceAll("eval\\((.*)\\)", "");
	    value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
	    value = value.replaceAll("script", "");
	   return value;
	}

	// 휴대폰번호 가운데 마스킹처리
	public static String maskingPhoneNum(String str) {
		String replaceString = str;

		Matcher matcher = Pattern.compile("^(\\d{3})-?(\\d{3,4})-?(\\d{4})$").matcher(str);

		if (matcher.matches()) {
			replaceString = "";

			boolean isHyphen = false;
			if (str.indexOf("-") > -1) {
				isHyphen = true;
			}

			for (int i = 1; i <= matcher.groupCount(); i++) {
				String replaceTarget = matcher.group(i);
				if (i == 2) {
					char[] c = new char[replaceTarget.length()];
					Arrays.fill(c, '*');

					replaceString = replaceString + String.valueOf(c);
				} else {
					replaceString = replaceString + replaceTarget;
				}

				if (isHyphen && i < matcher.groupCount()) {
					replaceString = replaceString + "-";
				}
			}
		}

		return replaceString;
	}

}

