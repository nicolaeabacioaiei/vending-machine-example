package com.yonder.vm;

import com.yonder.vm.service.DbService;
import com.yonder.vm.service.IoService;

public class Main {

    public static void main(String[] args){
        DbService dbService = new DbService();
        IoService ioService = new IoService();

        VendingMachine vm = dbService.read();

        vm.setDbService(dbService);
        vm.setIoService(ioService);
        vm.start();
    }
}
