package com.mite.movie.core.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Class used for converting {@link com.mite.movie.dto} 
 * to {@link com.mite.movie.database.entity} and vice-versa.
 * 
 * @author Mykhaylo.T
 */
@Component
public class ObjectConverter <D, E> {
	
	private final ModelMapper modelMapper;
	
	public ObjectConverter (ModelMapper modelMapper){
		this.modelMapper = modelMapper;
	}
	
	
	@SuppressWarnings("unchecked")
	public E toEntity(D dto,  E entity) {
		return modelMapper.map(dto, (Class<E>) entity);
	}
	
	@SuppressWarnings("unchecked")
	public D toDto(E entity,  D dto) {
		return  modelMapper.map( entity,(Class<D>) dto);
	}
}
