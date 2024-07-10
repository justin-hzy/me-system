package com.me.k3.sale;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.dms.entity.CommStore;
import com.me.modules.dms.entity.SaleFrDt1;
import com.me.modules.dms.entity.SaleFrDt2;
import com.me.modules.dms.entity.SaleFrMain;
import com.me.modules.dms.service.CommStoreService;
import com.me.modules.dms.service.SaleFrDt1Service;
import com.me.modules.dms.service.SaleFrDt2Service;
import com.me.modules.dms.service.SaleFrMainService;
import com.me.modules.fl.entity.FlOrderFormTableMain;
import com.me.modules.fl.service.FlOrderFormTableMainService;
import com.me.modules.k3.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class SaleTest {

    @Autowired
    private SaleFrMainService saleFrMainService;

    @Autowired
    private SaleFrDt1Service saleFrDt1Service;

    @Autowired
    private CommStoreService commStoreService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SaleFrDt2Service saleFrDt2Service;

    @Test
    public void getSale(){
        String frRequestId= "163227";
        QueryWrapper<SaleFrMain> saleFrMainQuery = new QueryWrapper<>();
        saleFrMainQuery.eq("requestid",frRequestId);

        SaleFrMain saleFrMain = saleFrMainService.getOne(saleFrMainQuery);
        String id = saleFrMain.getId();
        //发货店仓
        String fHdc = saleFrMain.getFhdc();

        //统计正品各sku的发货数量
        QueryWrapper<SaleFrDt1> saleFrDt1Query = new QueryWrapper<>();
        saleFrDt1Query.select("khddh","hptxm","sum(fhl) fhl")
                .groupBy("khddh","hptxm")
                .eq("mainid",id);
        List<SaleFrDt1> saleFrDt1s = saleFrDt1Service.list(saleFrDt1Query);

        //统计样品各sku的发货数量
        QueryWrapper<SaleFrDt2> saleFrDt2Query = new QueryWrapper<>();
        saleFrDt2Query.select("khddh","hptxm","sum(fhsl) fhsl")
                .groupBy("khddh","hptxm")
                .eq("mainid",id);

        List<SaleFrDt2> saleFrDt2s = saleFrDt2Service.list(saleFrDt2Query);




        Map<String,List<SaleFrDt1>> dt1Map = new HashMap<>();

        Map<String,List<SaleFrDt2>> dt2Map = new HashMap<>();

        //样品拆单
        if(saleFrDt1s.size()>0){
            for (SaleFrDt1 saleFrDt1 : saleFrDt1s){
                String khddh = saleFrDt1.getKhddh();
                khddh = "TW"+khddh;
                if(dt1Map.containsKey(khddh)){
                    List<SaleFrDt1> saleFrDt1List = dt1Map.get(khddh);
                    saleFrDt1List.add(saleFrDt1);
                }else {
                    List<SaleFrDt1> saleFrDt1List = new ArrayList<>();
                    saleFrDt1List.add(saleFrDt1);
                    dt1Map.put(khddh,saleFrDt1List);
                }
            }
        }
        //log.info(dt1Map.toString());

        //处理样品各子单单据
        dt1Map.forEach((key,value) ->{
            //获取子单单据
            List<SaleFrDt1> sonOrders = dt1Map.get(key);

            if(sonOrders.size()>0){

                //遍历当前单据的明细数据
                for (SaleFrDt1 saleFrDt1 : sonOrders){
                    //货品条形码
                    String hpTxm = saleFrDt1.getHptxm();
                    //货品发货量
                    Integer fHl = saleFrDt1.getFhl();



                    /*try {
                       String resJsonStr = inventoryService.getTWInventory(hpTxm,fHdc);

                       JSONArray resJsonArr = JSONArray.parseArray(resJsonStr);

                       if(resJsonArr.size()>0){
                            for(int i =0; i<resJsonArr.size();i++){
                                Integer fBaseQty = resJsonArr.getJSONObject(i).getInteger("FBaseQty");
                                if(Integer.compare(fBaseQty,fHl) <0){
                                    //仓库数量<单据发货数量
                                    Integer hkQty = fHl - fBaseQty;


                                }
                            }
                       }else {
                            // todo
                       }



                        //JSONObject
                    } catch (Exception e) {
                        //to do 记录异常表
                        throw new RuntimeException(e);
                    }*/

                    //查看所属主题
                    /*QueryWrapper<CommStore> commStoreQuery = new QueryWrapper<>();
                    commStoreQuery.select("szzt").eq("hpbh",hptxm).isNotNull("szzz");
                    List<CommStore> commStores =  commStoreService.list(commStoreQuery);
                    if (commStores.size()>0 && commStores.size()==1){
                        String sZzt = commStores.get(0).getSzzt();
                        if("ZT021".equals(sZzt)){
                            //香港

                        }else if("ZT026".equals(sZzt)){
                            //台湾

                        }

                    }else {
                        log.info("商品库sku="+hptxm+"数据异常");
                        return;
                    }*/
                }
            }


        });










    }

    @Test
    public void test(){
        Map<String,String> map = new HashMap();
        String str = map.get("code");
        log.info("str="+str);
    }
}
