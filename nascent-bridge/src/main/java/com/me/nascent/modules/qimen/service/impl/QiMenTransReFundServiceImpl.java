package com.me.nascent.modules.qimen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.entity.QiMenReTrade;
import com.me.nascent.modules.qimen.service.QiMenReTradeService;
import com.me.nascent.modules.qimen.service.QiMenTransReFundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenTransReFundServiceImpl implements QiMenTransReFundService {

    private QiMenReTradeService qiMenReTradeService;

    @Override
    public String transReFund(QiMenDto dto) {
        String message = dto.getMessage();
        Map<String, String> messageMap = convertJsonToMap(message);

        // 校验数据合法性 todo

        QiMenReTrade qiMenReTrade = encReTrade(messageMap);

        QueryWrapper<QiMenReTrade> qiMenReTradeQuery = new QueryWrapper<>();
        qiMenReTradeQuery.eq("tid",qiMenReTrade.getTid());

        QiMenReTrade existReTrade = qiMenReTradeService.getOne(qiMenReTradeQuery);

        if(existReTrade != null){
            UpdateWrapper<QiMenReTrade> qiMenReTradeUpdate = new UpdateWrapper<>();
            qiMenReTradeUpdate.eq("tid",qiMenReTrade.getTid());

            qiMenReTradeService.update(qiMenReTrade,qiMenReTradeUpdate);
        }else {
            qiMenReTradeService.save(qiMenReTrade);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code","SUCCESS");
        jsonObject.put("return_msg","数据处理成功");
        return jsonObject.toJSONString();
    }

    public static Map<String, String> convertJsonToMap(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            Map<String, String> map = mapper.readValue(jsonString, typeFactory.constructMapType(Map.class, String.class, String.class));
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static QiMenReTrade encReTrade(Map<String,String> messageMap){
        QiMenReTrade qiMenReTrade = new QiMenReTrade();

        String snapshotUrl = messageMap.get("snapshotUrl");
        String tid = messageMap.get("tid");
        String modified = messageMap.get("modified");
        String sellerMemo = messageMap.get("sellerMemo");
        String created = messageMap.get("created");
        String payTime = messageMap.get("payTime");
        String status = messageMap.get("status");
        String receiverCity = messageMap.get("receiverCity");
        String buyerMemo = messageMap.get("buyerMemo");
        String isPartConsign = messageMap.get("isPartConsign");
        String sellerNick = messageMap.get("sellerNick");

        qiMenReTrade.setSnapshotUrl(snapshotUrl);
        qiMenReTrade.setTid(tid);
        qiMenReTrade.setModified(modified);
        qiMenReTrade.setSellerMemo(sellerMemo);
        qiMenReTrade.setCreated(created);
        qiMenReTrade.setPayTime(payTime);
        qiMenReTrade.setStatus(status);
        qiMenReTrade.setReceiverCity(receiverCity);
        qiMenReTrade.setBuyerMemo(buyerMemo);
        qiMenReTrade.setIsPartConsign(isPartConsign);
        qiMenReTrade.setSellerNick(sellerNick);

        return qiMenReTrade;
    }
}
