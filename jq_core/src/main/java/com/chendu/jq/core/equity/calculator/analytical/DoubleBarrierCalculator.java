package com.chendu.jq.core.equity.calculator.analytical;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.equity.DoubleBarrierOption;
import com.chendu.jq.core.equity.calculator.OptionCalculator;
import com.chendu.jq.core.market.JqMarket;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.time.LocalDate;

public class DoubleBarrierCalculator extends OptionCalculator {
    @Override
    public Double calcPv(JqTrade trade, JqMarket jqMarket){
        BarrierOptionPvPricer barrierOptionPvPricer = new BarrierOptionPvPricer((DoubleBarrierOption) trade, jqMarket);
        return barrierOptionPvPricer.Pv();
    }
}

class BarrierOptionPvPricer {
    public static NormalDistribution normal = new NormalDistribution(0.0, 1.0);
    private OptionDirection _optionType;

    private double _S;  // spot price
    private double _K;  // strike
    private double _A;  // barrier, lower barrier in case of double barrier option
    private double _B; // upper barrier in case of double barrier option
    private double _T;  // maturity in years
    private double _r; // insterest rate
    private double _v; // vol

    private double b;
    private double a;
    private double x0;
    private double v2;
    private double u;

    private double _rebate;
    private double _coupon;

    public BarrierOptionPvPricer(DoubleBarrierOption barrierOption, JqMarket jqMarket) {
        LocalDate exerciseDate = barrierOption.getExerciseDates().get(0);
        Double exerciseTime = barrierOption.getDayCount().yearFraction(jqMarket.getMktDate(), exerciseDate);
        Double riskFreeRate = jqMarket.jqCurve(barrierOption.getDomCurrency()).getZeroRate(exerciseTime);
        Double spotPrice = jqMarket.tickerPrice(barrierOption.getUnderlyingTicker());
        Double vol = jqMarket.tickerVol(barrierOption.getUnderlyingTicker());
        Double exerciseInYears = barrierOption.getDayCount().yearFraction(jqMarket.getMktDate(), exerciseDate);

        _optionType = barrierOption.optionDirection;
        _rebate = barrierOption.getKoRebate();
        _coupon = barrierOption.getKiCoupon();
        _K = barrierOption.getStrike();
        _S = spotPrice;
        _T = exerciseInYears;
        _v = vol;
        _r = riskFreeRate;
        _B = barrierOption.getUBarrier();
        _A = barrierOption.getLBarrier();

        b = Math.log(_B);
        a = Math.log(_A);
        x0 = Math.log(_S);
        v2 = Math.pow(_v, 2);
        u = _r - v2 / 2;
    }

    public double Pv() {
        Double q = Q();
        Double rebate = (1 - q) * _rebate;
        Double coupon = q * _coupon;
        switch (_optionType) {
            case Call:
                return CallCalc() + rebate;
            case Put:
                return PutCalc() + coupon;
        }
        return Double.NaN;
    }

    private double Q() {
        Double result = M(x0) - M(2 * b - x0);
        Integer n = 1;
        Double newTerm = 1.0e100;
        while (Math.abs(newTerm) > 1e-28) {
            newTerm = M(x0 - 2 * n * (b - a)) - M(2 * b - x0 - 2 * n * (b - a));
            result += newTerm;
            n++;
        }
        n = 1;
        newTerm = 1.0e100;
        while (Math.abs(newTerm) > 1e-28) {
            newTerm = M(x0 - 2 * n * (b - a)) - M(2 * b - x0 - 2 * n * (b - a));
            result += newTerm;
            n--;
        }
        return result;
    }

    private double M(double c) {
        Double temp1 = 1 / Math.sqrt(v2 * _T) * Math.exp(u * (c - x0) / v2);
        Double temp2 = normal.cumulativeProbability((b - c - u * _T) / Math.sqrt(v2 * _T)) - normal.cumulativeProbability((a - c - u * _T) / Math.sqrt(v2 * _T));
        return temp1 * temp2;
    }

    private double CallCalc() {
        Double result = L(x0, Math.max(a, Math.log(_K)), b) - L((2 * b - x0), Math.max(a, Math.log(_K)), b);
        if (_K < _B) {
            Integer n = 1;
            Double newTerm = 1.0e100;
            while (Math.abs(newTerm) > 1e-28) {
                newTerm = L((x0 - 2 * n * (b - a)), Math.max(a, Math.log(_K)), b) - L((2 * b - x0 - 2 * n * (b - a)), Math.max(a, Math.log(_K)), b);
                result += newTerm;
                n++;
            }
            n = -1;
            newTerm = 1.0e100;
            while (Math.abs(newTerm) > 1e-28) {
                newTerm = L((x0 - 2 * n * (b - a)), Math.max(a, Math.log(_K)), b) - L((2 * b - x0 - 2 * n * (b - a)), Math.max(a, Math.log(_K)), b);
                result += newTerm;
                n--;
            }
        }
        return result;
    }

    private double PutCalc() {
        Double result = L(2 * b - x0, a, Math.min(b, Math.log(_K))) - L(x0, a, Math.min(b, Math.log(_K)));
        if (_K > _A) {
            Integer n = 1;
            Double newTerm = 1.0e100;
            while (Math.abs(newTerm) > 1e-28) {
                newTerm = L((2 * b - x0 - 2 * n * (b - a)), a, Math.min(b, Math.log(_K))) - L((x0 - 2 * n * (b - a)), a, Math.min(b, Math.log(_K)));
                result += newTerm;
                n++;
            }
            n = -1;
            newTerm = 1.0e100;
            while (Math.abs(newTerm) > 1e-28) {
                newTerm = L((2 * b - x0 - 2 * n * (b - a)), a, Math.min(b, Math.log(_K))) - L((x0 - 2 * n * (b - a)), a, Math.min(b, Math.log(_K)));
                result += newTerm;
                n--;
            }
        }
        return result;
    }

    private double L(double c, double g, double h) {
        Double tmp1 = Math.exp(u / v2 * (c - x0) - _r * _T);
        Double tmp2 = Math.exp(c + (u + v2 / 2) * _T) * normal.cumulativeProbability((h - c - (u + v2) * _T) / (_v * Math.sqrt(_T)))
            - _K * normal.cumulativeProbability((h - c - u * _T) / (_v * Math.sqrt(_T)));
        Double tmp3 = Math.exp(c + (u + v2 / 2) * _T) * normal.cumulativeProbability((g - c - (u + v2) * _T)) / (_v * Math.sqrt(_T))
            - _K * normal.cumulativeProbability((g - c - u * _T) / (_v * Math.sqrt(_T)));

        Double result = tmp1 * (tmp2 - tmp3);
        return result;
    }
}
