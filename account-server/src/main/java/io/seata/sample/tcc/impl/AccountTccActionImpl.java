package io.seata.sample.tcc.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.sample.dao.AccountDao;
import io.seata.sample.entity.Account;
import io.seata.sample.tcc.AccountTccAction;
import io.seata.sample.tcc.ResultHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @description:
 */
@Service
@Slf4j
public class AccountTccActionImpl implements AccountTccAction {
    @Autowired
    AccountDao accountDao;

    /**
     * try 尝试
     *
     * @param businessActionContext
     * @param userId
     * @param money
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean prepareDecreaseMoney(BusinessActionContext businessActionContext, Long userId, BigDecimal money) {
        log.info("减少账户金额，第一阶段锁定金额，userId=" + userId + "， money=" + money);
        Account account = accountDao.findOneByUserId(userId);
        //余额不足，处理
        if (account.getResidue().compareTo(money) < 0) {
            throw new RuntimeException("账户金额不足");
        }
        //冻结钱
        accountDao.updateFrozen(userId, account.getResidue().subtract(money), account.getFrozen().add(money));
        ResultHolder.setResult(getClass(), businessActionContext.getXid(), "p");
        return true;
    }

    /**
     * commit 提交
     *
     * @param businessActionContext
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        long userId = Long.parseLong(businessActionContext.getActionContext("userId").toString());
        BigDecimal money = new BigDecimal(businessActionContext.getActionContext("money").toString());
        log.info("减少账户金额，第二阶段，提交，userId=" + userId + "， money=" + money);

        //防止重复提交
        if (ResultHolder.getResult(getClass(), businessActionContext.getXid()) == null) {
            return true;
        }
        accountDao.updateFrozenToUsed(userId, money);
        //删除标识
        ResultHolder.removeResult(getClass(), businessActionContext.getXid());
        return true;
    }

    /**
     * cancel 撤销
     *
     * @param businessActionContext
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        long userId = Long.parseLong(businessActionContext.getActionContext("userId").toString());
        BigDecimal money = new BigDecimal(businessActionContext.getActionContext("money").toString());

        //防止重复回滚
        if (ResultHolder.getResult(getClass(), businessActionContext.getXid()) == null) {
            return true;
        }
        log.info("减少账户金额，第二阶段，回滚，userId=" + userId + "， money=" + money);
        accountDao.updateFrozenToResidue(userId, money);
        //删除标识
        ResultHolder.removeResult(getClass(), businessActionContext.getXid());
        return true;
    }
}
