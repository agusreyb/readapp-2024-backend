package ar.edu.unsam.algo3.bootstrap

import ar.edu.unsam.algo3.domain.Mail
import ar.edu.unsam.algo3.domain.MailSender
import org.springframework.stereotype.Component

@Component
class MailSenderStub : MailSender {
    override fun mandarMail(mail: Mail) {
        println("Enviando correo.")
    }
}
