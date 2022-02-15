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

import ci.bhci.bhevaluationpro.domain.Fonction;
import ci.bhci.bhevaluationpro.domain.dto.FonctionDto;
import ci.bhci.bhevaluationpro.exception.CustomAlreadyExistsException;
import ci.bhci.bhevaluationpro.exception.CustomDataNotFoundException;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.service.DepartementService;
import ci.bhci.bhevaluationpro.service.DirectionService;
import ci.bhci.bhevaluationpro.service.FonctionService;
import ci.bhci.bhevaluationpro.service.PersonnelPosteService;
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
@RequestMapping(ApiPaths.Entity.BPA_FONCTION)
@Log4j2
public class FonctionController {

	private final FonctionService service;
	private final DirectionService directionService;
	private final DepartementService departementService;
	private final PersonnelPosteService personnelPosteService;

	private boolean isExiste = false; // Variable booléenne initialisée à vrai

	private final Transformer<FonctionDto, Fonction> transformer = new Transformer<FonctionDto, Fonction>(
			FonctionDto.class, Fonction.class);

	@Autowired
	public FonctionController(FonctionService service, DirectionService directionService,
			DepartementService departementService, PersonnelPosteService personnelPosteService) {
		this.service = service;
		this.directionService = directionService;
		this.departementService = departementService;
		this.personnelPosteService = personnelPosteService;
	}

