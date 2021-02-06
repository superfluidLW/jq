package com.jq.common.deal.equity;

import com.jq.common.convention.*;
import com.jq.common.deal.DealType;
import com.jq.common.market.Market;
import com.jq.common.market.Security;
import com.jq.common.output.CashFlow;
import com.jq.common.output.JsonUtils;
import com.jq.common.output.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Data
@Slf4j
public class VanillaOption extends Option {
    public VanillaOption() {
        super();
        dealType = DealType.VanillaOption;
    }

    @Override
    public Option bumpMaturity(int offset) {
        try {
            VanillaOption vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), VanillaOption.class);

            vo.setMaturityDate(this.maturityDate.plusDays(offset));
            vo.setExerciseDate(exerciseDate.plusDays(offset));
            return vo;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    @Override
    public Double[][] payOffChart() {
        double delta = strike * 0.01;
        Double[][] payoff = new Double[40][2];
        int offset = payoff.length/2;
        for(int i = 0; i < payoff.length; ++i){
            Double price = strike + (i-offset) * delta;
            Double po = optionDirection == OptionDirection.Call ? Math.max(price-strike, 0) : Math.max(strike-price, 0);
            payoff[i][0] = price;
            payoff[i][1] = po;
        }
        return payoff;
    }

    @Override
    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {
        Double price = path.get(exerciseDate);
        return (optionDirection == OptionDirection.Call ? Math.max(price-strike, 0.0) : Math.max(strike-price, 0.0))*notional;
    }

    public static VanillaOption sampleDeal() {
        VanillaOption vanillaOption = new VanillaOption();
        vanillaOption.setStartDate(LocalDate.now());
        vanillaOption.setMaturityDate(LocalDate.now().plusYears(1));
        vanillaOption.setExerciseDate(LocalDate.now().plusYears(1));
        vanillaOption.setStrike(1000.0);
        vanillaOption.setOptionDirection(OptionDirection.Call);
        vanillaOption.setUnderlying(new Security("SH000300", Venue.ShExg));
        vanillaOption.setDayCount(DayCount.Act365);
        vanillaOption.setNotional(1.0);
        vanillaOption.setDomCurrency(Currency.Cny);
        vanillaOption.setValuationModel(ValuationModel.Analytical);
        return vanillaOption;
    }

}
