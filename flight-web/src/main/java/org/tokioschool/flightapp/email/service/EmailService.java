package org.tokioschool.flightapp.email.service;

import org.tokioschool.flightapp.email.dto.EmailDTO;

public interface EmailService {
    void sendEmail(EmailDTO emailDTO);
}
