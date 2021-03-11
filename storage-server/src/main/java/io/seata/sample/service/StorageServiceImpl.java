package io.seata.sample.service;

import io.seata.sample.tcc.StorageTccAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 */
@Slf4j
@Service("storageServiceImpl")
public class StorageServiceImpl implements StorageService {
    @Autowired
    StorageTccAction storageTccAction;

    /**
     * 扣减库存
     * @param productId 产品id
     * @param count 数量
     * @return
     */
    @Override
    public void decrease(Long productId, Integer count) {
        storageTccAction.prepareDecreaseStorage(null,productId,count);
    }
}
