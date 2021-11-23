package it.laskaridis.blueground.commons.emails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class Email {

    private String recipient;
    private String subject;
    private String body;

}
