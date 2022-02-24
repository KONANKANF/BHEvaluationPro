package ci.bhci.bhevaluationpro.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import ci.bhci.bhevaluationpro.domain.Direction;
import ci.bhci.bhevaluationpro.domain.dto.DirectionDto;
import ci.bhci.bhevaluationpro.exception.CustomAlreadyExistsException;
import ci.bhci.bhevaluationpro.exception.CustomDataNotFoundException;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.service.DepartementService;
import ci.bhci.bhevaluationpro.service.DirectionService;
import ci.bhci.bhevaluationpro.transformer.Transformer;
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
@RequestMapping(ApiPaths.Entity.BPA_DIRECTION)
@Log4j2
public class DirectionController {

	private final DirectionService service;
	private final DepartementService departementService;

	private final Transformer<DirectionDto, Direction> transformer = new Transformer<DirectionDto, Direction>(
			DirectionDto.class, Direction.class);
	private boolean isExiste = true;

	@Autowired
	public DirectionController(DirectionService service, DepartementService departementService) {
		this.service = service;
		this.departementService = departementService;
	}

	/**
	 * Get method to find all entities Direction
	 * 
	 * @return {@code ResponseEntity}
	 */
	@GetMapping("/all")
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
	@GetMapping("/find/{id}")
	public ResponseEntity<Response> getById(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing DirectionService : getById");
		try {
			Response response = new Response();
			Optional<Direction> foundEntity = this.service.getById(id);
			if (!foundEntity.isPresent()) {
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
				response.setData(this.transformer.convertToDto(foundEntity.get()));
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
	@PostMapping("/add")
	public ResponseEntity<Response> addEntity(@RequestBody DirectionDto entityDto) {
		log.info("Initializing DirectionService : addEntity");
		try {
			Response response = new Response();
			isExiste = true;
			if (!this.service.existDirection(entityDto.getCodeDirection(), entityDto.getLibelleDirection())) {
				if (entityDto.getDepartementDto().size() > 0) {
					entityDto.getDepartementDto().stream().forEach(element -> {
						if (element.getIdDirection() != null) {
							isExiste = false;
							return;
						}
					});
					if (!isExiste) {
						response.setTimestamp(new Date());
						response.setCode(HttpStatus.CONFLICT.value());
						response.setStatus(HttpStatus.CONFLICT.name());
						response.setMessage(new CustomAlreadyExistsException(
								"L'entité Direction renseigné au niveau de PersonnelPoste n'est pas cohérente.")
										.getMessage());
						log.warn("-- Données de PersonnelPoste ne sont pas correctes --");
						return new ResponseEntity<>(response, HttpStatus.CONFLICT);
					}
				}
				entityDto = this.service.addEntity(entityDto);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération effectuée avec succès!");
				response.setData(entityDto);
				log.info("-- Enregistrement de Direction effectué avec succès  --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.ALREADY_REPORTED.value());
				response.setStatus(HttpStatus.ALREADY_REPORTED.name());
				response.setMessage(
						new CustomAlreadyExistsException("Un enregistrement existe déjà avec les mêmes informations.")
								.getMessage());
				log.warn("-- Un enregistrement de Direction existe déjà --");
				return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@PutMapping("/edit")
	public ResponseEntity<Response> editEntity(@RequestBody DirectionDto entityDto) {
		try {
			Response response = new Response();
			isExiste = true;
			final Long id = entityDto.getId();
			if (this.service.findById(id).isPresent()) {
				if (entityDto.getDepartementDto().size() > 0) {
					entityDto.getDepartementDto().stream().forEach(element -> {
						try {
							if ((element.getId() != null
									&& (!this.departementService.findById(element.getId()).isPresent()
											|| !this.departementService.getByDirection(id, element.getId()).isPresent())
									|| (element.getIdDirection() != null && !element.getIdDirection().equals(id)))) {
								isExiste = false;
								return;
							}
						} catch (SQLException e) {
							log.error("Error -> " + e.getMessage());
							throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
									"Error -> " + e.getMessage());
						}
					});
					if (!isExiste) {
						response.setTimestamp(new Date());
						response.setCode(HttpStatus.NOT_FOUND.value());
						response.setStatus(HttpStatus.NOT_FOUND.name());
						response.setMessage(new CustomAlreadyExistsException(
								"Un ou plusieurs enregistrements PersonnelPoste non trouvé(s).").getMessage());
						log.warn("-- Enregistrement PersonnelPoste n'existe pas --");
						return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
					}
				}
				entityDto = this.service.updateEntity(entityDto);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				response.setData(entityDto);
				log.info("-- Modification de Direction effectuée avec succès --");
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

	@PutMapping("/delete")
	public ResponseEntity<Response> deleteEntity(@RequestBody DirectionDto entityDto) {
		try {
			Response response = new Response();
			final Long id = entityDto.getId();
			if (this.service.findById(id).isPresent()) {
				this.service.deleteEntity(entityDto);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				log.info("-- Suppression de Direction effectuée avec succès --");
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
}
