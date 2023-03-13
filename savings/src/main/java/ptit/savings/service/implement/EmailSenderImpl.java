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
    public void newBankAccountEmail(String toEmail, String owner, String stk) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(toEmail);
        msg.setText("Số tài khoản " +stk+ " đã đuợc mở thành công. \nXin hãy đăng ký tài khoản trên ứng dụng web để sử dụng dịch vụ của ngân hàng.");
        msg.setSubject("Chào mừng " + owner);

        mailSender.send(msg);
    }

    @Override
    public void verifyNewClientEmail(String toEmail, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("ptitbankapp@gmail.com");
        msg.setTo(toEmail);
        msg.setText("Nhập mã OTP " + otp + " để xác nhận đăng ký tài khoản");
        msg.setSubject("Xác nhận đăng ký tài khoản");
        mailSender.send(msg);
    }
}
