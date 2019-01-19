package com.yonder.vm;

import com.yonder.vm.model.Coin;
import com.yonder.vm.model.CurrencyType;
import com.yonder.vm.model.Product;
import com.yonder.vm.service.DbService;
import com.yonder.vm.service.IoService;

import java.util.*;

public class VendingMachine {

    private CurrencyType currency;
    private Map<Product, Integer> productStock;
    private TreeMap<Coin, Integer> coinStock;

    private transient IoService ioService;
    private transient DbService dbService;


    public VendingMachine(){}

    /**
     * Display the menu products to the user.
     */
    public void displayMenu() {
        ioService.displayMessage("Bine ai venit!");
        ioService.displayMessage("Cod  Produs\t Pret \tGramaj");
        for (Product product : productStock.keySet()) {
            ioService.displayMessage(product.getCod() + "\t" + product.getName() + "\t\t" + product.getPrice()+currency + "\t" + product.getSize());
        }
        ioService.displayMessage("0 - iesire");
    }

    /**
     * Display the coin menu to the user in order to select what coins to introduce.
     */
    public void displayCoinStock() {
        ioService.displayMessage("Cod  Valoare");
        for (Coin coin : coinStock.keySet()) {
            ioService.displayMessage(coin.getCod() + "  " + coin.getValue()+currency);
        }
    }

    /**
     * Deliver the product to the user.
     * @param product
     */
    public void deliverProduct(Product product) {
        productStock.put(product, productStock.get(product) - 1);
        ioService.displayMessage("Puteti ridica produsul!");
    }

    /**
     * Insert the coins needed to buy the selected product.
     * @param productPrice
     * @return the sum that was introduced by the user
     */
    public Integer insertCoins(Integer productPrice) {
        Integer sum = 0;
        int option;
        int toPay;
        boolean ok;
        while (sum < productPrice) {
            ioService.displayMessage("Introdu monezi:");
            ok = false;
            option = ioService.readUserInput();
            for (Coin coin : coinStock.keySet()) {
                if (coin.getCod() == option) {
                    coinStock.put(coin, 1 + coinStock.get(coin));
                    sum = sum + coin.getValue();
                    ioService.displayMessage("Suma introdusa: " + sum + " " + currency);
                    toPay = productPrice - sum;
                    ioService.displayMessage("Ramas de introdus: " + (toPay > 0 ? toPay : 0) + " " + currency);
                    ok = true;
                }
            }
            if (ok == false) {
                ioService.displayMessage("Optiunea introdusa nu este valida.");
            }
        }
        return sum;
    }

    /**
     * User selects a product to buy.
     * @return the selected product
     */
    public Product buyProduct() {
        ioService.displayMessage("Alege un produs:");
        int option = ioService.readUserInput();
        boolean ok = false;

        if (option == 0) {
            this.shutDown();
        }
        for (Product p : productStock.keySet()) {
            if (p.getCod() == option) {
                Integer quantity = productStock.get(p);
                if (quantity > 0) {
                    return p;
                } else {
                    ioService.displayMessage("Nu sunt produse suficiente.");
                    return this.buyProduct();
                }
            }
        }
        if (ok == false) {
            ioService.displayMessage("Optiunea introdusa nu este valida.");
            return this.buyProduct();
        }
        return null;
    }

    /**
     * Pay the rest to the user.
     * @param rest
     */
    public void payRest(Integer rest) {
        if(rest == 0){
            ioService.displayMessage("Nu este rest de dat.");
        }else {
            for (Coin coin : coinStock.keySet()) {
                while (coin.getValue() <= rest) {
                    if (coinStock.get(coin) > 0) {
                        ioService.displayMessage("Paying rest " + coin.getValue() + " " + currency);
                        coinStock.put(coin, coinStock.get(coin) - 1);
                        rest = rest - coin.getValue();
                    } else {
                        break;
                    }
                }
            }
            if (rest == 0) {
                ioService.displayMessage("Rest dat cu succes!");
            } else {
                ioService.displayMessage("Nu sunt destule monede pentru rest");
                ioService.displayMessage("Rest ramas: " + rest);
            }
        }
    }

    /**
     * Starting point of the Vending Machine.
     */
    public void start() {
        while (true) {
            this.displayMenu();
            Product product = this.buyProduct();
            this.displayCoinStock();
            Integer sum = this.insertCoins(product.getPrice());
            this.deliverProduct(product);
            //buy another product
            this.payRest(sum - product.getPrice());
            //save the VM
            dbService.write(this);
        }
    }

    /**
     * Shut down the Vending Machine
     */
    public void shutDown(){
        System.exit(0);
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public Map<Product, Integer> getProductStock() {
        return productStock;
    }

    public Map<Coin, Integer> getCoinStock() {
        return coinStock;
    }

    public void setIoService(IoService ioService) {
        this.ioService = ioService;
    }

    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }
}
