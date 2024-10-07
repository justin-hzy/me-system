package com.me.point;

import com.me.nascent.modules.point.service.TransPointService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class PointTest {

    @Autowired
    private TransPointService transPointService;

    @Test
    public void getPurePoint() throws Exception {

        /*100000387	泊美  100000386	Za姬芮*/

        /*pcode-206261	泊美积分体系
          pcode-206256	Za线上积分体系
          pcode-206258	Za姬芮线下积分体系*/
        transPointService.transPurePoint("pcode-206261",100000387L);
    }

    @Test
    public void getZaOnlinePoint() throws Exception {
        transPointService.transZaOnlinePoint("pcode-206256",100000386L);
    }

    @Test
    public void getZaOfflinePoint() throws Exception{
        transPointService.getZaOfflinePoint("pcode-206258",100000386L);
    }

    @Test
    public void putPoint() throws Exception {
        List<String> integralAccounts = new ArrayList<>();
        /*
          pcode-206261	泊美积分体系
          pcode-206256	Za线上积分体系
          pcode-206258	Za姬芮线下积分体系
        */
        integralAccounts.add("pcode-206256");
        /*
        * 平台id
        * 19 会员
        * 111 抖音
        * 11 有赞
        * 703212 泊美淘宝
        * 703184 Za淘宝
        * */
        String platform = "11";

        for (String integralAccount : integralAccounts){
            transPointService.putPureMemberPoint(integralAccount,platform);

        }
    }


    @Test
    public void transMemberTongPoint() throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String year = "2024";

        Calendar cal = Calendar.getInstance();

        for (int month = 1; month <= 10; month++) {
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
                Date endDateOfWeek = new Date(startDate.getTime() + 15 * 60 * 1000); // 修改为30分钟
                if (endDateOfWeek.after(endDate)) {
                    endDateOfWeek = endDate;
                }

                String startStr1 = sdf.format(startDate);
                String endStr2 = sdf.format(endDateOfWeek);
                System.out.println("同步订单数据: " + startStr1 + " 到 " + endStr2);

                //to do 执行接口
                 /*
                  pcode-206261	泊美积分体系
                  pcode-206256	Za线上积分体系
                  pcode-206258	Za姬芮线下积分体系
                  */
                String integralAccount = "pcode-206256";
                transPointService.transMemberPoint(startDate,endDateOfWeek,integralAccount);
                startDate = endDateOfWeek; // 修改为1分钟

            }

        }
    }

}
