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

    // ✅ Helper au niveau classe (pas dans une méthode)
    private static String safe(String s) {
        if (s == null) return "\u2014";          // — (em dash, unicode)
        String t = s.trim();
        return t.isEmpty() ? "\u2014" : t;
    }

    public void sendCredentials(String toEmail,
                                String nom,
                                String prenom,
                                String loginEmail,
                                String password,
                                String nomEntreprise) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Vos identifiants de connexion");

            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                            + "<div style='text-align: center; margin-bottom: 20px;'>"
                            + "<img src='https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Logo_OpenAI.svg/320px-Logo_OpenAI.svg.png' alt='Logo' style='max-height: 80px;'/>"
                            + "</div>"
                            + "<h2 style='color: #2c3e50;'>Bonjour " + safe(prenom) + " " + safe(nom) + ",</h2>"
                            + "<p>Bienvenue chez <strong>" + safe(nomEntreprise) + "</strong> !</p>"
                            + "<p>Voici vos informations de connexion à notre plateforme :</p>"
                            + "<ul>"
                            + "<li><strong>Email :</strong> " + safe(loginEmail) + "</li>"
                            + "<li><strong>Mot de passe :</strong> " + safe(password) + "</li>"
                            + "</ul>"
                            + "<p>Nous vous recommandons de changer votre mot de passe lors de votre première connexion.</p>"
                            + "<br>"
                            + "<p style='color: #888;'>Cordialement,<br>L'équipe " + safe(nomEntreprise) + "</p>"
                            + "</div>";

            helper.setText(htmlContent, true); // true = HTML
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }

    public void sendLeaveRequest(String[] toEmails,
                                 String nomEntreprise,
                                 String employeNomComplet,
                                 String employeEmail,
                                 String dateDebut,
                                 String dateFin,
                                 String dureeTexte,
                                 String motif,
                                 String commentaire) {

        if (toEmails == null || toEmails.length == 0) return; // rien à envoyer

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmails); // ou helper.setBcc(toEmails) si tu préfères BCC
            helper.setSubject("Nouvelle demande de congé - " + safe(nomEntreprise));

            String html =
                    "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                            + "<h2 style='color:#2c3e50;margin:0 0 10px;'>Nouvelle demande de congé</h2>"
                            + "<p>Entreprise : <strong>" + safe(nomEntreprise) + "</strong></p>"
                            + "<table style='border-collapse:collapse;width:100%;max-width:640px'>"
                            + "  <tr>"
                            + "    <td style='padding:8px;border:1px solid #eee;width:200px;background:#fafafa'>Employé</td>"
                            + "    <td style='padding:8px;border:1px solid #eee;'>" + safe(employeNomComplet) + " (" + safe(employeEmail) + ")</td>"
                            + "  </tr>"
                            + "  <tr>"
                            + "    <td style='padding:8px;border:1px solid #eee;background:#fafafa'>Date début</td>"
                            + "    <td style='padding:8px;border:1px solid #eee;'>" + safe(dateDebut) + "</td>"
                            + "  </tr>"
                            + "  <tr>"
                            + "    <td style='padding:8px;border:1px solid #eee;background:#fafafa'>Date fin</td>"
                            + "    <td style='padding:8px;border:1px solid #eee;'>" + safe(dateFin) + "</td>"
                            + "  </tr>"
                            + "  <tr>"
                            + "    <td style='padding:8px;border:1px solid #eee;background:#fafafa'>Durée</td>"
                            + "    <td style='padding:8px;border:1px solid #eee;'>" + safe(dureeTexte) + "</td>"
                            + "  </tr>"
                            + "  <tr>"
                            + "    <td style='padding:8px;border:1px solid #eee;background:#fafafa'>Motif</td>"
                            + "    <td style='padding:8px;border:1px solid #eee;'>" + safe(motif) + "</td>"
                            + "  </tr>"
                            + "  <tr>"
                            + "    <td style='padding:8px;border:1px solid #eee;background:#fafafa'>Commentaire</td>"
                            + "    <td style='padding:8px;border:1px solid #eee;white-space:pre-wrap'>" + safe(commentaire) + "</td>"
                            + "  </tr>"
                            + "</table>"
                            + "<p style='color:#777;margin-top:12px'>Cet email est automatique. Merci de traiter la demande dans l'application.</p>"
                            + "</div>";

            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de demande de congé : " + e.getMessage());
        }
    }
}
