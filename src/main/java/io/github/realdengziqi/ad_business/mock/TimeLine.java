package io.github.realdengziqi.ad_business.mock;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.sql.Time;
import java.sql.Timestamp;


/**
 * @author: tony
 * @date: 2022-12-21 5:09
 * @description:
 */
public class TimeLine {

    private final long endTimestamp;

    private long currentTimestamp;

    public TimeLine(String startTime,String endTime) {
        long startTimestamp = DateUtil.parseDateTime(startTime).toTimestamp().getTime();
        endTimestamp = DateUtil.parseDateTime(endTime).toTimestamp().getTime();
        currentTimestamp = startTimestamp;
    }

    public boolean hasNext() {
        return currentTimestamp < endTimestamp;
    }


    public DateTime next() {
        currentTimestamp = currentTimestamp + 1000L;
        return new DateTime(currentTimestamp);
    }

    public static double weight(DateTime dateTime) {
        int hour = dateTime.hour(true);
        if (hour <7) {
            return 0.3;
        }
        if (hour <12) {
            return 0.9;
        }
        if (hour <14) {
            return 0.8;
        }
        if (hour <20) {
            return 0.9;
        }
        if (hour <23) {
            return 0.6;
        }
        return 0.3;
    }

    public static boolean ifUse(DateTime dateTime) {
        double weight = weight(dateTime);
        double v = PublicStatus.random.nextDouble();
        return v < weight;
    }

public static void main(String[] args) {
    TimeLine timeLine = new TimeLine("2022-12-21 04:30:00", "2022-12-21 05:30:00");
    System.out.println(timeLine.next());
}
}
