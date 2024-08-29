package com.me.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.order.service.*;
import com.me.nascent.modules.order.entity.*;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;

import com.nascent.ecrp.opensdk.domain.customer.NickInfo;
import com.nascent.ecrp.opensdk.domain.trade.PromotionsVo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.OrdersVo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.TradeByModifySgFinishInfo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.TradesVo;
import com.nascent.ecrp.opensdk.request.trade.TradeSynRequest;
import com.nascent.ecrp.opensdk.response.trade.TradeSynResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@Slf4j
public class TradeTest {

    @Autowired
    private NascentConfig nascentConfig;

    @Autowired
    private TokenService tokenService;


    @Autowired
    TransOrderService transOrderService;


    @Test
    public void getOrderByDay() throws Exception {

        String startDateStr  = "2023-02-24 00:00:00";
        String endDateStr = "2023-02-28 23:59:59";


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = sdf.parse(startDateStr);

        Date endDate = sdf.parse(endDateStr);

        while (startDate.before(endDate)) {
            Date endDateOfWeek = new Date(startDate.getTime() + 30 * 60 * 1000); // 修改为30分钟
            if (endDateOfWeek.after(endDate)) {
                endDateOfWeek = endDate;
            }

            String startStr1 = sdf.format(startDate);
            String endStr2 = sdf.format(endDateOfWeek);
            System.out.println("同步订单数据: " + startStr1 + " 到 " + endStr2);

            transOrderService.transOrder(startDate, endDateOfWeek);

            startDate = endDateOfWeek; // 修改为1分钟
            //System.out.println("下一个开启时间:" + startDate);
        }
    }

    @Test
    public void getOrderByYear() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String year = "2024";

        Calendar cal = Calendar.getInstance();

        for (int month = 8; month <= 8; month++) {
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startDate = cal.getTime();

            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date endDate = cal.getTime();

            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDate);
            System.out.println("同步会员数据: " + startStr + " 到 " + endStr);

            while (startDate.before(endDate)) {
                Date endDateOfWeek = new Date(startDate.getTime() + 30 * 60 * 1000); // 修改为30分钟
                if (endDateOfWeek.after(endDate)) {
                    endDateOfWeek = endDate;
                }

                String startStr1 = sdf.format(startDate);
                String endStr2 = sdf.format(endDateOfWeek);
                System.out.println("同步订单数据: " + startStr1 + " 到 " + endStr2);

                transOrderService.transOrder(startDate, endDateOfWeek);

                startDate = endDateOfWeek; // 修改为1分钟
                //System.out.println("下一个开启时间:" + startDate);
            }

        }



        /*try {
            Date startDate = sdf.parse("2023-12-01 00:00:00");
            Date endDate = sdf.parse("2023-12-31 17:59:59");

            while (startDate.before(endDate)) {
                Date endDateOfWeek = new Date(startDate.getTime() + 30 * 60 * 1000); // 修改为30分钟
                if (endDateOfWeek.after(endDate)) {
                    endDateOfWeek = endDate;
                }

                String startStr = sdf.format(startDate);
                String endStr = sdf.format(endDateOfWeek);
                System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

                transOrderService.transOrder(startDate, endDateOfWeek);

                startDate = endDateOfWeek; // 修改为1分钟
                //System.out.println("下一个开启时间:" + startDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/



    }


    @Test
    public void putTrade() throws Exception {
        transOrderService.putOrder();

    }
}
