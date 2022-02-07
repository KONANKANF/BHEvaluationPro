/**
 * 
 */
package ci.bhci.bhevaluationpro.util;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kyao
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private int code;
    private String status;
    private String message;
    private String stackTrace;
    private Object data;	
}
