package ci.bhci.bhevaluationpro.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.Direction;
import ci.bhci.bhevaluationpro.domain.dto.DirectionDto;
import ci.bhci.bhevaluationpro.exception.CustomAlreadyExistsException;
import ci.bhci.bhevaluationpro.exception.CustomDataNotFoundException;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.service.DirectionService;
import ci.bhci.bhevaluationpro.util.ApiPaths;
import ci.bhci.bhevaluationpro.util.Response;
import lombok.extern.log4j.Log4j2;

/**
 * Controller for the Direction entity
 * 
 * @author kyao
 * @since 2022-02-03
 */

@RestController
//@CrossOrigin
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
	 * Get method to find all entities Direction
	 * 
	 * @return {@code ResponseEntity}
	 */
	@GetMapping
	public ResponseEntity<Response> getDirections() {
		log.info("Initializing DirectionService : getDirections");
		try {
			List<DirectionDto> entityDtos = this.service.getAll();
			Response response = new Response();
			if (entityDtos.size() > 0) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(entityDtos);
				log.info("Une liste de une ou plusieurs Direction trouvée." + "\r\n"
						+ "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(new CustomDataNotFoundException("Auncune Direction trouvée !").getMessage());
				log.warn("Auncune Direction ID trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}

	}

	/**
	 * Get Method to find entity Direction By Id
	 * 
	 * @param id
	 * @return DirectionDto{@code ResponseEntity}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Response> getById(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing DirectionService : getById");
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
				log.info("Direction ID :" + id + " trouvée." + "\r\n" + "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	/**
	 * Post Method to persist new entity DIrection
	 * 
	 * @param entityDto
	 * @return {@code ResponseEntity}
	 */
	@PostMapping
	public ResponseEntity<Response> addEntity(@RequestBody DirectionDto entityDto) {
		log.info("Initializing DirectionService : addEntity");
		try {
			Response response = new Response();
			if (!this.service.existDirection(entityDto.getCodeDirection(), entityDto.getLibelleDirection())) {
				entityDto = this.service.addEntity(entityDto);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération effectuée avec succès!");
				response.setData(entityDto);
				log.info("-- New entity Direction added successfully --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				log.info("-- Entity Direction already exists --");
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.ALREADY_REPORTED.value());
				response.setStatus(HttpStatus.ALREADY_REPORTED.name());
				response.setMessage(
						new CustomAlreadyExistsException("Un enregistrement existe déjà avec les mêmes informations.")
								.getMessage());
				return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response> editEntity(@RequestBody DirectionDto entityDto, @PathVariable("id") Long id) {
		try {
			Response response = new Response();
			if (this.service.findById(id).isPresent()) {
				entityDto = this.service.updateEntity(entityDto, id);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				response.setData(entityDto);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Direction ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Direction ID :" + id + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response> deleteEntity(@RequestBody DirectionDto entityDto, @PathVariable("id") Long id) {
		try {
			Response response = new Response();
			if (this.service.findById(id).isPresent()) {
				this.service.delete(entityDto, id);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Direction ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Direction ID :" + entityDto.getId() + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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
