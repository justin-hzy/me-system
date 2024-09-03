package com.me.member;

import com.me.nascent.modules.member.service.MemberMigrationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@Slf4j
public class MemberTest {

    @Autowired
    private MemberMigrationService memberMigrationService;


    @Test
    public void transPureMemberByRange() throws Exception {

        String year = "2024";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            // 在这里调用处理会员数据的方法
            memberMigrationService.transPureMemberByRange(startDate, endDate);
        }

    }

    @Test
    public void transZaMemberByRange() throws Exception {
        String year = "2024";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            // 在这里调用处理会员数据的方法
            memberMigrationService.transZaMemberByRange(startDate, endDate);
        }


    }

    @Test
    public void transPureStoreMemberByRange() throws Exception {
        //String year = "2024";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        /*
        2017-05-09 17:41:33泊美
        2016-09-29 15:59:39Za姬芮

        最早入会的时间

        100149663L 抖音泊美官方旗舰店 2024-07-16 到 2024-07-17 开始有数据
        100150166L 泊美品牌商城 2023年有数据
        100156928L 泊美会员中心 2024年有数据

        100150083L Za会员中心
        100149660L Za姬芮官方旗舰店
        100149661L 抖音Za姬芮官方旗舰店
        100150165L 有赞Za姬芮官方旗舰店

        */
        Long shopId = 100156928L;
        for (int year = 2023;year<=2024;year++){
            for (int month = 1; month <= 12; month++) {
                cal.set(Calendar.YEAR, year);
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
                // 在这里调用处理会员数据的方法
                memberMigrationService.transPureStoreMemberByRange(startDate, endDate,shopId);
            }
        }
    }

    @Test
    public void putPureMember() throws Exception {
        memberMigrationService.putPureMember();
    }
}
