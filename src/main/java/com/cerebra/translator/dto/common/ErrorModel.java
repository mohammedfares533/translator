package com.cerebra.translator.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorModel {

    private String errorRef;
    private String description;

    /**
     * Instantiates a new Error.
     *
     * @param errorRef     the errorRef
     * @param description the description
     *
     *  return : Error Model
     */
    public ErrorModel(String errorRef, String description) {
        this.errorRef = errorRef;
        this.description = description;
    }
}
