package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response.LogEntry;

public class LogsHandler {
    private static LogsHandler instance;
    private List<LogEntry> entries;

    private LogsHandler() {
        entries = new ArrayList<>();
    }

    public static LogsHandler getInstance() {
        if (instance == null) {
            instance = new LogsHandler();
        }

        return instance;
    }

    public void addMessage(String message) {
        String dateTimeIso = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        entries.add(new LogEntry(dateTimeIso, false, message, false));
    }

    public void addError(String message) {
        String dateTimeIso = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        entries.add(new LogEntry(dateTimeIso, true, message, false));
        recordDoneSignal();
    }

    public void recordDoneSignal() {
        entries.add(new LogEntry(null, false, null, true));
    }

    public List<LogEntry> getEntries() {
        return entries;
    }

    public void clear() {
        entries.clear();
    }
}
