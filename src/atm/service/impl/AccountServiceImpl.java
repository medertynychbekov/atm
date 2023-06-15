package atm.service.impl;

import atm.dao.AccountDao;
import atm.model.UserAccount;
import atm.service.AccountService;

import java.util.*;

public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao = new AccountDao();
    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void singUp(String name, String lastName) {
        UserAccount userAccount = new UserAccount();

        userAccount.setName(name);
        userAccount.setLastName(lastName);

        int cardNumber = random.nextInt(10_000_000, 99_999_999);
        int pinCode = random.nextInt(1_000, 9_999);

        userAccount.setCardNumber(String.valueOf(cardNumber));
        userAccount.setPinCode(String.valueOf(pinCode));

        accountDao.getUserAccounts().add(userAccount);
        System.out.println(userAccount);

    }

    @Override
    public void singIn(String name, String lastName) {
        int choose = 0;
        try {
            ho:
            for (int i = 0; i < accountDao.getUserAccounts().size(); i++) {
                UserAccount user = accountDao.getUserAccounts().get(i);
                if (user.getName().equals(name) && user.getLastName().equals(lastName)) {

                    String chooseStr = null;
                    System.out.println("Нажмите 1 чтобы увидеть баланс ");
                    System.out.println("Нажмите 2 чтобы пополнить баланс");
                    System.out.println("Нажмите 3 чтобы отправить деньги другу");
                    System.out.println("Нажмите 4 чтобы снять наличные");

                    while (true) {

                        chooseStr = scanner.nextLine();
                        if ("1234".contains(chooseStr) && !chooseStr.equals("")) {
                            choose = Integer.parseInt(chooseStr);
                            break ho;
                        } else if (!chooseStr.equals(""))
                            System.out.println("Ты что, слепой?");
                    }

                }
            }
            switch (choose) {
                case 1 -> {
                    System.out.println("Введите свою карту");
                    String cardNumber = scanner.nextLine();

                    System.out.println("Введите пин код");
                    String pinCode = scanner.nextLine();
                    balance(cardNumber, pinCode);
                }
                case 2 -> {
                    System.out.println("Введите свою карту");
                    String cardNumber = scanner.nextLine();

                    System.out.println("Введите пин код");
                    String pinCode = scanner.nextLine();
                    deposit(cardNumber, pinCode);
                }
                case 3 -> {
                    System.out.println("Напишите карту друга ");
                    String friendCardNumber = scanner.nextLine();
                    sendToFriend(friendCardNumber);
                }
                case 4 -> {
                    System.out.println("Сколько денег хотите снять?");
                    int amount = scanner.nextInt();

                    withdrawMoney(amount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void balance(String cardNumber, String pinCode) {
        try {
            for (int i = 0; i < accountDao.getUserAccounts().size(); i++) {
                UserAccount user = accountDao.getUserAccounts().get(i);

                if (user.getCardNumber().equals(cardNumber) && user.getPinCode().equals(pinCode)) {
                    System.out.println("Ваш счет");
                    System.out.println(user.getBalance());
                } else {
                    throw new Exception("Логин или пароль не правильно!");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deposit(String cardNumber, String pinCode) {
        try {
            UserAccount user = accountDao.getUserByCardNumber(cardNumber);

            if (user != null && user.getPinCode().equals(pinCode)) {
                System.out.println("На какую сумму хотите попонить свой баланс?");
                int cache = scanner.nextInt();

                user.setBalance(cache);
                accountDao.getUserAccounts().add(user);
                System.out.println("User -> " + user);
            } else {
                throw new Exception("Логин или пароль не правильный");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sendToFriend(String friendCardNumber) {
        System.out.println("Введите свою карту");
        String cardNumber = scanner.nextLine();

        System.out.println("Введите пин код");
        String pinCode = scanner.nextLine();

        System.out.println("Сколько денег хотите перевести?");
        int amount = scanner.nextInt();

        UserAccount friend = null;

        try {
            for (int i = 0; i < accountDao.getUserAccounts().size(); i++) {
                UserAccount user = accountDao.getUserAccounts().get(i);

                if (user.getCardNumber().equals(friendCardNumber)) {
                    friend = user;
                }
            }
            
            for (int i = 0; i < accountDao.getUserAccounts().size(); i++) {
                UserAccount user = accountDao.getUserAccounts().get(i);
                if (user.getCardNumber().equals(cardNumber) && user.getPinCode().equals(pinCode)) {
                    user.setBalance(user.getBalance() - amount);
                    accountDao.getUserAccounts().add(user);

                    friend.setBalance(friend.getBalance() + amount);

                    accountDao.getUserAccounts().add(friend);

                    System.out.println("You  -> " + user);
                    System.out.println("Your friend -> " + friend);
                } else {
                    throw new Exception("Логин или пароль неверный");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void withdrawMoney(int amount) {
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("веедите свои данные !");
        String cardNumber = scanner1.nextLine();
        String pinCode = scanner1.nextLine();
        for (UserAccount userAccount1 : accountDao.getUserAccounts()) {
            if (cardNumber.equals(userAccount1.getCardNumber()) && pinCode.equals(userAccount1.getPinCode())) {
                Scanner money = new Scanner(System.in);
                int counter10 = 0;
                for (int i = 0; i < amount; i += 10) {
                    counter10++;
                }

                if (userAccount1.getBalance() > amount) {
                    System.out.println("Чтобы получить " + amount + " по 1000 купюр " + counter10 / 100 + " shtuk нажмите на 1");
                    System.out.println("Чтобы получить " + amount + " по 500 купюр " + counter10 / 50 + " shtuk нажмите на 2");
                    System.out.println("Чтобы получить " + amount + " по 200 купюр " + counter10 / 20 + " shtuk нажмите на 3");
                    System.out.println("Чтобы получить " + amount + " по 100 купюр " + counter10 / 10 + " shtuk нажмите на 4");
                    System.out.println("Чтобы получить " + amount + " по 50 купюр " + counter10 / 5 + " shtuk нажмите на 5");
                    System.out.println("Чтобы получить " + amount + " по 10 купюр " + counter10 + " shtuk нажмите на 6");
                    int akcha = money.nextInt();

                    int balance = (userAccount1.getBalance() - akcha);

                    if (akcha == 1) {
                        System.out.println("ваши деньги " + amount + "som");
                    } else if (akcha == 2) {
                        System.out.println("ваши деньги " + amount + "som");
                    } else if (akcha == 3) {
                        System.out.println("ваши деньги " + amount + "som");
                    } else if (akcha == 4) {
                        System.out.println("ваши деньги " + amount + "som");
                    } else if (akcha == 5) {
                        System.out.println("ваши деньги " + amount + "som");
                    } else if (akcha == 6) {
                        System.out.println("ваши деньги " + amount + "som");
                    }
                    userAccount1.setBalance(balance);
                    System.out.println("Ваш баланс :");
                    System.out.println(userAccount1);
                } else {
                    System.out.println("Не достаточно средств!");
                }
            }

        }
    }
}
