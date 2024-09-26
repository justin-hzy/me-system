package com.me.nascent.modules.grade.controller;

import com.me.nascent.modules.grade.service.TransGradeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("grade")
@AllArgsConstructor
public class GradeController {

    public TransGradeService transGradeService;


}
