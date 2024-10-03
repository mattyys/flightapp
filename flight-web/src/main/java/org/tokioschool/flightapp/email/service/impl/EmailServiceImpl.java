package org.tokioschool.flightapp.email.service.impl;

import jakarta.activation.DataHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.email.dto.EmailDTO;
import org.tokioschool.flightapp.email.service.EmailService;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

  // bean que  nos permite utilizar todas las funcionalidad para manejar emails
  private final JavaMailSender javaMailSender;

  @Override
  public void sendEmail(EmailDTO emailDTO) {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    // CREAR EL EMAIL
    try {
      mimeMessage.addRecipients(
          MimeMessage.RecipientType.TO, emailDTO.getTo()); // se agrega en to del mensaje
      mimeMessage.setSubject(
          emailDTO.getSubject(),
          StandardCharsets.UTF_8.name()); // se agrega el asunto y se indica codificacion

      // contenido o body del email
      Multipart multipart = new MimeMultipart();
      mimeMessage.setContent(multipart);

      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setText(emailDTO.getTextBody(), StandardCharsets.UTF_8.name());
      multipart.addBodyPart(messageBodyPart);

      // attachments o adjuntos
      emailDTO
          .getAttachments()
          .forEach(
              attachmentDTO -> {
                try {
                  MimeBodyPart mimeBodyPart = new MimeBodyPart();
                  mimeBodyPart.setDataHandler(
                      new DataHandler( // un wrapper para el array de bytes proporcuionado por
                                       // jakarta
                          new ByteArrayDataSource( // el adjunto es el bytearray
                              attachmentDTO.getContent(), attachmentDTO.getContentType())));
                  mimeBodyPart.setFileName(attachmentDTO.getFilename());
                  multipart.addBodyPart(mimeBodyPart);
                } catch (MessagingException e) {
                  log.error(
                      "Exception when adding attachment to mimeMessage, will be ignored, to:{}, subject:{}",
                      emailDTO.getTo(),
                      emailDTO.getSubject());
                }
              });

    } catch (MessagingException e) {
      log.error(
          "Exception when building mimeMessage, to:{} subject:{}",
          emailDTO.getTo(),
          emailDTO.getSubject(),
          e);
      return;
    }

    // ENVIO DE EMAIL

    try {
      javaMailSender.send(mimeMessage);
    } catch (MailException e) {
      log.error(
          "Exception when sending mimeMessage, to:{} subject:{}",
          emailDTO.getTo(),
          emailDTO.getSubject(),
          e);
    }
  }
}
