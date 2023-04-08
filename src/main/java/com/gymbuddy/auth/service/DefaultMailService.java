package com.gymbuddy.auth.service;

import com.gymbuddy.auth.util.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.gymbuddy.auth.exception.Errors.WRONG_EMAIL_TYPE;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMailService implements MailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    private final JavaMailSender javaMailSender;

    @Override
    @SneakyThrows(MessagingException.class)
    public void sendEmail(final String applicationUrl,
                          final String token,
                          final String toEmail,
                          final String username,
                          final EmailType emailType) {
        final String url;
        final String emailText;
        final String subject;

        // deciding which email to be sent
        if (EmailType.REGISTRATION_EMAIL == emailType) {
            url = applicationUrl +
                    "/auth/verify-registration?token=" + token;
            emailText = getRegistrationEmailText(url, username);
            subject = "New registration verification";
        } else if (EmailType.RESET_PASSWORD_EMAIL == emailType) {
            url = applicationUrl +
                    "/auth/save-password?token=" + token;
            subject = "Password reset";
            emailText = getNewPasswordEmailText(url, username);
        } else {
            url = applicationUrl;
            subject = null;
            emailText = null;
            log.error(WRONG_EMAIL_TYPE);
        }
        log.info("Email sending in progress! URL: {}", url);
        if (emailText != null) {
            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(toEmail);
            messageHelper.setSubject(subject);

            mimeMessage.setContent(emailText, "text/html; charset=ISO-8859-2");
            javaMailSender.send(mimeMessage);
            log.info("{} mail sent!", emailType.name());
        }
    }

    private String getRegistrationEmailText(final String url, final String username) {
        return
                "<!DOCTYPE html>\n" +
                        "<html lang=\"hu\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"iso-8859-2\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"x-apple-disable-message-reformatting\">\n" +
                        "    <title></title>\n" +
                        "    \n" +
                        "    <link href=\"https://fonts.googleapis.com/css?family=Roboto:400,600\" rel=\"stylesheet\" type=\"text/css\">\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "    <style>\n" +
                        "        html,\n" +
                        "        body {\n" +
                        "            margin: 0 auto !important;\n" +
                        "            padding: 0 !important;\n" +
                        "            height: 100% !important;\n" +
                        "            width: 100% !important;\n" +
                        "            font-family: 'Roboto', sans-serif !important;\n" +
                        "            font-size: 14px;\n" +
                        "            margin-bottom: 10px;\n" +
                        "            line-height: 24px;\n" +
                        "            color: #8094ae;\n" +
                        "            font-weight: 400;\n" +
                        "        }\n" +
                        "        * {\n" +
                        "            -ms-text-size-adjust: 100%;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        table,\n" +
                        "        td {\n" +
                        "            mso-table-lspace: 0pt !important;\n" +
                        "            mso-table-rspace: 0pt !important;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            border-spacing: 0 !important;\n" +
                        "            border-collapse: collapse !important;\n" +
                        "            table-layout: fixed !important;\n" +
                        "            margin: 0 auto !important;\n" +
                        "        }\n" +
                        "        table table table {\n" +
                        "            table-layout: auto;\n" +
                        "        }\n" +
                        "        a {\n" +
                        "            colour: inherit;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "        img {\n" +
                        "            -ms-interpolation-mode:bicubic;\n" +
                        "        }\n" +
                        " .style1 {\n" +
                        "\tfont-size: 14px;\n" +
                        "\tcolor: #6576FF;\n" +
                        "}\n" +
                        ".style2 {\n" +
                        "\tfont-weight: 600;\n" +
                        "\tfont-size: 18px;\n" +
                        "\tcolor: #15718A;\n" +
                        "\ttext-align: center;\n" +
                        "}\n" +
                        "    .style3 {\n" +
                        "\tfont-weight: 600;\n" +
                        "\tfont-size: 13px;\n" +
                        "\tcolor: #FFFFFF;\n" +
                        "\ttext-transform: uppercase;\n" +
                        "\tmargin-left: 0;\n" +
                        "}\n" +
                        ".style4 {\n" +
                        "\tfont-size: 13px;\n" +
                        "\tcolor: #9EA8BB;\n" +
                        "\ttext-align: center;\n" +
                        "}\n" +
                        "    .style5 {\n" +
                        "\ttext-align: center;\n" +
                        "}\n" +
                        "    </style>\n" +
                        "\n" +
                        "\n" +
                        "</head>\n" +
                        "\n" +
                        "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f5f6fa;\">\n" +
                        "\t<br>\n" +
                        "\t<center style=\"width: 100%; background-color: #f5f6fa;\">\n" +
                        "        <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#f5f6fa\">\n" +
                        "            <tr>\n" +
                        "               <td style=\"padding: 40px 0;\">\n" +
                        "                    <table style=\"width:100%;max-width:620px;margin:0 auto;\">\n" +
                        "                        <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"text-align: center; padding-bottom:25px\">\n" +
                        "\t\t\t\t\t\t\t\t\t<img alt=\"\" src=\"https://i.ibb.co/9ZgG8kx/logo.png\" width=\"80\" height=\"80\">\n" +
                        "                                    <p style=\"padding-top: 12px; color: #15718a;\" class=\"style1\">\n" +
                        "\t\t\t\t\t\t\t\t\tMyFinance költségnyilvántartó app</p>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                        </tbody>\n" +
                        "                    </table>\n" +
                        "                    <table style=\"width:100%;max-width:620px;margin:0 auto;background-color:#ffffff;\">\n" +
                        "                        <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"padding: 30px 30px 15px 30px;\">\n" +
                        "                                    <p style=\"margin: 0; color: #15718a;\" class=\"style2\">\n" +
                        "\t\t\t\t\t\t\t\t\t<img alt=\"\" src=\"https://novella.jcdecaux.hu/UploadedImages/ikon-reg.png\" width=\"250\" height=\"250\"></p>\n" +
                        "                                    <h2 style=\"margin: 0; color: #15718a;\" class=\"style2\">\n" +
                        "\t\t\t\t\t\t\t\t\tRegisztráció megerősítése</h2>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"padding: 0 30px 20px\">\n" +
                        "                                    <p style=\"margin-bottom: 10px;\" class=\"style5\">Kedves \n" +
                        "\t\t\t\t\t\t\t\t\t" + username + "</p>\n" +
                        "\t\t\t\t\t\t\t\t\t<p style=\"margin-bottom: 10px;\" class=\"style5\">Köszönjük, hogy regisztrált \n" +
                        "\t\t\t\t\t\t\t\t\taz alkalmazásunkban.</p>\n" +
                        "\t\t\t\t\t\t\t\t\t<p style=\"margin-bottom: 10px;\" class=\"style5\">&nbsp;Az \n" +
                        "\t\t\t\t\t\t\t\t\talábbi gombra kattintva aktiválhatja \n" +
                        "\t\t\t\t\t\t\t\t\tfelhasználói fiókját: </p>\n" +
                        "\t\t\t\t\t\t\t\t\t<p style=\"margin-bottom: 10px;\" class=\"style5\">\n" +
                        "\t\t\t\t\t\t\t\t\t&nbsp;</p>\n" +
                        "\t\t\t\t\t\t\t\t\t<div class=\"style5\">\n" +
                        "\t\t\t\t\t\t\t\t\t&nbsp;<span style=\"width: 18%; background-color: #f5f6fa;\"><a href=\"" + url + "\" style=\"background-color:#15718a; border-radius:150px;display:inline-block;line-height:40px; text-align:center;padding: 0 30px; height: 41px;\" class=\"style3\"><div class=\"style5\">\n" +
                        "\t\t\t\t\t\t\t\t\tREGISZTRÁCIÓ MEGERŐSÍTÉSE</div>\n" +
                        "\t\t\t\t\t\t\t\t\t</a>\n" +
                        "    </span>\n" +
                        "</div>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"padding: 20px 30px 40px\">\n" +
                        "                                    <p class=\"style5\">Amennyiben nem Ön regisztrált,&nbsp;hagyja&nbsp;ezt \n" +
                        "\t\t\t\t\t\t\t\t\ta levelet&nbsp;figyelmen&nbsp;kívül. </p>\n" +
                        "\t\t\t\t\t\t\t\t\t<p>&nbsp;</p>\n" +
                        "                                    <p style=\"margin: 0; font-size: 13px; line-height: 22px; color:#9ea8bb;\">\n" +
                        "\t\t\t\t\t\t\t\t\t&nbsp;</p>\n" +
                        "\t\t\t\t\t\t\t\t\t<p style=\"margin: 0; line-height: 22px; \" class=\"style4\">\n" +
                        "\t\t\t\t\t\t\t\t\tEz egy automatikusan generált email, kérjük \n" +
                        "\t\t\t\t\t\t\t\t\tne válaszoljon rá!</p>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                        </tbody>\n" +
                        "                    </table>\n" +
                        "                    <table style=\"width:100%;max-width:620px;margin:0 auto;\">\n" +
                        "                        <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"text-align: center; padding:25px 20px 0;\">\n" +
                        "                                    <p style=\"font-size: 13px;\">2022.&nbsp; \n" +
                        "\t\t\t\t\t\t\t\t\tMyfinance app . Minden jog fenntartva.</p>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                        </tbody>\n" +
                        "                    </table>\n" +
                        "               </td>\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "    </center>\n" +
                        "</body>\n" +
                        "</html>";
    }

    private String getNewPasswordEmailText(final String url, final String username) {
        return
                "<!DOCTYPE html>\n" +
                        "<html lang=\"hu\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"iso-8859-2\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"x-apple-disable-message-reformatting\">\n" +
                        "    <title></title>\n" +
                        "    \n" +
                        "    <link href=\"https://fonts.googleapis.com/css?family=Roboto:400,600\" rel=\"stylesheet\" type=\"text/css\">\n" +
                        "\n" +
                        "    <style>\n" +
                        "        html,\n" +
                        "        body {\n" +
                        "            margin: 0 auto !important;\n" +
                        "            padding: 0 !important;\n" +
                        "            height: 100% !important;\n" +
                        "            width: 100% !important;\n" +
                        "            font-family: 'Roboto', sans-serif !important;\n" +
                        "            font-size: 14px;\n" +
                        "            margin-bottom: 10px;\n" +
                        "            line-height: 24px;\n" +
                        "            color: #8094ae;\n" +
                        "            font-weight: 400;\n" +
                        "        }\n" +
                        "        * {\n" +
                        "            -ms-text-size-adjust: 100%;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        table,\n" +
                        "        td {\n" +
                        "            mso-table-lspace: 0pt !important;\n" +
                        "            mso-table-rspace: 0pt !important;\n" +
                        "        }\n" +
                        "        table {\n" +
                        "            border-spacing: 0 !important;\n" +
                        "            border-collapse: collapse !important;\n" +
                        "            table-layout: fixed !important;\n" +
                        "            margin: 0 auto !important;\n" +
                        "        }\n" +
                        "        table table table {\n" +
                        "            table-layout: auto;\n" +
                        "        }\n" +
                        "        a {\n" +
                        "            colour: inherit;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "        img {\n" +
                        "            -ms-interpolation-mode:bicubic;\n" +
                        "        }\n" +
                        "    .style1 {\n" +
                        "\tfont-size: 14px;\n" +
                        "\tcolor: #6576FF;\n" +
                        "}\n" +
                        ".style2 {\n" +
                        "\tfont-weight: 600;\n" +
                        "\tfont-size: 18px;\n" +
                        "\tcolor: #15718A;\n" +
                        "\ttext-align: center;\n" +
                        "}\n" +
                        "    .style3 {\n" +
                        "\tfont-weight: 600;\n" +
                        "\tfont-size: 13px;\n" +
                        "\tcolor: #FFFFFF;\n" +
                        "\ttext-transform: uppercase;\n" +
                        "\tmargin-left: 0;\n" +
                        "}\n" +
                        ".style4 {\n" +
                        "\tfont-size: 13px;\n" +
                        "\tcolor: #9EA8BB;\n" +
                        "\ttext-align: center;\n" +
                        "}\n" +
                        "    .style5 {\n" +
                        "\ttext-align: center;\n" +
                        "}\n" +
                        ".style6 {\n" +
                        "\tmargin-left: 150px;\n" +
                        "\tmargin-right: 150px;\n" +
                        "}\n" +
                        "    .style7 {\n" +
                        "\ttext-align: left;\n" +
                        "}\n" +
                        "    </style>\n" +
                        "\n" +
                        "</head>\n" +
                        "\n" +
                        "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f5f6fa;\">\n" +
                        "\t<span style=\"width: 100%; background-color: #f5f6fa;\">\n" +
                        "        <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#f5f6fa\">\n" +
                        "            <tr>\n" +
                        "               <td style=\"padding: 40px 0;\">\n" +
                        "                    <table style=\"width:100%;max-width:620px;margin:0 auto;\">\n" +
                        "                        <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"text-align: center; padding-bottom:25px\">\n" +
                        "                                    <a href=\"#\">\n" +
                        "\t\t\t\t\t\t\t\t\t<img alt=\"\" src=\"https://i.ibb.co/9ZgG8kx/logo.png\" width=\"80\" height=\"80\">\n" +
                        "                                    <p style=\"padding-top: 12px; color: #15718a;\" class=\"style1\">\n" +
                        "\t\t\t\t\t\t\t\t\tMyFinance költségnyilvántartó app</p>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                        </tbody>\n" +
                        "                    </table>\n" +
                        "                    <table style=\"width:100%;max-width:620px;margin:0 auto;background-color:#ffffff;\">\n" +
                        "                        <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"padding: 30px 30px 15px 30px;\">\n" +
                        "                                <img src=\"https://ci3.googleusercontent.com/proxy/TMnYbMNa-oAYoT4wJf48Nu0TW6AYDQ1OfAXVjRKo7xbAMNtj0WrclZhjf4TRtmd4DCqkj1jMNMNDJlfLOdJKYG1RH5yL7XOQNssfGTs5c3YIx0HJzO-Liw79Hg=s0-d-e1-ft#https://puregoldprotein.s3.eu-central-1.amazonaws.com/email/password.png\" style=\"width:256px; height:256px\" class=\"style6\" data-bit=\"iit\">\n" +
                        "                                    <h2 style=\"margin: 0; \" class=\"style2\">\n" +
                        "\t\t\t\t\t\t\t\t\tElfelejtett jelszó</h2>\n" +
                        "\t\t\t\t\t\t\t\t<p style=\"margin: 0; \" class=\"style2\">\n" +
                        "\t\t\t\t\t\t\t\t\t</p>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"padding: 0 30px 20px\" class=\"style7\">\n" +
                        "                                    <p style=\"margin-bottom: 10px;\" class=\"style5\">Kedves \n" +
                        "\t\t\t\t\t\t\t\t\t" + username + "!</p>\n" +
                        "\t\t\t\t\t\t\t\t\t<p style=\"margin-bottom: 10px;\" class=\"style5\">Ezen az \n" +
                        "\t\t\t\t\t\t\t\t\temail címen regisztrált felhasználó új \n" +
                        "\t\t\t\t\t\t\t\t\tjelszót igényelt. </p>\n" +
                        "\t\t\t\t\t\t\t\t\t\n" +
                        "\t\t\t\t\t\t\t\t\t<p style=\"margin-bottom: 10px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t<form method=\"post\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<div class=\"style5\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<br>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<span style=\"width: 18%; background-color: #f5f6fa;\"><a href=\"" + url + "\" style=\"background-color:#15718a; border-radius:150px;display:inline-block;line-height:40px; text-align:center;padding: 0 30px; height: 41px;\" class=\"style3\"><div class=\"style5\">\n" +
                        "\t\t\t\t\t\t\t\t\túJ jelszó beállítása</div>\n" +
                        "\t\t\t\t\t\t\t\t\t</a>\n" +
                        "    </span>\n" +
                        "</div>\n" +
                        "\t\t\t\t\t\t\t\t\t</form>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"padding: 20px 30px 40px\">\n" +
                        "                                    <p class=\"style5\">Amennyiben nem Ön kért új \n" +
                        "\t\t\t\t\t\t\t\t\tjelszót,&nbsp;hagyja&nbsp;ezt \n" +
                        "\t\t\t\t\t\t\t\t\ta levelet&nbsp;figyelmen&nbsp;kívül. </p>\n" +
                        "\t\t\t\t\t\t\t\t\t<p>&nbsp;</p>\n" +
                        "                                    <p style=\"margin: 0; font-size: 13px; line-height: 22px; color:#9ea8bb;\">\n" +
                        "\t\t\t\t\t\t\t\t\t&nbsp;</p>\n" +
                        "\t\t\t\t\t\t\t\t\t<p style=\"margin: 0; line-height: 22px; \" class=\"style4\">\n" +
                        "\t\t\t\t\t\t\t\t\tEz egy automatikusan generált email, kérjük \n" +
                        "\t\t\t\t\t\t\t\t\tne válaszoljon rá!</p>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                        </tbody>\n" +
                        "                    </table>\n" +
                        "                    <table style=\"width:100%;max-width:620px;margin:0 auto;\">\n" +
                        "                        <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"text-align: center; padding:25px 20px 0;\">\n" +
                        "                                    <p style=\"font-size: 13px;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2022.&nbsp; \n" +
                        "\t\t\t\t\t\t\t\t\tMyfinance app . Minden jog fenntartva.</p>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                        </tbody>\n" +
                        "                    </table>\n" +
                        "               </td>\n" +
                        "            </tr>\n" +
                        "        </table>\n" +
                        "    </span>\n" +
                        "</body>\n" +
                        "</html>";
    }
}
