package de.adorsys.smartanalytics.exception.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * Key/Code zur Identifizierung der Message.
     */
    private String key;

    private Severity severity;

    /**
     * Feldbezug bei Validierungsfehlern. Optional.
     */
    private String field;

    /**
     * Ausformulierte Beschreibung des Fehlers. Optional.
     */
    private String renderedMessage;

    /**
     * Zurätzliche Informationen zu dem Fehler- Optional.
     */
    private Map<String, String> paramsMap; // NOSONAR

    public enum Severity {
        ERROR,
        WARNING,
        INFO
    }

    public Message(String key, Severity severity) {
        this.key = key;
        this.severity = severity;
    }

    public Message(String key, Severity severity, String renderedMessage) {
        this.key = key;
        this.severity = severity;
        this.renderedMessage = renderedMessage;
    }

    public Message(String key, Severity severity, String renderedMessage, Map<String, String> paramsMap) {
        this.key = key;
        this.severity = severity;
        this.renderedMessage = renderedMessage;
        this.paramsMap = paramsMap;
    }
}
