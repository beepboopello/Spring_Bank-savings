package ptit.savings.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ptit.savings.model.Account;
import ptit.savings.model.Saving;
import ptit.savings.service.EmailSender;

@Service
public class EmailSenderImpl implements EmailSender{
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,String subject, String body){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(toEmail);
        msg.setText(body);
        msg.setSubject(subject);
        mailSender.send(msg);
    }

    @Override
    public void newBankAccountEmail(String toEmail, String owner, String stk, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(toEmail);
        msg.setText("Vui lòng cung cấp mã otp : " + otp + " cho nhân viên quầy để hoàn tất việc đăng ký tài khoản.");
        msg.setSubject("Xác thực tạo tài khoản");
        mailSender.send(msg);
    }

    @Override
    public void newBankAccountVerifiedEmail(Account accounts){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(accounts.getEmail());
        msg.setText("Tài khoản có số " + accounts.getStk() + " đã đuợc mở thành công.\n");
        msg.setSubject("Chào mừng " + accounts.getFirst_name());
        mailSender.send(msg);
    }

    @Override
    public void verifySaving(String toEmail, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(toEmail);
        msg.setText("Vui lòng cung cấp mã otp : " + otp + " cho nhân viên quầy để hoàn tất việc mở sổ tiết kiệm");
        msg.setSubject("Xác thực mở sổ tiết kiệm");
        mailSender.send(msg);
    }

    @Override
    public void savingVerified(Saving saving) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(saving.getAccount().getEmail());
        msg.setText("Sổ tiết kiệm có số " + saving.getNumber() + " đã đuợc mở thành công.");
        msg.setSubject("Mở sổ tiết kiệm thành công");
        mailSender.send(msg);
    }

    @Override
    public void verifyWithdrawal(String toEmail, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(toEmail);
        msg.setText("Vui lòng cung cấp mã otp : " + otp + " cho nhân viên quầy để hoàn tất việc rút sổ tiết kiệm");
        msg.setSubject("Xác thực rút sổ tiết kiệm");
        mailSender.send(msg);
    }

    @Override
    public void withdrawalVerified(Saving saving) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(saving.getAccount().getEmail());
        msg.setText("Sổ tiết kiệm có số " + saving.getNumber() + " đã đuợc rút thành công.");
        msg.setSubject("Rút sổ tiết kiệm thành công");
        mailSender.send(msg);
    }


}
