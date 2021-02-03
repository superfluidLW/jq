package com.jq.impl.common;

import com.jq.common.convention.BizDayAdjust;
import com.jq.common.convention.Venue;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Data
public class Calendar {
    private Venue venue;
    private LinkedHashSet<LocalDate> holidays = new LinkedHashSet<>();

    public Calendar(Venue venue){
        this.venue = venue;
    }

    public Calendar(Venue venue, LinkedHashSet<LocalDate> holidays){
        this.venue = venue;
        this.holidays = holidays;
    }

    public List<LocalDate> BizDateBetweenDates(LocalDate start, LocalDate end){
        List<LocalDate> bizDates = new ArrayList<>();
        for(LocalDate date = start; date.isBefore(end); date = date.plusDays(1)){
            if(isBizDay(date)){
                bizDates.add(LocalDate.from(date));
            }
        }

        return bizDates;
    }

    public boolean isBizDay(LocalDate date){
        if(holidays.isEmpty()){
            return !(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY));
        }
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
