package com.chendu.jq.core.common;

import com.chendu.jq.core.common.jqEnum.BizDayAdjust;
import com.chendu.jq.core.common.jqEnum.Venue;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
public class JqCalendar {
    private Venue venue;
    private LinkedHashSet<LocalDate> holidays = new LinkedHashSet<>();

    public JqCalendar(Venue venue){
        this.venue = venue;
    }

    public JqCalendar(Venue venue, LinkedHashSet<LocalDate> holidays){
        this.venue = venue;
        this.holidays = holidays;
    }

    public boolean isBizDay(LocalDate date){
        return !holidays.contains(date);
    }

    public LocalDate addDays(LocalDate date, int days2Add, BizDayAdjust bizDayAdjust){
        LocalDate newDate = date.plusDays(days2Add);
        return adjust(newDate, bizDayAdjust);
    }

    public LocalDate addBizDays(LocalDate date, int days2Add, BizDayAdjust bizDayAdjust){
        if(days2Add == 0){
            return date;
        }

        int shift = days2Add < 0 ? -1 : 1;
        do{
            date = date.plusDays(1);
            if(isBizDay(date)) {
                days2Add -= shift;
            }
        }while (days2Add != 0);

        return adjust(date, bizDayAdjust);
    }

    public LocalDate nextBizDay(LocalDate date){
        return addBizDays(date, 1, BizDayAdjust.None);
    }

    public LocalDate prevBizDay(LocalDate date){
        return addBizDays(date, -1, BizDayAdjust.None);
    }

    private LocalDate adjust(LocalDate date, BizDayAdjust bizDayAdjust){
        if(holidays.contains(date)) {
            switch (bizDayAdjust)
            {
                case Following:
                    return nextBizDay(date);
                case ModifiedFollowing:
                    LocalDate newDate = nextBizDay(date);
                    return newDate.getMonth().equals(date.getMonth()) ? newDate : prevBizDay(date);
                case Previous:
                    return prevBizDay(date);
                case ModifiedPrevious:
                    newDate = prevBizDay(date);
                    return  newDate.getMonth().equals(date.getMonth()) ? newDate : nextBizDay(date);
            }
        }

        return date;
    }
}
