package com.yonder.vm.service;

import java.util.Scanner;

public class IoService {

    private Scanner scanner;

    public IoService(){
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display a message to the user.
     * @param message
     */
    public void displayMessage(String message){
        System.out.println(message);
    }

    /**
     * Read the input user - an integer value
     * @return
     */
    public int readUserInput(){
        return scanner.nextInt();
    }
}
