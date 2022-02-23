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

import ci.bhci.bhevaluationpro.domain.Personnel;
import ci.bhci.bhevaluationpro.domain.dto.PersonnelDto;
import ci.bhci.bhevaluationpro.domain.dto.PersonnelPosteDto;
import ci.bhci.bhevaluationpro.exception.CustomAlreadyExistsException;
import ci.bhci.bhevaluationpro.exception.CustomDataNotFoundException;
import ci.bhci.bhevaluationpro.exception.CustomErrorException;
import ci.bhci.bhevaluationpro.service.DepartementService;
import ci.bhci.bhevaluationpro.service.DirectionService;
import ci.bhci.bhevaluationpro.service.FonctionService;
import ci.bhci.bhevaluationpro.service.PersonnelPosteService;
import ci.bhci.bhevaluationpro.service.PersonnelService;
import ci.bhci.bhevaluationpro.transformer.Transformer;
import ci.bhci.bhevaluationpro.util.ApiPaths;
import ci.bhci.bhevaluationpro.util.Response;
import lombok.extern.log4j.Log4j2;

/**
 * Controller for Personnel services
 * 
 * @author kyao
 * @since 2022-02-03
 */

@RestController
@RequestMapping(ApiPaths.Entity.BPA_PERSONNEL)
@Log4j2
public class PersonnelController {
	private final PersonnelService service;
	private final FonctionService fonctionService;
	private final PersonnelPosteService personnelPosteService;

	private boolean isExiste = true; // Variable booléenne initialisée à vrai

	private final Transformer<PersonnelDto, Personnel> transformer = new Transformer<PersonnelDto, Personnel>(
			PersonnelDto.class, Personnel.class);

	@Autowired
	public PersonnelController(PersonnelService service, DirectionService directionService,
			DepartementService departementService, PersonnelPosteService personnelPosteService,
			FonctionService fonctionService) {
		this.service = service;
		this.fonctionService = fonctionService;
		this.personnelPosteService = personnelPosteService;
	}

