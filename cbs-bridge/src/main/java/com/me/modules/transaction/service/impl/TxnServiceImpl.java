package com.me.modules.transaction.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.sys.request.RequestService;
import com.me.modules.sys.response.ResponseService;
import com.me.modules.token.service.TokenService;
import com.me.modules.transaction.dto.QryTxnReqDto;
import com.me.modules.transaction.entity.TransactionDetail;
import com.me.modules.transaction.service.TransactionDetailService;
import com.me.modules.transaction.service.TxnService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class TxnServiceImpl implements TxnService {

    private RequestService requestService;

    private TokenService tokenService;

    private ResponseService responseService;

    private TransactionDetailService transactionDetailService;

    static final String bodyEncryptionKey = "04CD09E422766D2412A9BBF802D2185C548412EF9CC20D47357D2975DFEB9CBD5270FA3F17F261B77A471C1800F7391407134C2629FEA3EDD58F67340914F19723";

    static final String signEncryptionPrivateKey = "96c0ef256c22c15ff6c5f70669b3a8d6ec1352cb490ecade7c9b3c386cbf22b8";

    static final String TARGET_URL = "https://tmcapi.cmbchina.com/openapi/account/openapi/v1/transaction-detail/query";

    static final String bodyDecryptionKey = "96c0ef256c22c15ff6c5f70669b3a8d6ec1352cb490ecade7c9b3c386cbf22b8";

    @Override
    public Map<String,Integer> queryTxn(int currentPage,String date) throws IOException {


        QryTxnReqDto qryTxnReqDto = new QryTxnReqDto();
        qryTxnReqDto.setDateType("0");
//        qryTxnReqDto.setStartDate(yesterdayString);
//        qryTxnReqDto.setEndDate(yesterdayString);
//        qryTxnReqDto.setStartDate("2024-01-10");
//        qryTxnReqDto.setEndDate("2024-01-11");


        qryTxnReqDto.setStartDate(date);
        qryTxnReqDto.setEndDate(date);
        qryTxnReqDto.setPageSize(1000);
        qryTxnReqDto.setCurrentPage(currentPage);
        String requestData = JSONObject.toJSONString(qryTxnReqDto);

        CloseableHttpClient client = HttpClients.custom()
                // 禁止HttpClient自动解压缩
                .disableContentCompression()
                .build();

        String token = tokenService.queryToken();

        //String token =  "ca89b851-835c-4a29-bb73-e6ac1b6dca1f";

        HttpPost httpPost = requestService.setupRequest(requestData,token,signEncryptionPrivateKey,TARGET_URL,bodyEncryptionKey);
        String jsonResp;
        try (CloseableHttpResponse response = client.execute(httpPost)) {
            byte[] finalResponseData = responseService.handleResponse(response,bodyDecryptionKey);
            jsonResp = new String(finalResponseData);
            //log.info("\n返回结果：{}", jsonResp);
        }catch (IOException ignored){
            throw new IOException("网络连接失败或超时！");
        }finally {
            client.close();
        }

        JSONObject jsonObject = JSONObject.parseObject(jsonResp);
        Integer nextPage = jsonObject.getJSONObject("data").getInteger("nextPage");

        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");

        List<TransactionDetail> txnDetailList = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            QueryWrapper<TransactionDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("bankSerialNumber",jsonArray.getJSONObject(i).getString("bankSerialNumber"));
            TransactionDetail obj  = transactionDetailService.getOne(queryWrapper);
            if(obj == null){
                TransactionDetail transactionDetail = new TransactionDetail();

                transactionDetail.setAccountBalance(jsonArray.getJSONObject(i).getString("accountBalance"));
                transactionDetail.setAccountName(jsonArray.getJSONObject(i).getString("accountName"));
                transactionDetail.setAccountNature(jsonArray.getJSONObject(i).getString("accountNature"));
                transactionDetail.setAccountNo(jsonArray.getJSONObject(i).getString("accountNo"));
                transactionDetail.setAccountStatus(jsonArray.getJSONObject(i).getString("accountStatus"));
                transactionDetail.setAssociatedCustomerNumber(jsonArray.getJSONObject(i).getString("associatedCustomerNumber"));
                transactionDetail.setBankSerialNumber(jsonArray.getJSONObject(i).getString("bankSerialNumber"));
                transactionDetail.setBankTransactionDate(jsonArray.getJSONObject(i).getString("bankTransactionDate"));
                transactionDetail.setBankType(jsonArray.getJSONObject(i).getString("bankType"));
                transactionDetail.setBookingDate(jsonArray.getJSONObject(i).getString("bookingDate"));
                transactionDetail.setCheckCode(jsonArray.getJSONObject(i).getString("checkCode"));
                transactionDetail.setCorporateIdentityCode(jsonArray.getJSONObject(i).getString("corporateIdentityCode"));
                transactionDetail.setCurrency(jsonArray.getJSONObject(i).getString("currency"));
                transactionDetail.setDetailSource(jsonArray.getJSONObject(i).getString("detailSource"));
                transactionDetail.setDetailType(jsonArray.getJSONObject(i).getString("detailType"));
                transactionDetail.setDigest(jsonArray.getJSONObject(i).getString("digest"));
                transactionDetail.setErpSerialNumber(jsonArray.getJSONObject(i).getString("erpSerialNumber"));
                transactionDetail.setExtensionMsg(jsonArray.getJSONObject(i).getString("extensionMsg"));
                transactionDetail.setIncurredAmount(jsonArray.getJSONObject(i).getString("incurredAmount"));
                transactionDetail.setLoanType(jsonArray.getJSONObject(i).getString("loanType"));
                transactionDetail.setMerchantName(jsonArray.getJSONObject(i).getString("merchantName"));
                transactionDetail.setMerchantNumber(jsonArray.getJSONObject(i).getString("merchantNumber"));
                transactionDetail.setOpenBank(jsonArray.getJSONObject(i).getString("openBank"));
                transactionDetail.setOppositeAccount(jsonArray.getJSONObject(i).getString("oppositeAccount"));
                transactionDetail.setOppositeName(jsonArray.getJSONObject(i).getString("oppositeName"));
                transactionDetail.setOppositeOpeningBank(jsonArray.getJSONObject(i).getString("oppositeOpeningBank"));
                transactionDetail.setPayApplyRemark1(jsonArray.getJSONObject(i).getString("payApplyRemark1"));
                transactionDetail.setPayApplyRemark2(jsonArray.getJSONObject(i).getString("payApplyRemark2"));
                transactionDetail.setPayApplyRemark3(jsonArray.getJSONObject(i).getString("payApplyRemark3"));
                transactionDetail.setPaymentNature(jsonArray.getJSONObject(i).getString("paymentNature"));
                transactionDetail.setPaymentNatureFlag(jsonArray.getJSONObject(i).getString("paymentNatureFlag"));
                transactionDetail.setPersonalizeField1(jsonArray.getJSONObject(i).getString("personalizeField1"));
                transactionDetail.setPersonalizeField2(jsonArray.getJSONObject(i).getString("personalizeField2"));
                transactionDetail.setPersonalizeField3(jsonArray.getJSONObject(i).getString("personalizeField3"));
                transactionDetail.setPersonalizeField4(jsonArray.getJSONObject(i).getString("personalizeField4"));
                transactionDetail.setPersonalizeField5(jsonArray.getJSONObject(i).getString("personalizeField5"));
                transactionDetail.setPostscript(jsonArray.getJSONObject(i).getString("postscript"));
                transactionDetail.setProtocolType(jsonArray.getJSONObject(i).getString("protocolType"));
                transactionDetail.setProtocolTypeName(jsonArray.getJSONObject(i).getString("protocolTypeName"));
                transactionDetail.setPurpose(jsonArray.getJSONObject(i).getString("purpose"));
                transactionDetail.setRemark(jsonArray.getJSONObject(i).getString("remark"));
                transactionDetail.setReserveField1(jsonArray.getJSONObject(i).getString("reserveField1"));
                transactionDetail.setReserveField2(jsonArray.getJSONObject(i).getString("reserveField2"));
                transactionDetail.setReserveField3(jsonArray.getJSONObject(i).getString("reserveField3"));
                transactionDetail.setReserveField4(jsonArray.getJSONObject(i).getString("reserveField4"));
                transactionDetail.setTransactionCode(jsonArray.getJSONObject(i).getString("transactionCode"));
                transactionDetail.setTransactionSerialNumber(jsonArray.getJSONObject(i).getString("transactionSerialNumber"));
                transactionDetail.setUnitCode(jsonArray.getJSONObject(i).getString("unitCode"));
                transactionDetail.setUnitName(jsonArray.getJSONObject(i).getString("unitName"));
                transactionDetail.setUpdateTime(jsonArray.getJSONObject(i).getString("updateTime"));
                transactionDetail.setValueDate(jsonArray.getJSONObject(i).getString("valueDate"));
                transactionDetail.setVirtualAccount(jsonArray.getJSONObject(i).getString("virtualAccount"));
                transactionDetail.setVirtualAccountName(jsonArray.getJSONObject(i).getString("virtualAccountName"));
                transactionDetail.setVoucherCode(jsonArray.getJSONObject(i).getString("voucherCode"));
                txnDetailList.add(transactionDetail);
            }
        }

        if (txnDetailList.size()>0){
            transactionDetailService.saveBatch(txnDetailList);
        }

        Map<String,Integer> respMap = new HashMap<>();
        respMap.put("nextPage",nextPage);
        return respMap;
    }

}
