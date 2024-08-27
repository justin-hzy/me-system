package com.me.nascent.modules.qimen.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.qimen.entity.QiMenCustomer;
import com.me.nascent.modules.qimen.entity.QiMenOrder;
import com.me.nascent.modules.qimen.entity.QiMenPromotionDetail;
import com.me.nascent.modules.qimen.entity.QiMenTrade;
import com.me.nascent.modules.qimen.service.QiMenOrderService;
import com.me.nascent.modules.qimen.service.QiMenPromotionDetailService;
import com.me.nascent.modules.qimen.service.QiMenPutTradeService;
import com.me.nascent.modules.qimen.service.QiMenTradeService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.trade.OrderDetailVo;
import com.nascent.ecrp.opensdk.domain.trade.PromotionDetailVo;
import com.nascent.ecrp.opensdk.domain.trade.TradeDetailVo;
import com.nascent.ecrp.opensdk.request.trade.TradeSaveRequest;
import com.nascent.ecrp.opensdk.response.point.PointAddResponse;
import com.nascent.ecrp.opensdk.response.trade.TradeSaveResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class QiMenPutTradeServiceImpl implements QiMenPutTradeService {

    private NascentConfig nascentConfig;

    private QiMenTradeService qiMenTradeService;

    private QiMenOrderService qiMenOrderService;

    private QiMenPromotionDetailService qiMenPromotionDetailService;

    @Override
    public void putQiMenTrade() throws Exception {

        List<QiMenTrade> qiMenTrades = qiMenTradeService.list();

        int batchSize = 100; // 每次处理的数据量
        int totalSize  = qiMenTrades.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < loopCount; i++) {

            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量

            List<QiMenTrade> batchList = qiMenTrades.subList(start, end);
            log.info("batchList=" + batchList.toString());

            List<TradeDetailVo> tradeDetailVoList = new ArrayList<>();

            for (QiMenTrade qiMenTrade : batchList){

                TradeDetailVo tradeDetailVo = new TradeDetailVo();
                String tId = qiMenTrade.getTid();
                BigDecimal bigDecimal_1 = new BigDecimal(qiMenTrade.getTotalFee());
                tradeDetailVo.setTotalFee(bigDecimal_1);
                BigDecimal bigDecimal_2 = new BigDecimal(qiMenTrade.getPayment());
                tradeDetailVo.setPayment(bigDecimal_2);
                tradeDetailVo.setShippingType(qiMenTrade.getShippingType());
                tradeDetailVo.setOutTradeId(tId);
                tradeDetailVo.setNasOuid(qiMenTrade.getBuyerOpenUid());
                tradeDetailVo.setTradeStatus(qiMenTrade.getStatus());
                tradeDetailVo.setTradeType(qiMenTrade.getType());
                tradeDetailVo.setTradeFrom("TMALL");
                Date created = sdf.parse(qiMenTrade.getCreated());
                tradeDetailVo.setCreated(created);
                BigDecimal bigDecimal_3 = new BigDecimal(qiMenTrade.getNum());
                tradeDetailVo.setNum(bigDecimal_3);
                //tradeDetailVo.setRefundStatus(qiMenTrade.getR);
                //暂定为支付宝类型
                tradeDetailVo.setPayType(8);


                QueryWrapper<QiMenOrder> qiMenOrderQuery = new QueryWrapper<>();
                qiMenOrderQuery.eq("tid",tId);

                List<QiMenOrder> qiMenOrders = qiMenOrderService.list(qiMenOrderQuery);

                if(CollUtil.isNotEmpty(qiMenOrders)){
                    List<OrderDetailVo> orderDetailVos = new ArrayList<>();
                    for (QiMenOrder qiMenOrder : qiMenOrders){
                        OrderDetailVo orderDetailVo = new OrderDetailVo();
                        orderDetailVo.setOutOrderId(qiMenOrder.getOid());
                        orderDetailVo.setOrderStatus(qiMenOrder.getStatus());
                        orderDetailVo.setTitle(qiMenOrder.getTitle());
                        orderDetailVo.setOutItemId(qiMenOrder.getNumIid());
                        BigDecimal bigDecimal_4 = new BigDecimal(qiMenOrder.getNum());
                        orderDetailVo.setOrderNum(bigDecimal_4);
                        BigDecimal bigDecimal_5 = new BigDecimal(qiMenOrder.getTotalFee());
                        orderDetailVo.setOrderTotalFee(bigDecimal_5);
                        BigDecimal bigDecimal_6 = new BigDecimal(qiMenOrder.getPayment());
                        orderDetailVo.setOrderPayment(bigDecimal_6);
                        BigDecimal bigDecimal_7 = new BigDecimal(qiMenOrder.getPrice());
                        orderDetailVo.setOrderPrice(bigDecimal_7);
                        orderDetailVos.add(orderDetailVo);
                    }
                    tradeDetailVo.setOrderDetailVoList(orderDetailVos);
                }

                QueryWrapper<QiMenPromotionDetail> qiMenPromotionDetailQuery = new QueryWrapper<>();
                qiMenPromotionDetailQuery.eq("id",tId);
                List<QiMenPromotionDetail> qiMenPromotionDetails = qiMenPromotionDetailService.list(qiMenPromotionDetailQuery);
                if (CollUtil.isNotEmpty(qiMenPromotionDetails)){
                    List<PromotionDetailVo> promotionDetailVos = new ArrayList<>();
                    for (QiMenPromotionDetail qiMenPromotionDetail : qiMenPromotionDetails){
                        PromotionDetailVo promotionDetailVo = new PromotionDetailVo();
                        promotionDetailVo.setPromotionName(qiMenPromotionDetail.getPromotionName());
                        promotionDetailVo.setGiftItemId(qiMenPromotionDetail.getGiftItemId());
                        promotionDetailVo.setGiftItemName(qiMenPromotionDetail.getGiftItemName());
                        promotionDetailVo.setPromotionActivityId(qiMenPromotionDetail.getPromotionId());
                        promotionDetailVo.setGiftItemNum(Integer.valueOf(qiMenPromotionDetail.getGiftItemNum()));
                        promotionDetailVo.setPromotionDesc(qiMenPromotionDetail.getPromotionDesc());
                        BigDecimal bigDecimal_8 = new BigDecimal(qiMenPromotionDetail.getDiscountFee());
                        promotionDetailVo.setDiscountFee(bigDecimal_8);
                        promotionDetailVos.add(promotionDetailVo);
                    }
                    tradeDetailVo.setPromotionDetailVoList(promotionDetailVos);
                }
                tradeDetailVoList.add(tradeDetailVo);
            }
            TradeSaveRequest request = new TradeSaveRequest();
            request.setTradeDetailVoList(tradeDetailVoList);

            ApiClient client = new ApiClientImpl(request);
            TradeSaveResponse response = client.execute(request);

        }
    }
}
