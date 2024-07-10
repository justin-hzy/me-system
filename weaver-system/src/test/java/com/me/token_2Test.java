package com.me;


import com.me.modules.dms.sys.service.DMSTokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class token_2Test {

    /**
     * 模拟缓存服务
     */
    private static final Map<String,String> SYSTEM_CACHE = new HashMap<>();


    /**
     * ecology系统发放的授权许可证(appid)
     */
    private static final String APPID = "0ddf6d56a167eaf78d6c6f4a8ffaa086";

    @Autowired
    DMSTokenService dmsTokenService;


    @Test
    public void tokenDemo(){
        dmsTokenService.sumbit();

    }


}
