package ci.bhci.bhevaluationpro.transformer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

public class  Transformer<DTO, DAO> {
	private Class<DTO> dtoClass;

	private Class<DAO> daoClass;

	private ModelMapper modelMapper;

	public Transformer(Class<DTO> dtoClass, Class<DAO> daoClass) {
		modelMapper = new ModelMapper();
		this.dtoClass = dtoClass;
		this.daoClass = daoClass;
	}

	public DTO convertToDto(DAO source) {
		DTO dto = modelMapper.map(source, dtoClass);
		return dto;
	}

	public DTO convertToDto(Optional<DAO> source) {
		DTO dto = modelMapper.map(source, dtoClass);
		return dto;
	}

	public List<DTO> convertToDto(List<DAO> source) {
		List<DTO> dtos = convertToList(source, dtoClass);
		return dtos;
	}

	public DAO convertToEntity(DTO source) {
		DAO dao = modelMapper.map(source, daoClass);
		return dao;
	}

	public List<DAO> convertToEntity(List<DTO> source) {
		List<DAO> daos = convertToList(source, daoClass);
		return daos;
	}

	<S, T> List<T> convertToList(List<S> sources, Class<T> targetClass) {
		return sources.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
	}


}
