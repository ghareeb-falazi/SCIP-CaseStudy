package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {
    private String isoDateTime;
    private boolean isError;
    private String message;
    private boolean isDone;
}
