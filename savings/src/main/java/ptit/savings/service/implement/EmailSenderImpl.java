package ptit.savings.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
        msg.setText("Số tài khoản " +stk+ " đã đuợc mở thành công. \nVui lòng cung cấp mã otp : " + otp + " cho nhân viên quầy để hoàn tất việc đăng ký tài khoản.");
        msg.setSubject("Chào mừng " + owner);
        mailSender.send(msg);
    }

    @Override
    public void verifySaving(String toEmail, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(toEmail);
        msg.setText("Vui lòng cung cấp mã otp : " + otp + " cho nhân viên quầy để hoàn tất việc mở sổ tiết kiệm");
        msg.setSubject("Xác thực sổ tiết kiệm");
        mailSender.send(msg);
    }


}
