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

import ci.bhci.bhevaluationpro.domain.Departement;
import ci.bhci.bhevaluationpro.domain.dto.DepartementDto;
import ci.bhci.bhevaluationpro.exception.CustomAlreadyExistsException;
import ci.bhci.bhevaluationpro.exception.CustomDataNotFoundException;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.service.DepartementService;
import ci.bhci.bhevaluationpro.service.DirectionService;
import ci.bhci.bhevaluationpro.service.FonctionService;
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
//@CrossOrigin
@RequestMapping(ApiPaths.Entity.BPA_DEPARTEMENT)
@Log4j2
public class DepartementController {

	private final DepartementService service;
	private final DirectionService directionService;
	private final FonctionService fonctionService;

	private final Transformer<DepartementDto, Departement> transformer = new Transformer<DepartementDto, Departement>(
			DepartementDto.class, Departement.class);
	private boolean isExiste = true;

	@Autowired
	public DepartementController(DepartementService service, DirectionService directionService,
			FonctionService fonctionService) {
		this.service = service;
		this.directionService = directionService;
		this.fonctionService = fonctionService;
	}

	/**
	 * Get method to find all entities Departement
	 * 
	 * @return {@code ResponseEntity}
	 */
	@GetMapping
	public ResponseEntity<Response> getDepartements() {
		log.info("Initializing DepartementService : getDepartements");
		try {
			List<DepartementDto> entityDtos = this.service.getAll();
			Response response = new Response();
			if (entityDtos.size() > 0) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(entityDtos);
				log.info("Une liste d'un ou plusieurs Departement trouvée." + "\r\n"
						+ "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(new CustomDataNotFoundException("Auncune Departement trouvée !").getMessage());
				log.warn("Auncun Departement ID trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}

	}

	/**
	 * Get Method to find entity Departement By Id
	 * 
	 * @param id
	 * @return DepartementDto{@code ResponseEntity}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Response> getById(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing DepartementService : getById");
		try {
			Response response = new Response();
			Optional<Departement> foundEntity = this.service.getById(id);
			if (!foundEntity.isPresent()) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Departement ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Departement ID :" + id + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(this.transformer.convertToDto(foundEntity.get()));
				log.info("Departement ID :" + id + " trouvée." + "\r\n" + "Traitement effectué avec succès!");
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
	public ResponseEntity<Response> addEntity(@RequestBody DepartementDto entityDto) {
		log.info("Initializing DepartementService : addEntity");
		try {
			Response response = new Response();
			isExiste = true;
			Long idDepartement = entityDto.getIdDirection();
			if (!this.service.existDepartement(entityDto.getIdDirection(), entityDto.getLibelleDepartement())) {
				if (this.directionService.getById(entityDto.getIdDirection()).isPresent()) {
					if (entityDto.getFonctionDto().size() > 0) {
						entityDto.getFonctionDto().stream().forEach(element -> {
							if (element.getIdDepartement() != null
									|| (element.getIdDirection() != null
											&& !element.getIdDirection().equals(idDepartement))) {
								isExiste = false;
								return;
							}
						});
						if (!isExiste) {
							response.setTimestamp(new Date());
							response.setCode(HttpStatus.CONFLICT.value());
							response.setStatus(HttpStatus.CONFLICT.name());
							response.setMessage(new CustomAlreadyExistsException(
									"L'entité Departement renseigné au niveau de Fonction n'est pas cohérente.")
											.getMessage());
							log.warn("-- Données de Fonction ne sont pas correctes --");
							return new ResponseEntity<>(response, HttpStatus.CONFLICT);
						}
					}
					entityDto = this.service.addEntity(entityDto);
					response.setTimestamp(new Date());
					response.setCode(HttpStatus.OK.value());
					response.setStatus(HttpStatus.OK.name());
					response.setMessage("Opération effectuée avec succès!");
					response.setData(entityDto);
					log.info("-- Enregistrement de Departement effectué avec succès --");
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					response.setTimestamp(new Date());
					response.setCode(HttpStatus.NOT_FOUND.value());
					response.setStatus(HttpStatus.NOT_FOUND.name());
					response.setMessage(
							new CustomAlreadyExistsException("La Direction indiquée n'existe pas.").getMessage());
					log.info("-- Echec de l'enregistrement de Fonction. La Direction indiquée n'existe pas --");
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				}

			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.ALREADY_REPORTED.value());
				response.setStatus(HttpStatus.ALREADY_REPORTED.name());
				response.setMessage(
						new CustomAlreadyExistsException("Un enregistrement existe déjà avec les mêmes informations.")
								.getMessage());
				log.warn("-- Un enregistrement de Departement existe déjà --");
				return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response> editEntity(@RequestBody DepartementDto entityDto, @PathVariable("id") Long id) {
		try {
			Response response = new Response();
			isExiste = true;
			Long idDepartement = entityDto.getIdDirection();
			if (entityDto.getId().equals(id) && this.service.getById(id).isPresent()
					&& this.directionService.getById(idDepartement).isPresent()) {
				if (entityDto.getFonctionDto().size() > 0) {
					entityDto.getFonctionDto().stream().forEach(element -> {
						try {
							if ((element.getId() != null
									&& (!this.fonctionService.findById(element.getId()).isPresent())
									|| !this.service.getByDirection(element.getIdDirection(), id).isPresent())
									|| (element.getIdDirection() != null
											&& !element.getIdDirection().equals(idDepartement))
									|| (element.getIdDepartement() != null && !element.getIdDepartement().equals(id))) {
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
								"Un ou plusieurs enregistrements Fonction non trouvé(s).").getMessage());
						log.warn("-- Enregistrement Fonction n'existe pas --");
						return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
					}
				}
				entityDto = this.service.updateEntity(entityDto, id);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				response.setData(entityDto);
				log.info("-- Modification de Departement effectuée avec succès --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Departement ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Departement ID :" + id + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//					response.setTimestamp(new Date());
//					response.setCode(HttpStatus.NOT_FOUND.value());
//					response.setStatus(HttpStatus.NOT_FOUND.name());
//					response.setMessage(
//							new CustomAlreadyExistsException("La Direction indiquée n'existe pas.").getMessage());
//					log.info("-- Echec de l'enregistrement de Fonction. La Direction indiquée n'existe pas --");
//					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
//			} else {
//				response.setTimestamp(new Date());
//				response.setCode(HttpStatus.NOT_FOUND.value());
//				response.setStatus(HttpStatus.NOT_FOUND.name());
//				response.setMessage(
//						new CustomDataNotFoundException("Auncune Departement ID :" + id + " trouvée !").getMessage());
//				log.warn("Auncune Departement ID :" + id + " trouvée !");
//				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//			}

		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response> deleteEntity(@RequestBody DepartementDto entityDto, @PathVariable("id") Long id) {
		try {
			Response response = new Response();
			if (this.service.findById(id).isPresent()) {
				this.service.delete(entityDto, id);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				log.info("-- Suppression de Departement effectuée avec succès --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Departement ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Departement ID :" + entityDto.getId() + " trouvée !");
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
