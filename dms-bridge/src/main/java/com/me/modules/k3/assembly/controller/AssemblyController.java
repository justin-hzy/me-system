package com.me.modules.k3.assembly.controller;

import com.me.modules.k3.assembly.dto.PutAssemblyDto;
import com.me.modules.k3.tran.service.TranService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("k3")
@AllArgsConstructor
public class AssemblyController {

    private TranService tranService;

    @PostMapping("putAssembly")
    public String putAssembly(@RequestBody PutAssemblyDto dto) throws Exception {

        return tranService.tranAssembly(dto);
    }
}
