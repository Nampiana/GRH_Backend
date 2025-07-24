package app.gestion.GRH.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendCredentials(String toEmail, String nom, String prenom, String loginEmail, String password, String nomEntreprise) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Vos identifiants de connexion");

            String htmlContent = "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                    + "<div style='text-align: center; margin-bottom: 20px;'>"
                    + "<img src='https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Logo_OpenAI.svg/320px-Logo_OpenAI.svg.png' alt='Logo' style='max-height: 80px;'/>"
                    + "</div>"
                    + "<h2 style='color: #2c3e50;'>Bonjour " + prenom + " " + nom + ",</h2>"
                    + "<p>Bienvenue chez <strong>" + nomEntreprise + "</strong> !</p>"
                    + "<p>Voici vos informations de connexion à notre plateforme :</p>"
                    + "<ul>"
                    + "<li><strong>Email :</strong> " + loginEmail + "</li>"
                    + "<li><strong>Mot de passe :</strong> " + password + "</li>"
                    + "</ul>"
                    + "<p>Nous vous recommandons de changer votre mot de passe lors de votre première connexion.</p>"
                    + "<br>"
                    + "<p style='color: #888;'>Cordialement,<br>L'équipe " + nomEntreprise + "</p>"
                    + "</div>";

            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}
