package com.chendu.jq.common;

import com.chendu.jq.common.jqEnum.BusinessDayConvention;
import com.chendu.jq.common.jqEnum.Venue;
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

    public LocalDate addDays(LocalDate date, int days2Add, BusinessDayConvention businessDayConvention){
        LocalDate newDate = date.plusDays(days2Add);
        return adjust(newDate, businessDayConvention);
    }

    public LocalDate addBizDays(LocalDate date, int days2Add, BusinessDayConvention businessDayConvention){
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

        return adjust(date, businessDayConvention);
    }

    public LocalDate nextBizDay(LocalDate date){
        return addBizDays(date, 1, BusinessDayConvention.None);
    }

    public LocalDate prevBizDay(LocalDate date){
        return addBizDays(date, -1, BusinessDayConvention.None);
    }

    private LocalDate adjust(LocalDate date, BusinessDayConvention businessDayConvention){
        if(holidays.contains(date)) {
            switch (businessDayConvention)
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
