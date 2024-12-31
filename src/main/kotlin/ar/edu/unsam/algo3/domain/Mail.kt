package ar.edu.unsam.algo3.domain

interface MailSender {
  fun mandarMail( mail : Mail)
}

data class Mail(val remitente: String, val destinatario: String, val asunto: String, val cuerpo: String)

