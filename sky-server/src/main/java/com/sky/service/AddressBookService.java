package com.sky.service;


import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void addAddress(AddressBook addressBook);

    List<AddressBook> getAddressBookByUserId();

    AddressBook getDefaultAddress();

    AddressBook selectById(Long id);

    void update(AddressBook addressBook);

    void deleteById(Long id);

    void setDefault(Long id);
}
