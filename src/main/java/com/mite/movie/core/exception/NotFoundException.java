package com.mite.movie.core.exception;

import org.springframework.lang.NonNull;

/**
 * Class to handle exceptions related to the business logic 
 * when an Object is not found.
 * 
 * @author Mykhaylo.T
 *
 */
public class NotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public NotFoundException(@NonNull Class<?> object, Long id) {
	        super(String.format("Object [%s] with id [%d] not found", object.getSimpleName(), id));
	}
	
	public NotFoundException(@NonNull Class<?> object, String name) {
        super(String.format("Object [%s] with name [%s] not found", object.getSimpleName(), name));
    }

}
