package com.me.modules.assembly.controller;

import com.me.modules.assembly.dto.PutAssemblyDto;
import com.me.modules.tran.service.TranService;
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

    @PostMapping("putHkAssembly")
    public String putHkAssembly(@RequestBody PutAssemblyDto dto) throws Exception {
        return tranService.tranHkAssembly(dto);
    }

    @PostMapping("putTwAssembly")
    public String putTwAssembly(@RequestBody PutAssemblyDto dto) throws Exception {
        return tranService.tranTwAssembly(dto);
    }
}
