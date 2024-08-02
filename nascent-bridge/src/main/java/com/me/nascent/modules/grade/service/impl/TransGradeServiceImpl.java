package com.me.nascent.modules.grade.service.impl;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.grade.service.TransGradeService;
import com.me.nascent.modules.member.service.MemberService;
import com.me.nascent.modules.token.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransGradeServiceImpl implements TransGradeService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;



    @Override
    public void TransGrade() {

    }
}
