package com.me.bos;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.exception.BusinessException;
import com.me.common.utils.DateUtils;
import com.me.modules.order.customer.dto.QueryIdAndCodeRespDto;
import com.me.modules.order.customer.service.CustomerService;
import com.me.modules.order.mapper.DateMapper;
import com.me.modules.order.mretail.dto.TransStockInMRetailReqDto;
import com.me.modules.order.mretail.entity.MRetail;
import com.me.modules.order.mretail.service.MRetailService;
import com.me.modules.order.mretail.service.StockInMRetailService;
import com.me.modules.order.msale.service.MSaleService;
import com.me.modules.order.mtransfer.entity.MTransfer;
import com.me.modules.order.mtransfer.service.MTransferService;
import com.me.modules.order.stockin.entity.WangDianAbroadStockIn;
import com.me.modules.order.stockin.service.WangDianAbroadStockInListService;
import com.me.modules.order.stockin.service.WangDianAbroadStockInService;
import com.me.modules.order.store.entity.CStore;
import com.me.modules.order.store.entity.StoreRel;
import com.me.modules.order.store.service.CStoreService;
import com.me.modules.order.store.service.StoreRelService;
import com.me.modules.sys.mapper.IdMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Slf4j
public class BosTest1_2 {

    @Autowired
    private WangDianAbroadStockInService wangDianAbroadStockInService;

    @Autowired
    private StockInMRetailService stockInMRetailService;



    @Test
    public void transMRetailJob(){
        List<WangDianAbroadStockIn> inOrderList = wangDianAbroadStockInService.queryUnTransWangDianAbroadStockIn();
        log.info("inOrderList="+inOrderList.toString());

        for(WangDianAbroadStockIn order : inOrderList){
//            TransStockInMRetailReqDto reqDto = new TransStockInMRetailReqDto();
//            stockInMRetailService.transStockInMRetail(order,reqDto);
            try {
                TransStockInMRetailReqDto reqDto = new TransStockInMRetailReqDto();
                stockInMRetailService.transStockInMRetail(order,reqDto);
            }catch (Exception e){
                log.info(e.getMessage());
                //wangDianAbroadStockInService.updateError(order.getOrderNo(),e.getMessage());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void transStockInMRetail(WangDianAbroadStockIn order, TransStockInMRetailReqDto reqDto){

    }


    public void test(){
        int i = 5;
        i = 5/0;
    }
}
