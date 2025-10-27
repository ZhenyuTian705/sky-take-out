package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    AddressBookMapper addressBookMapper;

    @Override
    @Transactional
    public void addAddress(AddressBook addressBook) {
        if(addressBook.getIsDefault()!=null && addressBook.getIsDefault()==1){
            AddressBook defaultAddressBook = addressBookMapper.getDefaultAddressBook();
            if(defaultAddressBook!=null){
                throw new AddressBookBusinessException("已经有默认地址");
            }
        }
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.insert(addressBook);
    }

    @Override
    public List<AddressBook> getAddressBookByUserId() {


        List<AddressBook> list = addressBookMapper.getAddressBookByUserId(BaseContext.getCurrentId());

        return list;
    }

    @Override
    public AddressBook getDefaultAddress() {
        AddressBook addressBook = addressBookMapper.getDefaultAddressBook();
        return addressBook;
    }

    @Override
    public AddressBook selectById(Long id) {
        AddressBook addressBook = addressBookMapper.selectById(id);
        return addressBook;
    }

    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void setDefault(Long id) {
        AddressBook defaultAddressBook = addressBookMapper.getDefaultAddressBook();
        if(defaultAddressBook!=null) {
            defaultAddressBook.setIsDefault(0);
            addressBookMapper.update(defaultAddressBook);
        }
        AddressBook addressBook = addressBookMapper.selectById(id);
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }
}
