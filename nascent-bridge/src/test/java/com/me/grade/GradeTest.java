package com.me.grade;

import com.me.nascent.modules.grade.service.TransGradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class GradeTest {

    @Autowired
    TransGradeService transGradeService;



    @Test
    public void transGradeTest() throws Exception {

        //100000387 泊美  100000386	Za姬芮
        Long viewId = 100000387L;


    }

    @Test
    public void transZaGradeTest() throws Exception {

        //100000387 泊美  100000386	Za姬芮
        Long viewId = 100000386L;


    }

    @Test
    public void checkPureGrade(){
        //transGradeService.checkPureGrade();
    }


    @Test
    public void putGrade() throws Exception {
        transGradeService.putPureGrade();
    }


    @Test
    public void putMemberTongGrade() throws Exception {
        //100000387 泊美  100000386	Za姬芮
        //Long viewId = 100000387L;
        //100149662L 泊美 100149660L Za姬芮
        Long shopId = 100149660L;
        transGradeService.putMemberTongGrade(shopId);
    }

    @Test
    public void transMemberTongGrade() throws Exception {
        //100000387 泊美  100000386	Za姬芮
        Long viewId = 100000386L;

        transGradeService.transMemberTongGrade(viewId);
    }

    @Test
    public void transShopActiveCustomerGrade() throws Exception {
        //100000387L 泊美  100000386L	Za姬芮
        Long viewId = 100000386L;
        transGradeService.transShopActiveCustomerGrade(viewId);
    }


    @Test
    public void transOffLineShopActiveCustomerGrade() throws Exception{
        transGradeService.transOffLineShopActiveCustomerGrade();
    }


    @Test
    public void putShopActiveCustomerGrade() throws Exception {
        List<Long> shopIds = new ArrayList<>();
        //抖店 泊美 平台id 111 改执行sql
        shopIds.add(100149663L);
        //有赞 泊美 平台 id 11 100150166
        //shopIds.add(100150166L);
        //会员中心 平台id 19
        //泊美 会员 100156928
        //shopIds.add(100156928L);
        //Za 会员
        //shopIds.add(100150083L);
        //抖店Za 100149661 平台id 111 改执行sql
        //shopIds.add(100149661L);
        //有赞Za 100150165 平台 id 11
        //shopIds.add(100150165L);
        transGradeService.putShopActiveCustomerGrade(shopIds);
    }

    @Test
    public void putOffLineShopActiveCustomerGrade() throws Exception {
        List<Long> shopIds = new ArrayList<>();

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
            shopIds.add(num);
        }

        transGradeService.putOffLineShopActiveCustomerGrade(shopIds);
    }
}
