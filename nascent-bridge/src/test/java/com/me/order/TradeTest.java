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

        String startDateStr  = "2012-01-01 00:00:00";
        String endDateStr = "2012-01-01 01:59:59";

        //定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 解析字符串到 LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, formatter);
        // 转换为 Date
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime sevenDaysLater = startDateTime.plusDays(7);

        // 将 LocalDateTime 转换为 Date
        /*Date sevenDaysLaterDate = Date.from(sevenDaysLater.atZone(ZoneId.systemDefault()).toInstant());
        * Map<String,Object> resMap  = transOrderService.transOrder(nextId,startDate,sevenDaysLaterDate);
        * */

        LocalDateTime endDateTime = LocalDateTime.parse(endDateStr, formatter);
        Long nextId = 0L;
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Map<String,Object> resMap  = transOrderService.transOrder(nextId,startDate,endDate);

        nextId = (Long) resMap.get("nextId");
        startDate = (Date) resMap.get("modifyTime");
        Boolean isNext = (Boolean) resMap.get("isNext");

        log.info("nextId="+nextId);
        log.info("modifyTime="+startDate);
        log.info("isNext="+isNext);
    }

    @Test
    public void getOrderByYear() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*try {
            Date startDate = sdf.parse("2023-02-21 16:37:16");
            Date endDate = sdf.parse("2023-02-30 23:59:59");

            System.out.println(startDate.before(endDate));

            while (startDate.before(endDate)) {
                Date endDateOfWeek = new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000);
                if (endDateOfWeek.after(endDate)) {
                    endDateOfWeek = endDate;
                }

                String startStr = sdf.format(startDate);
                String endStr = sdf.format(endDateOfWeek);
                System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

                //transOrderService.transOrder(startDate,endDateOfWeek);


                startDate = new Date(endDateOfWeek.getTime() + 24 * 60 * 60 * 1000);
                System.out.println("下一个开启时间:"+startDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        String year = "2023";

        Calendar cal = Calendar.getInstance();

        for (int month = 12; month <= 12; month++) {
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
    public void putTrade(){
        transOrderService.putOrder();

    }
}
