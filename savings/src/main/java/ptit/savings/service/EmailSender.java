package ptit.savings.service;

import ptit.savings.model.Account;
import ptit.savings.model.Saving;

public interface EmailSender {
    public void sendEmail(String toEmail,String subject, String body);

    public void newBankAccountEmail(String toEmail, String owner, String stk, String otp);

    public void newBankAccountVerifiedEmail(Account account);

    public void verifySaving(String toEmail, String otp);
    
    public void savingVerified(Saving saving);

    public void verifyWithdrawal(String toEmail, String otp);

    public void withdrawalVerified(Saving saving);
}
