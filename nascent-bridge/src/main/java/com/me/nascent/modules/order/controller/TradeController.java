package com.me.nascent.modules.order.controller;

import com.me.nascent.modules.order.service.TransOrderService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("trade")
@AllArgsConstructor
public class TradeController {

    private TransOrderService transOrderService;

    @Async
    @PostMapping("getOrderByYear")
    public void getOrderByYear() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date startDate = sdf.parse("2023-02-21 16:37:16");
            Date endDate = sdf.parse("2023-02-21 17:59:59");

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
        }

    }
}
