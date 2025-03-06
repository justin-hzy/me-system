package com.me.order;

import com.kingdee.bos.webapi.sdk.ApiRequester;
import com.me.modules.fl.dto.TransFlOrderDto;
import com.me.modules.fl.service.TransFlOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class TransFlOrderTest {

    @Autowired
    private TransFlOrderService transFlOrderService;

    @Test
    public void TransFlOrderTest(){
        TransFlOrderDto dto = new TransFlOrderDto();
        List<String> nameList = new ArrayList<>();
        nameList.add("1601015172");
        dto.setNameList(nameList);
        transFlOrderService.TransFlOrder(dto);
    }
}
