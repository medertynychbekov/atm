import atm.service.AccountService;
import atm.service.impl.AccountServiceImpl;

public class Main {
    public static void main(String[] args) {
        AccountService accountService = new AccountServiceImpl();

        accountService.singUp("Meder", "Tynychbekov");
        accountService.singUp("Meder1", "Tynychbekov1");
        accountService.singUp("Meder", "Tynychpekov");
        while (true) {
            accountService.singIn("Meder", "Tynychbekov");

        }

    }
}