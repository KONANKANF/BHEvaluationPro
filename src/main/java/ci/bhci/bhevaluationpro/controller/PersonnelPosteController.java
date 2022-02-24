package ci.bhci.bhevaluationpro.controller;

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

import ci.bhci.bhevaluationpro.domain.PersonnelPoste;
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

@RestController
@RequestMapping(ApiPaths.Entity.BPA_PERSONNELPOSTE)
@Log4j2
public class PersonnelPosteController {
	private final PersonnelPosteService service;
	private boolean isExiste = true; // Variable booléenne initialisée à vrai

	private final Transformer<PersonnelPosteDto, PersonnelPoste> transformer = new Transformer<PersonnelPosteDto, PersonnelPoste>(
			PersonnelPosteDto.class, PersonnelPoste.class);

	@Autowired
	public PersonnelPosteController(PersonnelPosteService service, DirectionService directionService,
			DepartementService departementService, PersonnelService personnelService,
			FonctionService fonctionService) {
		this.service = service;
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
			List<PersonnelPosteDto> entityDtos = this.service.getAll();
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
			Optional<PersonnelPoste> foundEntity = this.service.findById(id);
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
	public ResponseEntity<Response> addEntity(@RequestBody PersonnelPosteDto entityDto) throws Exception {
		log.info("Initializing PersonnelPosteService : addEntity");
		try {
			Response response = new Response();
//			Vérification si un enregistrement n'existe pas déjà avec les memes informations
			if (!this.service.existPersonnelPoste(entityDto.getIdPersonnel(), entityDto.getIdFonction())) {
				entityDto = this.service.addEntity(entityDto);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Opération effectuée avec succès!");
				response.setData(entityDto);
				log.info("-- Enregistrement de PersonnelPoste effectué avec succès --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.ALREADY_REPORTED.value());
				response.setStatus(HttpStatus.ALREADY_REPORTED.name());
				response.setMessage(new CustomAlreadyExistsException(
						"Un enregistrement PersonnelPoste existe déjà avec les mêmes informations.").getMessage());
				log.warn("-- Un enregistrement de PersonnelPoste existe déjà --");
				return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Response> editEntity(@RequestBody PersonnelPosteDto entityDto, @PathVariable("id") Long id) {
		log.info("Initializing PersonnelService : updateEntity");
		try {
			Response response = new Response();
			isExiste = true; // Réinitialisation de la variable à faux
			Optional<PersonnelPoste> optional = this.service.findById(id);
//			Vérifiation si une instance Fonction existe pour les informations fournies 
			if (entityDto.getId().equals(id) && optional.isPresent()) {
				List<PersonnelPosteDto> personnelPosteDto = this.service.findByPesonnelAndFonction(entityDto.getIdPersonnel(), entityDto.getIdFonction());
//					Vérification si la liste de PersonnelPoste n'est pas vide
					if (personnelPosteDto.size() > 0) {
						personnelPosteDto.stream().forEach(element -> {
							try {
////								Vérification si PersonnelPoste existe déjà
								if (!element.getId().equals(id)){
									isExiste = false;
									return;
								}
							} catch (Exception e) {
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
									"Un enregistrement PersonnelPoste existe déjà.").getMessage());
							log.warn("-- Enregistrement PersonnelPoste existe déjà! --");
							return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
						}
					}
					entityDto = this.service.updateEntity(entityDto, id);
					response.setTimestamp(new Date());
					response.setCode(HttpStatus.OK.value());
					response.setStatus(HttpStatus.OK.name());
					response.setMessage("Traitement effectué avec succès!");
					response.setData(entityDto);
					log.info("-- Modification de PersonnelPoste effectuée avec succès --");
					return new ResponseEntity<>(response, HttpStatus.OK);
//					}
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncun PersonnelPoste ID :" + id + " trouvé !").getMessage());
				log.warn("Auncun PersonnelPoste ID :" + id + " trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response> deleteEntity(@RequestBody PersonnelPosteDto entityDto, @PathVariable("id") Long id) {
		try {
			Response response = new Response();
			if (this.service.findById(id).isPresent() && entityDto.getId().equals(id)) {
				this.service.deleteEntity(entityDto);
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.OK.value());
				response.setStatus(HttpStatus.OK.name());
				response.setMessage("Traitement effectué avec succès!");
				log.info("-- Suppression de PersonnelPoste effectuée avec succès --");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setTimestamp(new Date());
				response.setCode(HttpStatus.NOT_FOUND.value());
				response.setStatus(HttpStatus.NOT_FOUND.name());
				response.setMessage(
						new CustomDataNotFoundException("Auncun PersonnelPoste ID :" + id + " trouvé !").getMessage());
				log.warn("Auncun PersonnelPoste ID :" + entityDto.getId() + " trouvé !");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Error -> " + e.getMessage());
			throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error -> " + e.getMessage());
		}
	}
}
