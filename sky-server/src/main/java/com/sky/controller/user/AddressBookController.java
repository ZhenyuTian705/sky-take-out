package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "用户端分类接口")
@RequestMapping("/user/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;


    @PostMapping
    @ApiOperation(value = "新增地址")
    public Result addAddress(@RequestBody AddressBook addressBook){
        addressBookService.addAddress(addressBook);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询当前用户的地址")
    public Result<List<AddressBook>> getAddressBookByUserId(){
        List<AddressBook> list = addressBookService.getAddressBookByUserId();
        return Result.success(list);
    }
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefaultAddress(){
        AddressBook addressbook = addressBookService.getDefaultAddress();
        return Result.success(addressbook);
    }

    @GetMapping("/{id}")
    public Result<AddressBook> selectById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.selectById(id);

        return Result.success(addressBook);
    }

    @PutMapping
    public Result update(@RequestBody AddressBook addressBook){
        addressBookService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteById(Long id){
        addressBookService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook.getId());
        return Result.success();
    }

}
