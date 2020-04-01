import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class View extends Frame implements ActionListener {
    public View(){
        SuperShine superShine = new SuperShine();
        WashCard[] washCards = new WashCard[6];
        Customer[] customers = new Customer[6];
        ArrayList<String> arr = new ArrayList<>();
        Statistics stats = new Statistics(arr);

        //sætter vaskepriserne samt rabatpriserne
        double economyWash = 50;
        double economyDiscount = economyWash*0.8;
        double standardWash = 80;
        double standardDiscount = standardWash*0.8;


        //opretter WashCards
        washCards[0] = new WashCard(1, 1000, true);
        washCards[1] = new WashCard(2, 200, false);
        washCards[2] = new WashCard(3, 400, false);
        washCards[3] = new WashCard(4, 600, false);
        washCards[4] = new WashCard(5, 800, false);
        washCards[5] = new WashCard(6, 1000, false);

        //opretter customers og tildeler dem WashCards
        customers[0] = new Customer("Janus Pedersen", washCards[0], washCards[0].getMoney());
        customers[1] = new Customer("Hardy Akira", washCards[1], washCards[1].getMoney());
        customers[2] = new Customer("Oskar Tuska", washCards[2], washCards[2].getMoney());
        customers[3] = new Customer("Amir Khaled", washCards[3], washCards[3].getMoney());
        customers[4] = new Customer("Rasmus Møller", washCards[4], washCards[4].getMoney());
        customers[5] = new Customer("Rasmus Trap", washCards[5], washCards[5].getMoney());

        // Sætter tiden til for rabat til 14 og en boolean der tjekker om klokken er før 14.
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        boolean beforeTwo = Calendar.getInstance().before(cal);
        // da der kaldes getInstance() på Calender ovenfor, kan denne ikke være dynamisk og derfor benyttes timeStamp nedenfor som dato og tid i statistikken
        String timeStamp = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(LocalDateTime.now());


        Label welcomeText = new Label();
        Label welcomeText2 = new Label();
        welcomeText.setText("Hej og velkommen til SuperShine. Vi er en automatisk vaskehal.");
        welcomeText2.setText("Indsæt dit WashCard for at fortsætte (kort ID)");
        welcomeText.setBounds(30,40,1000,30);
        welcomeText2.setBounds(30,60,1000,30);
        add(welcomeText);
        add(welcomeText2);

        TextField idTextField = new TextField("");
        idTextField.setBounds(30, 100, 250,20);
        add(idTextField);

        TextField chosenAction = new TextField("");
        chosenAction.setBounds(30, 100, 250,20);
        //add(chosenAction);



        Label purchaseLabel = new Label();
        Label giftPurchase = new Label();
        Label receiptLabel = new Label();


        purchaseLabel.setText("Købet er gennemført, vil du have en kvittering?");
        giftPurchase.setText("Da dette er din 10. vask hos os, modtager du hermed én gratis biografbillet. Kontakt os for at modtage den");
        purchaseLabel.setBounds(30,150, 400,30);
        giftPurchase.setBounds(30,200, 400,30);
        receiptLabel.setBounds(30,175,400,30);




        Button b=new Button("Næste");
        b.setBounds(400,400,80,30);
        add(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (WashCard washCard : washCards) {
                    if (Integer.parseInt(idTextField.getText()) == washCard.getCardID() && washCard.isAdminStatus()) {
                            welcomeText.setText("Du har nu følgende valgmuligheder:");
                            welcomeText2.setText("[1] Vælg bil vask - [2] Tank kort op - [3] Se balance - [4] Se statistik");
                            b.setVisible(false);
                            idTextField.setVisible(false);
                            add(chosenAction);
                            //remove(idTextField);
                    } else if (Integer.parseInt(idTextField.getText()) == washCard.getCardID() && !washCard.isAdminStatus()) {
                        welcomeText.setText("Du har nu følgende valgmuligheder:");
                        welcomeText2.setText("[1] Vælg bil vask - [2] Tank kort op - [3] Se balance");
                        b.setVisible(false);
                        idTextField.setVisible(false);
                        add(chosenAction);
                        //remove(idTextField);
                    }
                }
            }
        });

        Button b1=new Button("Næste");
        b1.setBounds(400,400,80,30);
        add(b1);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chosenAction.getText().equals("1")){
                    welcomeText2.setVisible(false);
                    chosenAction.setVisible(false);
                    welcomeText.setText("Vi hos SuperShine kan tilbyde dig tre forskellige slags vaske:");
                    Checkbox checkbox1 = new Checkbox("Economy (50 kr)");
                    checkbox1.setBounds(30,70, 150,30);
                    Checkbox checkbox2 = new Checkbox("Standard (80 kr)");
                    checkbox2.setBounds(30,95, 150,30);
                    Checkbox checkbox3 = new Checkbox("De Luxe (120 kr)");
                    checkbox3.setBounds(30,120, 150,30);
                    add(checkbox1);
                    add(checkbox2);
                    add(checkbox3);

                    checkbox1.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                        if (checkbox1.getState()) {
                            if (washCards[Integer.parseInt(idTextField.getText()) - 1].getMoney() > economyWash) {
                                if (beforeTwo) {
                                    washCards[Integer.parseInt(idTextField.getText()) - 1].setMoney(washCards[Integer.parseInt(idTextField.getText())  - 1].getMoney() - economyDiscount);
                                    arr.add("-----\n" + timeStamp + "\nWashType: Economy (discount)\n" + "ID: " + washCards[Integer.parseInt(idTextField.getText()) - 1].getCardID() + ", Name: " + customers[Integer.parseInt(idTextField.getText()) - 1].getName() + "\n-----");
                                    checkbox2.setVisible(false);
                                    checkbox3.setVisible(false);
                                    add(purchaseLabel);
                                    receiptLabel.setText("Du har nu: " +  washCards[Integer.parseInt(idTextField.getText()) - 1].getMoney() + " tilbage på dit WashCard");
                                    add(receiptLabel);
                                    try {
                                        if(stats.readAmountOfWashes(Integer.parseInt(idTextField.getText()) - 1) % 9 == 0 && stats.readAmountOfWashes(Integer.parseInt(idTextField.getText()) - 1) != 0 ){
                                            add(giftPurchase);
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    washCards[Integer.parseInt(idTextField.getText()) - 1].setMoney(washCards[Integer.parseInt(idTextField.getText()) - 1].getMoney() - economyWash);
                                    welcomeText.setText("penge: " + washCards[Integer.parseInt(idTextField.getText()) - 1].getMoney());
                                    arr.add("-----\n" + timeStamp + "\nWashType: Economy (no discount)\n" + "ID: " + washCards[Integer.parseInt(idTextField.getText()) - 1].getCardID() + ", Name: " + customers[Integer.parseInt(idTextField.getText()) - 1].getName() + "\n-----");
                                }
                            }
                        }
                    }
                });
                    try {
                        stats.writeToFile(arr);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                }else if(chosenAction.getText().equals("2")){
                    welcomeText.setText("Hvor mange penge vil du fylde på dit WashCard? (max 1000)");
                    welcomeText2.setVisible(false);
                    b1.setVisible(false);
                    Button b2=new Button("Indsæt");
                    b2.setBounds(400,400,80,30);
                    add(b2);
                    b2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            welcomeText.setText("Pengene er nu indsat på dit WashCard");
                        }
                    });

                }else if(chosenAction.getText().equals("3")){
                    welcomeText.setText("På dit WashCard står der: " /*+ washCards[Integer.parseInt(textField.getText())].getMoney()*/);
                    welcomeText2.setVisible(false);

                }else if(chosenAction.getText().equals("4")){

                }
            }
        });

        setSize(500,500);
        setLayout(null);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
