package com.me.nascent.modules.qimen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.entity.QiMenCustomer;
import com.me.nascent.modules.qimen.entity.QiMenCustomerExtend;
import com.me.nascent.modules.qimen.service.QiMenCustomerExtendService;
import com.me.nascent.modules.qimen.service.QiMenCustomerService;
import com.me.nascent.modules.qimen.service.QiMenTransCustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenTransCustomerServiceImpl implements QiMenTransCustomerService {

    private QiMenCustomerService qiMenCustomerService;

    private QiMenCustomerExtendService qiMenCustomerExtendService;

    @Override
    public String transCustomer(QiMenDto dto) throws Exception {

        String message = dto.getMessage();

        //message = "{\"city\":\"New York\",\"age\":30,\"name\":\"John\"}";

        /*String sign = dto.getSign();*/

        JSONObject messageJsonObj = JSONObject.parseObject(message);


        /*Map<String, String> messageMap = convertJsonToMap(message);


        List<Map<String, String>> collectionM = new ArrayList<>();

        collectionM.add(messageMap);

        String result  = sortAndConcatenateParameters(collectionM);

        String secret = "";

        String stringSignTemp = secret+result;*/

        //转换对象
        QiMenCustomer qiMenCustomer = encCustomer(messageJsonObj);

        //拓展信息
        JSONObject extendJsonObj = messageJsonObj.getJSONObject("extend");

        QiMenCustomerExtend qiMenCustomerExtend = encExpand(extendJsonObj,qiMenCustomer);


        QueryWrapper<QiMenCustomer> qiMenCustomerQuery = new QueryWrapper<>();
        if(qiMenCustomer.getNasOuid() !=null && qiMenCustomer.getOuid() == null){
            qiMenCustomerQuery.eq("nasOuid",qiMenCustomer.getNasOuid())
                    .eq("sellerNick",qiMenCustomer.getSellerNick());
        }else if(qiMenCustomer.getNasOuid() ==null && qiMenCustomer != null){
            qiMenCustomerQuery.eq("ouid",qiMenCustomer.getOuid())
                    .eq("sellerNick",qiMenCustomer.getSellerNick());
        }

        QiMenCustomer existObj = qiMenCustomerService.getOne(qiMenCustomerQuery);
        if(existObj != null){
            String existFirstBindCardTime = existObj.getFirstBindCardTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date existDate = sdf.parse(existFirstBindCardTime);

            Date newDate = sdf.parse(qiMenCustomer.getFirstBindCardTime());

            if(existDate.before(newDate)){

            }

            UpdateWrapper<QiMenCustomer> qiMenCustomerUpdate = new UpdateWrapper<>();
            if(qiMenCustomer.getNasOuid() !=null && qiMenCustomer.getOuid() == null){
                qiMenCustomerQuery.eq("nasOuid",qiMenCustomer.getNasOuid())
                        .eq("sellerNick",qiMenCustomer.getSellerNick());
            }else if(qiMenCustomer.getNasOuid() ==null && qiMenCustomer != null){
                qiMenCustomerQuery.eq("ouid",qiMenCustomer.getOuid())
                        .eq("sellerNick",qiMenCustomer.getSellerNick());
            }
            qiMenCustomerService.update(qiMenCustomer,qiMenCustomerUpdate);

            if(qiMenCustomerExtend != null){
                QueryWrapper<QiMenCustomerExtend> qiMenCustomerExtendQuery = new QueryWrapper<>();
                if(qiMenCustomerExtend.getNasOuid() != null && qiMenCustomerExtend.getOuid() == null ){
                    qiMenCustomerExtendQuery.eq("nasOuid",qiMenCustomer.getNasOuid())
                            .eq("sellerNick",qiMenCustomer.getSellerNick());
                }else if(qiMenCustomerExtend.getNasOuid() == null && qiMenCustomerExtend.getOuid() != null ){
                    qiMenCustomerExtendQuery.eq("ouid",qiMenCustomer.getOuid())
                            .eq("sellerNick",qiMenCustomer.getSellerNick());
                }
                QiMenCustomerExtend existQiMenCustomerExtend = qiMenCustomerExtendService.getOne(qiMenCustomerExtendQuery);


                if(existQiMenCustomerExtend != null){
                    UpdateWrapper<QiMenCustomerExtend> qiMenCustomerExtendUpdate = new UpdateWrapper<>();

                    if(qiMenCustomer.getNasOuid() !=null && qiMenCustomer.getOuid() == null){
                        qiMenCustomerExtendUpdate.eq("nasOuid",qiMenCustomer.getNasOuid())
                                .eq("sellerNick",qiMenCustomer.getSellerNick());
                    }else if(qiMenCustomer.getNasOuid() ==null && qiMenCustomer != null){
                        qiMenCustomerExtendUpdate.eq("ouid",qiMenCustomer.getOuid())
                                .eq("sellerNick",qiMenCustomer.getSellerNick());
                    }
                    qiMenCustomerExtendService.update(qiMenCustomerExtend,qiMenCustomerExtendUpdate);
                }else {
                    qiMenCustomerExtendService.save(qiMenCustomerExtend);
                }
            }
        }else {
            qiMenCustomerService.save(qiMenCustomer);

            if(qiMenCustomerExtend != null){
                qiMenCustomerExtendService.save(qiMenCustomerExtend);
            }
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


    public static String sortAndConcatenateParameters(List<Map<String, String>> collectionM) {
        Map<String, String> params = new TreeMap<>();
        for (Map<String, String> map : collectionM) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null && !value.isEmpty()) {
                    params.put(key, value);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(entry.getKey()).append(entry.getValue());
        }

        return result.toString();
    }

    public static QiMenCustomer encCustomer(JSONObject messageJsonObj){
        String firstBindCardTime = messageJsonObj.getString("firstBindCardTime");
        //最新解绑时间
        String lastedUnbindCardTime = messageJsonObj.getString("lastedUnbindCardTime");
        //会员级别
        String level = messageJsonObj.getString("level");
        //加密手机号
        String mixMobile = messageJsonObj.getString("mixMobile");
        //公司级会员唯一标识
        String omid = messageJsonObj.getString("omid");
        //店铺级会员唯一标识
        String ouid = messageJsonObj.getString("ouid");
        //会员积分
        String point = messageJsonObj.getString("point");
        //卖家昵称
        String sellerNick = messageJsonObj.getString("sellerNick");
        //南讯ouid
        String nasOuid = messageJsonObj.getString("nasOuid");




        QiMenCustomer qiMenCustomer = new QiMenCustomer();
        qiMenCustomer.setNasOuid(nasOuid);
        qiMenCustomer.setSellerNick(sellerNick);
        qiMenCustomer.setPoint(point);
        qiMenCustomer.setOuid(ouid);
        qiMenCustomer.setOmid(omid);
        qiMenCustomer.setMixMobile(mixMobile);
        qiMenCustomer.setLevel(level);
        qiMenCustomer.setLastedUnbindCardTime(lastedUnbindCardTime);
        qiMenCustomer.setFirstBindCardTime(firstBindCardTime);

        return qiMenCustomer;
    }


    public static QiMenCustomerExtend encExpand(JSONObject extendJsonObj,QiMenCustomer qiMenCustomer){
        String babyBirthday = extendJsonObj.getString("babyBirthday");
        String birthday = extendJsonObj.getString("birthday");
        String city = extendJsonObj.getString("city");
        String province = extendJsonObj.getString("province");
        String email = extendJsonObj.getString("email");
        String sex = extendJsonObj.getString("sex");
        String name = extendJsonObj.getString("name");

        QiMenCustomerExtend qiMenCustomerExtend = new QiMenCustomerExtend();
        qiMenCustomerExtend.setNasOuid(qiMenCustomer.getNasOuid());
        qiMenCustomerExtend.setSellerNick(qiMenCustomer.getSellerNick());
        qiMenCustomerExtend.setBabyBirthday(babyBirthday);
        qiMenCustomerExtend.setBirthday(birthday);
        qiMenCustomerExtend.setCity(city);
        qiMenCustomerExtend.setProvince(province);
        qiMenCustomerExtend.setEmail(email);
        qiMenCustomerExtend.setSex(sex);
        qiMenCustomerExtend.setName(name);

        return qiMenCustomerExtend;
    }
}
