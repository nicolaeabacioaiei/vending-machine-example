package com.yonder.vm.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yonder.vm.VendingMachine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Simulates a service to a database.
 * Class that is used to interact with the file where the state of the VM is saved.
 */
public class DbService {
    private Gson gson;

    public DbService() {
        gson = new GsonBuilder().enableComplexMapKeySerialization()
                .setPrettyPrinting().create();
    }

    public void write(VendingMachine vm){
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream("D:/VendingMachine.json"), "UTF-8");
        }catch (Exception e){
            System.out.println("Failed create/write file." + e);
        }
        gson.toJson(vm, writer);
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed close file." + e);
        }
    }

    /**
     * Read the file and initializes a VendingMachine with that data.
     * @return
     */
    public VendingMachine read(){
        VendingMachine vm = null;
        try {
            vm = gson.fromJson(new FileReader("D:/VendingMachine.json"), VendingMachine.class);
        } catch (FileNotFoundException e) {
            System.out.println("Failed to read file." + e);
        }
        return vm;
    }
}
