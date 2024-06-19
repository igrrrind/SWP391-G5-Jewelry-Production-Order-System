package com.swp391.JewelryProduction.services.account;

import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.enums.WorkStatus;
import com.swp391.JewelryProduction.pojos.Account;
import com.swp391.JewelryProduction.pojos.Staff;
import com.swp391.JewelryProduction.pojos.UserInfo;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> findAllAccounts();
    Account saveAccountIfNew(AccountDTO accountDTO);
    Account saveUserInfo(UserInfo info, String email);
    Account findAccountById(String accountId);
    Account findAccountByEmailAndPassword(String email, String password);
    Optional<Account> updateAccount(AccountDTO accountDTO);
    Account findAccountByEmail(String email);
    Optional<Account> updateAccountStatusActive (String email);
    UserInfo findInfoById(String id);
    UserInfo findInfoByEmail(String email);
    Account findAccountByRole(Role role);
    void saveAccountPassword(AccountDTO accountDTO, String newPassword);
    Staff findStaffByRoleAndWorkStatus(Role role, WorkStatus workStatus);
    List<Account> findAllByRole(Role role);
    void deleteAccount(String accountID);
}
