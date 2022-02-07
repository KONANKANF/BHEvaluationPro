package ci.bhci.bhevaluationpro.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ci.bhci.bhevaluationpro.domain.Direction;
import ci.bhci.bhevaluationpro.domain.dto.DirectionDto;
import ci.bhci.bhevaluationpro.exception.CustomDataNotFoundException;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.service.DirectionService;
import ci.bhci.bhevaluationpro.service.impl.DirectionServiceImpl;
import ci.bhci.bhevaluationpro.util.ApiPaths;
import ci.bhci.bhevaluationpro.util.PaginationMod;
import ci.bhci.bhevaluationpro.util.Response;
import lombok.extern.log4j.Log4j2;

/**
 * Controller for the Direction entity
 * 
 * @author kyao
 * @since 2022-02-03
 */

@RestController // or @Controller
//@CrossOrigin
//@RequestMapping(value = "/")
@RequestMapping(ApiPaths.Entity.BPA_DIRECTION)
@Log4j2
public class DirectionController {

	private final DirectionService service;
//	private final Response response;

	@Autowired
	public DirectionController(DirectionService service) {
		this.service = service;
//		this.response = response;
	}

	/**
	 * Get method
	 * 
	 * @return Array of DirectionDto
	 */
	@GetMapping
	public ResponseEntity<Response> getDirections() {
		try {
			List<DirectionDto> entityDtos = this.service.getAll();
			Response response = new Response();
			if(entityDtos.size() > 0) {				
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(entityDtos);
				log.info("Une liste de une ou plusieurs Direction trouvée."+"\r\n"+"Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}else {
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Direction trouvée !").getMessage());
				log.warn("Auncune Direction ID trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
		
	}

	/**
	 * 
	 * @param id
	 * @return DirectionDto
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Response> getById(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing directionService :" + DirectionServiceImpl.class);		
		try {
			Response response = new Response();
			DirectionDto entityDto = this.service.getById(id).orElse(null);
			if (entityDto == null) {				
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Direction ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Direction ID :" + id + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(entityDto);
				log.info("Direction ID :" + id + " trouvée."+"\r\n"+"Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	// PaginationMod<T>: create response object for pagination
//	@GetMapping("/pagination")
//	public ResponseEntity<PaginationMod<DirectionDto>> getAllPageable(Pageable pageable){
//	  PaginationMod<DirectionDto>pages=service.getAllDirections(pageable);
//	  return ResponseEntity.ok(pages);
//	}

}
