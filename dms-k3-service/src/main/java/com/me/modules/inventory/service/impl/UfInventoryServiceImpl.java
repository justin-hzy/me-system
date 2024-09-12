package com.me.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.inventory.entity.UfInventory;
import com.me.modules.inventory.mapper.UfInventoryMapper;
import com.me.modules.inventory.service.UfInventoryService;
import com.me.modules.sale.dto.PutSaleReqDto;
import com.me.modules.sale.pojo.SaleFEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UfInventoryServiceImpl extends ServiceImpl<UfInventoryMapper,UfInventory> implements UfInventoryService {

    @Override
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public void updateUfInventory(PutSaleReqDto dto) {
        //仓
        String fstockorgid = dto.getFstockorgid();
        //销售主体
        String fsaleorgid = dto.getFsaleorgid();

        List<SaleFEntity> fentitylist = dto.getFentitylist();

        for (SaleFEntity saleFEntity : fentitylist)
            if("ZT021".equals(fsaleorgid)){
                QueryWrapper<UfInventory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("ckbm","FL003").eq("wlbm",saleFEntity.getFmaterialId());

                UfInventory ufInventory = getOne(queryWrapper);

                if(ufInventory!=null){
                    String frealqty = saleFEntity.getFrealqty();
                    Integer cksl = Integer.valueOf(frealqty);
                    ufInventory.setCksl(ufInventory.getCksl()+cksl);
                    ufInventory.setQmkcsl(ufInventory.getQmkcsl()-cksl);

                    UpdateWrapper<UfInventory> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("ckbm","FL003").eq("wlbm",saleFEntity.getFmaterialId())
                            .set("cksl",ufInventory.getCksl()+cksl)
                            .set("qmkcsl",ufInventory.getQmkcsl()-cksl);

                    update(updateWrapper);
                }
            }else if("ZT026".equals(fstockorgid)){
                QueryWrapper<UfInventory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("ckbm","FL004").eq("wlbm",saleFEntity.getFmaterialId());

                UfInventory ufInventory = getOne(queryWrapper);

                if (ufInventory!=null){
                    String frealqty = saleFEntity.getFrealqty();
                    Integer cksl = Integer.valueOf(frealqty);
                    ufInventory.setCksl(ufInventory.getCksl()+cksl);
                    ufInventory.setQmkcsl(ufInventory.getQmkcsl()-cksl);

                    UpdateWrapper<UfInventory> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("ckbm","FL004").eq("wlbm",saleFEntity.getFmaterialId())
                            .set("cksl",ufInventory.getCksl()+cksl)
                            .set("qmkcsl",ufInventory.getQmkcsl()-cksl);
                    update(updateWrapper);
                }
            }
    }
}
