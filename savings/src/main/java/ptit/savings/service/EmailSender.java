package ptit.savings.service;


public interface EmailSender {
    public void sendEmail(String toEmail,String subject, String body);

    public void newBankAccountEmail(String toEmail,String owner,String stk);

    public void verifyNewClientEmail(String toEmail,String otp);
}
