package com.me.nascent.modules.member.service.impl;

import com.me.nascent.modules.member.service.MemberMigrationService;
import com.me.nascent.modules.member.service.TransMemberService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@AllArgsConstructor
public class MemberMigrationServiceImpl implements MemberMigrationService {

    private TransMemberService transMemberService;

    @Override
    public void transMemberByRange(Date startDate, Date endDate) throws Exception {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            /*Date startDate = sdf.parse("2024-04-01 16:37:01");
            Date endDate = sdf.parse("2024-04-28 59:59:59");*/

            while (startDate.before(endDate)) {
                Date endDateOfWeek = new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000); // 修改为30分钟
                if (endDateOfWeek.after(endDate)) {
                    endDateOfWeek = endDate;
                }

                /*String startStr = sdf.format(startDate);
                String endStr = sdf.format(endDateOfWeek);*/
                //System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

                transMemberService.TransMemberByRange(startDate,endDateOfWeek);

                startDate = endDateOfWeek;  // 修改为1分钟
                //System.out.println("下一个开启时间:" + startDate);
            }

            /*Date endDateOfWeek = new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000); // 修改为30分钟
            if (endDateOfWeek.after(endDate)) {
                endDateOfWeek = endDate;
            }

            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDateOfWeek);
            System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

            transMemberService.TransMemberByRange(startDate,endDateOfWeek);

            startDate = endDateOfWeek; // 修改为1分钟*/

        }catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
