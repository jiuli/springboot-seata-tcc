package io.seata.sample.service;

import io.seata.core.context.RootContext;
import io.seata.sample.dao.AccountDao;
import io.seata.sample.feign.OrderApi;
import io.seata.sample.tcc.AccountTccAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author
 */
@Service("accountServiceImpl")
public class AccountServiceImpl implements AccountService{

    private final AccountTccAction accountTccAction;

    public AccountServiceImpl(AccountTccAction accountTccAction) {
        this.accountTccAction = accountTccAction;
    }

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     */
    @Override
    public void decrease(Long userId, BigDecimal money) {
        accountTccAction.prepareDecreaseMoney(null, userId, money);
    }
}