	/**
	 * Get method to find all entities Fonction
	 * 
	 * @return {@code ResponseEntity}
	 */
	@GetMapping
	public ResponseEntity<Response> getFonctions() {
		log.info("Initializing FonctionService : getFonctions");
		try {
			List<FonctionDto> entityDtos = this.service.getAll();
			Response response = new Response();
			if (entityDtos.size() > 0) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(entityDtos);
				log.info(
						"Une liste d'une ou plusieurs Fonction trouvée." + "\r\n" + "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(new CustomDataNotFoundException("Auncune Fonction trouvée !").getMessage());
				log.warn("Auncun Fonction ID trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}

	}

	/**
	 * Get Method to find entity Fonction By Id
	 * 
	 * @param id
	 * @return FonctionDto{@code ResponseEntity}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Response> getById(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing FonctionService : getById");
		try {
			Response response = new Response();
			Optional<Fonction> foundEntity = this.service.getById(id);
			if (!foundEntity.isPresent()) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Fonction ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Fonction ID :" + id + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(this.transformer.convertToDto(foundEntity.get()));
				log.info("Fonction ID :" + id + " trouvée." + "\r\n" + "Traitement effectué avec succès!");
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
	 * @return
	 * @return
	 * @return {@code ResponseEntity}
	 */
	@PostMapping
	public ResponseEntity<Response> addEntity(@RequestBody FonctionDto entityDto) throws Exception {
		log.info("Initializing FonctionService : addEntity");

		try {
			Response response = new Response();
			isExiste = false; // Réinitialisation de la variable à faux
//			Vérification si enregistrement n'existe pas déjà
			if (!this.service.existFonction(entityDto.getIdDirection(), entityDto.getIdDepartement(),
					entityDto.getManagerIdFonction(), entityDto.getLibelleFonction())) {
//				Vérification si les informations liées à la Direction/Departement/Manager sont cohérentes
				if (this.directionService.getById(entityDto.getIdDirection()).isPresent()
						&& ((entityDto.getIdDepartement() == null
								|| (entityDto.getIdDepartement() != null && this.departementService
										.getByDirection(entityDto.getIdDirection(), entityDto.getIdDepartement())
										.isPresent())))
						&& (entityDto.getManagerIdFonction() == null
								|| (entityDto.getManagerIdFonction() != null
										&& this.service
												.getByDirection(entityDto.getIdDirection(),
														entityDto.getIdDepartement(), entityDto.getManagerIdFonction())
												.isPresent()))) {
//					Vérification si la liste de PersonnelPoste n'est pas vide
					if (entityDto.getPersonnelPosteDto().size() > 0) {
						entityDto.getPersonnelPosteDto().stream().forEach(element -> {
							try {
//								Vérification si la fonction existe déjà pour le Personnel
								if (element.getIdFonction() != null && this.personnelPosteService
										.existPersonnelPoste(element.getIdPersonnel(), element.getIdFonction())) {
									isExiste = true;
									return;
								}
							} catch (SQLException e) {
								log.error("Error -> " + e.getMessage());
								throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
										"Error -> " + e.getMessage());
							}
						});
						if (isExiste) {
							response.setTimestamp(new Date());
							response.setCode(HttpStatus.ALREADY_REPORTED.value());
							response.setStatus(HttpStatus.ALREADY_REPORTED.name());
							response.setMessage(new CustomAlreadyExistsException(
									"Un enregistrement PersonnelPoste existe déjà avec les mêmes informations.")
											.getMessage());
							log.warn("-- Un enregistrement PersonnelPoste existe déjà --");
							return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
						}
					}
					entityDto = this.service.addEntity(entityDto);
					response.setTimestamp(new Date());
					response.setCode(HttpStatus.OK.value());
					response.setStatus(HttpStatus.OK.name());
					response.setMessage("Opération effectuée avec succès!");
					response.setData(entityDto);
					log.info("-- Enregistrement de Fonction effectué avec succès --");
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					response.setTimestamp(new Date());
					response.setCode(HttpStatus.NOT_FOUND.value());
					response.setStatus(HttpStatus.NOT_FOUND.name());
					response.setMessage(new CustomAlreadyExistsException(
							"Les informations (Direction, Departement ou Manager) fournies ne sont pas correctes.")
									.getMessage());
					response.setData(entityDto);
					log.info("-- Echec de l'enregistrement de Fonction. Les données se sont pas correctes --");
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				}
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.ALREADY_REPORTED.value());
				response.setStatus(HttpStatus.ALREADY_REPORTED.name());
				response.setMessage(new CustomAlreadyExistsException(
						"Un enregistrement Fonction existe déjà avec les mêmes informations.").getMessage());
				log.warn("-- Un enregistrement de Fonction existe déjà --");
				return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response> editEntity(@RequestBody FonctionDto entityDto, @PathVariable("id") Long id) {
		try {
			Response response = new Response();
//			Vérifiation si une instance Fonction existe pour les informations fournies 
			if (this.service.findById(id).isPresent() && this.service.findById(entityDto.getId()).isPresent()) {
//				Vérification si les informations liées à la Direction/Departement/Manager sont cohérentes
				if (this.directionService.getById(entityDto.getIdDirection()).isPresent()
						&& ((entityDto.getIdDepartement() == null
								|| (entityDto.getIdDepartement() != null && this.departementService
										.getByDirection(entityDto.getIdDirection(), entityDto.getIdDepartement())
										.isPresent())))
						&& (entityDto.getManagerIdFonction() == null
								|| (entityDto.getManagerIdFonction() != null
										&& this.service
												.getByDirection(entityDto.getIdDirection(),
														entityDto.getIdDepartement(), entityDto.getManagerIdFonction())
												.isPresent()))) {
//					Vérification si la liste de PersonnelPoste n'est pas vide
					if (entityDto.getPersonnelPosteDto().size() > 0) {
						entityDto.getPersonnelPosteDto().stream().forEach(element -> {
							try {
//								Vérification si PersonnelPoste existe
								if (element.getId() != null && !this.personnelPosteService.findById(element.getId()).isPresent() ) {
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
									"Un ou plusieurs enregistrements PersonnelPoste non trouvé(s).")
											.getMessage());
							log.warn("-- Enregistrement PersonnelPoste n'existe pas --");
							return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
						}
					}
					entityDto = this.service.updateEntity(entityDto, id);
					response.setTimestamp(new Date());
					response.setCode(HttpStatus.OK.value());
					response.setStatus(HttpStatus.OK.name());
					response.setMessage("Traitement effectué avec succès!");
					response.setData(entityDto);
					log.info("-- Modification de Fonction effectuée avec succès --");
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					response.setTimestamp(new Date());
					response.setCode(HttpStatus.NOT_FOUND.value());
					response.setStatus(HttpStatus.NOT_FOUND.name());
					response.setMessage(new CustomAlreadyExistsException(
							"Les informations (Direction, Departement ou Manager) fournies ne sont pas correctes.")
									.getMessage());
					response.setData(entityDto);
					log.info("-- Echec de l'enregistrement de Fonction. Les données se sont pas correctes --");
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				}

			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Fonction ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Fonction ID :" + id + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response> deleteEntity(@RequestBody FonctionDto entityDto, @PathVariable("id") Long id) {
		try {
			Response response = new Response();
			if (this.service.findById(id).isPresent()) {
				this.service.delete(entityDto, id);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				log.info("-- Suppression de Fonction effectuée avec succès --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Fonction ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Fonction ID :" + entityDto.getId() + " trouvée !");
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
