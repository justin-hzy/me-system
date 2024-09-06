package com.me.member;

import com.me.nascent.modules.member.service.MemberMigrationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public void transStoreMemberByRange() throws Exception {
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
        Long shopId = 100149661L;
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
    public void transZaStoreMemberByRange() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        List<Long> numbers = getOffLineStoreId();

        for (Long shopId : numbers){
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

    }


    @Test
    public void putPureMember() throws Exception {
        memberMigrationService.putPureMember();
    }

    public static List<Long> getOffLineStoreId(){
// 创建一个ArrayList实例
       List<Long> numbers = new ArrayList<>();

        // 定义要添加到列表中的整数
        long[] data = {
                100234651, 100186884, 100186879, 100186880, 100186881, 100186882, 100186883,
                100186877, 100186878, 100157262, 100150553, 100150168, 100150169, 100150170,
                100150171, 100150172, 100150173, 100150175, 100150176, 100150177, 100150178,
                100150180, 100150181, 100150182, 100150183, 100150184, 100150186, 100150187,
                100150188, 100150189, 100150190, 100150191, 100150192, 100150194, 100150195,
                100150196, 100150197, 100150198, 100150199, 100150200, 100150201, 100150202,
                100150203, 100150204, 100150205, 100150206, 100150207, 100150208, 100150209,
                100150210, 100150211, 100150212, 100150213, 100150214, 100150215, 100150216,
                100150217, 100150218, 100150219, 100150221, 100150222, 100150223, 100150224,
                100150226, 100150227, 100150229, 100150230, 100150231, 100150232, 100150234,
                100150235, 100150236, 100150237, 100150238, 100150240, 100150241
        };

        for (long num : data) {
            numbers.add(num);
        }

        return numbers;
    }
}
