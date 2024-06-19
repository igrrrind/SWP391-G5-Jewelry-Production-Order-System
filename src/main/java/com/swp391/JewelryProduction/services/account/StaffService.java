package com.swp391.JewelryProduction.services.account;


import com.swp391.JewelryProduction.dto.AccountDTO;
import com.swp391.JewelryProduction.enums.Role;
import com.swp391.JewelryProduction.enums.WorkStatus;
import com.swp391.JewelryProduction.pojos.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<Staff> findAllStaff();
    List<Staff> findAllByRole(Role role);

    Staff findStaffById(String staffId);

    Staff updateStaff(AccountDTO accountDTO, WorkStatus workStatus);

    Staff createStaff(AccountDTO accountDTO, WorkStatus workStatus);


    void deleteStaffById(Staff staff);
}
