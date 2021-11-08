package com.lizumin.easyimage.mapper;

import com.lizumin.easyimage.model.entity.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:41 am
 * 4
 */
public interface AccountRepository extends CrudRepository<Account,Integer> {
    Account findAccountByEmail(String email);

    Account findAccountByUserid(long id);
}
