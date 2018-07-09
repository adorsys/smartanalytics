package de.adorsys.smartanalytics.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "INVALID_FILE"
)
public class FileUploadException extends ParametrizedMessageException {

    public FileUploadException(String message) {
        super("unable import file");
        this.addParam("message", message);
    }

}
