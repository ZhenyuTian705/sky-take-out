package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper {



    void insert(AddressBook addressBook);


    @Select("select * from address_book where user_id=#{userId}")
    List<AddressBook> getAddressBookByUserId(Long currentId);

    @Select("select * from address_book where is_default=1")
    AddressBook getDefaultAddressBook();

    @Select("select * from address_book where id=#{id}")
    AddressBook selectById(Long id);

    void update(AddressBook addressBook);

    @Delete("delete from address_book where id=#{id}")
    void deleteById(Long id);


}
