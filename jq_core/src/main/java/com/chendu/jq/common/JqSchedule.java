package com.chendu.jq.common;

import com.chendu.jq.common.jqEnum.BusinessDayConvention;
import com.chendu.jq.common.jqEnum.Stub;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class JqSchedule {
    private List<LocalDate> gridDates;

    public JqSchedule(LocalDate startDate, LocalDate endDate, Period period, Stub stub, JqCalendar calendar, BusinessDayConvention bdc){
        gridDates = new ArrayList<>();

        switch (stub) {
            case LongEnd:
            case ShortEnd:
                LocalDate date;
                for (date = startDate; date.isBefore(endDate); date = date.plus(period)) {
                    gridDates.add(date);
                }
                if (!date.equals(endDate) && stub == Stub.LongEnd) {
                    if (gridDates.size() > 1) {
                        gridDates.remove(gridDates.size() - 1);
                    }
                }
                gridDates.add(endDate);
                break;
            case LongStart:
            case ShortStart:
                for (date = endDate; date.isAfter(startDate); date = date.minus(period)) {
                    gridDates.add(date);
                }
                if (!date.equals(startDate) && stub == Stub.LongStart) {
                    if (gridDates.size() > 1) {
                        gridDates.remove(gridDates.size() - 1);
                    }
                }
                gridDates.add(endDate);
                Collections.reverse(gridDates);
                break;
        }
    }
}
