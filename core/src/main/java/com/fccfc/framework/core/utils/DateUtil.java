/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.framework.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年11月6日 <br>
 * @see com.fccfc.framework.core.utils <br>
 */
public final class DateUtil {

    public static Date string2Date(String dateStr) {
        if (CommonUtil.isEmpty(dateStr)) {
            return null;
        }
        dateStr = dateStr.trim();
        Date date = null;
        switch (dateStr.length()) {
            case 8:
                date = string2Date(dateStr, DateConstants.DATE_FORMAT_8);
                break;
            case 10:
                date = string2Date(dateStr, dateStr.indexOf("/") == -1 ? DateConstants.DATE_FORMAT_10
                    : DateConstants.DATE_FORMAT_10_2);
                break;
            case 11:
                date = string2Date(dateStr, DateConstants.DATE_FORMAT_11);
                break;
            case 14:
                date = string2Date(dateStr, DateConstants.DATETIME_FORMAT_14);
                break;
            case 19:
                date = string2Date(dateStr, dateStr.indexOf("/") == -1 ? DateConstants.DATETIME_FORMAT_19
                    : DateConstants.DATETIME_FORMAT_19_2);
                break;
            case 21:
                date = string2Date(dateStr, DateConstants.DATETIME_FORMAT_21);
                break;
            case 23:
                date = string2Date(dateStr, dateStr.indexOf("/") == -1 ? DateConstants.DATETIME_FORMAT_23
                    : DateConstants.DATETIME_FORMAT_23_2);
                break;
            default:
                throw new IllegalArgumentException(dateStr + "不支持的时间格式");
        }
        return date;
    }

    public static Date string2Date(String date, String format) {
        if (CommonUtil.isEmpty(format)) {
            throw new IllegalArgumentException("the date format string is null!");
        }
        DateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date.trim());
        }
        catch (ParseException e) {
            throw new IllegalArgumentException("the date string " + date + " is not matching format: " + format, e);
        }
    }

    public static String date2String(Date date) {
        return date2String(date, DateConstants.DATETIME_FORMAT_19);
    }

    public static String date2String(Date date, String format) {
        String result = null;
        if (date != null) {
            DateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        }
        return result;
    }

    public static String getCurrentTimestamp() {
        return date2String(new Date(), DateConstants.DATETIME_FORMAT_14);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
