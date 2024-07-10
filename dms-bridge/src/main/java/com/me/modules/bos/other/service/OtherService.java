package com.me.modules.bos.other.service;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.bos.other.dto.PostOtherInDto;
import com.me.modules.bos.other.dto.PostOtherOutDto;

import java.io.IOException;

public interface OtherService {

    public JSONObject PostOtherIn(PostOtherInDto reqDto);


    public JSONObject postReOther(PostOtherOutDto postOtherOutDto);

    public JSONObject postSetFr(PostOtherInDto reqDto);

    public JSONObject postSetSon(PostOtherOutDto reqDto);

    public JSONObject postDismantleSon(PostOtherInDto reqDto);

    public JSONObject postDismantleFr(PostOtherOutDto reqDto);

    JSONObject postTransCodeFr(PostOtherOutDto reqDto);


    JSONObject postTransCodeSon(PostOtherInDto reqDto);

    void updateOtherLog();
}