	/**
	 * Get method to find all entities Fonction
	 * 
	 * @return {@code ResponseEntity}
	 */
	@GetMapping
	public ResponseEntity<Response> getPersonnels() {
		log.info("Initializing PersonnelService : getFonctions");
		try {
			List<PersonnelDto> entityDtos = this.service.getAll();
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
	 * Get Method to find entity Personnel By Id
	 * 
	 * @param id
	 * @return PersonnelDto{@code ResponseEntity}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Response> getById(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing PersonnelService : getById");
		try {
			Response response = new Response();
			Optional<Personnel> foundEntity = this.service.getById(id);
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
	 * Get Method to find entity Personnel By Direction Id
	 * 
	 * @param id
	 * @return PersonnelDto{@code ResponseEntity}
	 */
	@GetMapping("/direction/{id}")
	public ResponseEntity<Response> getByDirection(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing PersonnelService : getById");
		try {
			Response response = new Response();
			List<PersonnelDto> foundEntity = this.service.findByDirection(id);
			if (foundEntity.size() > 0) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(foundEntity);
				log.info("Liste de Personnel pour Direction ID :" + id + " trouvée." + "\r\n" + "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncun Personnel pour Direction ID :" + id + " trouvé !").getMessage());
				log.warn("Auncun Personnel pour Direction ID :" + id + " trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	/**
	 * Get Method to find entity Personnel By Departement Id
	 * 
	 * @param id
	 * @return PersonnelDto{@code ResponseEntity}
	 */
	@GetMapping("/departement/{id}")
	public ResponseEntity<Response> getByDepartement(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing PersonnelService : getById");
		try {
			Response response = new Response();
			List<PersonnelDto> foundEntity = this.service.findByDepartement(id);
			if (foundEntity.size() > 0) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(foundEntity);
				log.info("Liste de Personnel pour Departement ID :" + id + " trouvée." + "\r\n" + "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncun Personnel pour Departement ID :" + id + " trouvé !").getMessage());
				log.warn("Auncun Personnel pour Departement ID :" + id + " trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}
	
	/**
	 * Get Method to find entity Personnel By Manager Id
	 * 
	 * @param id
	 * @return PersonnelDto{@code ResponseEntity}
	 */
	@GetMapping("/manager/{id}")
	public ResponseEntity<Response> getByManager(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing PersonnelService : getById");
		try {
			Response response = new Response();
			List<PersonnelDto> foundEntity = this.service.findByManager(id);
			if (foundEntity.size() > 0) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(foundEntity);
				log.info("Liste de Personnel pour Manager ID :" + id + " trouvée." + "\r\n" + "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncun Personnel pour Manager ID :" + id + " trouvé !").getMessage());
				log.warn("Auncun Personnel pour Manager ID :" + id + " trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}
	
	/**
	 * Get Method to find entity Personnel By Manager Id
	 * 
	 * @param id
	 * @return PersonnelDto{@code ResponseEntity}
	 */
	@GetMapping("/fonction/{id}")
	public ResponseEntity<Response> getByFonction(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing PersonnelService : getById");
		try {
			Response response = new Response();
			List<PersonnelDto> foundEntity = this.service.findByFonction(id);
			if (foundEntity.size() > 0) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(foundEntity);
				log.info("Liste de Personnel pour Fonction ID :" + id + " trouvée." + "\r\n" + "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncun Personnel pour Fonction ID :" + id + " trouvé !").getMessage());
				log.warn("Auncun Personnel pour Fonction ID :" + id + " trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}	
	
	/**
	 * Get Method to find entity Personnel By Manager Id
	 * 
	 * @param id
	 * @return PersonnelDto{@code ResponseEntity}
	 */
	@GetMapping("/personnelposte/{id}")
	public ResponseEntity<Response> getByPersonnelPoste(@PathVariable(value = "id", required = true) Long id) {
		log.info("Initializing PersonnelService : getById");
		try {
			Response response = new Response();
			PersonnelDto foundEntity = this.service.findByPersonnelPoste(id);
			if (foundEntity != null) {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération terminée avec succès!");
				response.setData(foundEntity);
				log.info("Personnel pour Manager ID :" + id + " trouvée." + "\r\n" + "Traitement effectué avec succès!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncun Personnel pour Manager ID :" + id + " trouvé !").getMessage());
				log.warn("Auncun Personnel pour Manager ID :" + id + " trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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
	public ResponseEntity<Response> addEntity(@RequestBody PersonnelDto entityDto) throws Exception {
		log.info("Initializing PersonnelService : addEntity");
		try {
			Response response = new Response();
			isExiste = true; // Réinitialisation de la variable à faux
//			Vérification si un enregistrement n'existe pas déjà avec les memes informations
			if (!this.service.existPersonnel(entityDto.getMatriculePersonnel(), entityDto.getEmailPersonnel())) {
//				Vérification si la liste de PersonnelPoste n'est pas vide
				if (entityDto.getPersonnelPosteDto().size() > 0) {
					entityDto.getPersonnelPosteDto().stream().forEach(element -> {
//						Vérification si la fonction existe déjà pour le Personnel
						try {
							if (element.getIdPersonnel() != null
									|| (element.getIdFonction() == null || (element.getIdFonction() != null
											&& !this.fonctionService.findById(element.getIdFonction()).isPresent()))
									|| element.getDebutPoste() == null) {
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
						response.setCode(HttpStatus.CONFLICT.value());
						response.setStatus(HttpStatus.CONFLICT.name());
						response.setMessage(new CustomAlreadyExistsException(
								"Les informations de PersonnelPoste ne sont pas correctes.").getMessage());
						log.warn("-- Informations PersonnelPoste non corretes --");
						return new ResponseEntity<>(response, HttpStatus.CONFLICT);
					}
				}
				entityDto = this.service.addEntity(entityDto);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération effectuée avec succès!");
				response.setData(entityDto);
				log.info("-- Enregistrement de Personnel effectué avec succès --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.ALREADY_REPORTED.value());
				response.setStatus(HttpStatus.ALREADY_REPORTED.name());
				response.setMessage(new CustomAlreadyExistsException(
						"Un enregistrement Personnel existe déjà avec les mêmes informations.").getMessage());
				log.warn("-- Un enregistrement de Personnel existe déjà --");
				return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response> editEntity(@RequestBody PersonnelDto entityDto, @PathVariable("id") Long id) {
		log.info("Initializing PersonnelService : updateEntity");
		try {
			Response response = new Response();
			isExiste = true; // Réinitialisation de la variable à faux
//			Vérifiation si une instance Fonction existe pour les informations fournies 
			if (entityDto.getId().equals(id) && this.service.findById(id).isPresent()) {
//					Vérification si la liste de PersonnelPoste n'est pas vide
				if (entityDto.getPersonnelPosteDto().size() > 0) {
					entityDto.getPersonnelPosteDto().stream().forEach(element -> {
						try {
							List<PersonnelPosteDto> personnelPosteDto = this.personnelPosteService
									.findByPesonnelAndFonction(element.getIdPersonnel(), element.getIdFonction());
//								Vérification si PersonnelPoste existe
							if ((element.getId() != null
									&& this.personnelPosteService.findById(element.getId()).equals(null))
									|| !this.service.findById(element.getIdPersonnel()).isPresent()
									|| !this.fonctionService.findById(element.getIdFonction()).isPresent()
									|| (element.getIdPersonnel() != null && !element.getIdPersonnel().equals(id))
									|| (element.getId() == null && (personnelPosteDto.size() > 0
											|| element.getIdPersonnel() == null && element.getIdFonction() == null
													&& element.getDebutPoste() == null))
									) {
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
				entityDto = this.service.updateEntity(entityDto, id);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				response.setData(entityDto);
				log.info("-- Modification de Personnel effectuée avec succès --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Personnel ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Personnel ID :" + id + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response> deleteEntity(@RequestBody PersonnelDto entityDto, @PathVariable("id") Long id) {
		try {
			Response response = new Response();
			if (this.service.findById(id).isPresent() && entityDto.getId().equals(id)) {
				this.service.deleteEntity(entityDto);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				log.info("-- Suppression de Personnel effectuée avec succès --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncune Fonction ID :" + id + " trouvée !").getMessage());
				log.warn("Auncune Personnel ID :" + entityDto.getId() + " trouvée !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}
}
