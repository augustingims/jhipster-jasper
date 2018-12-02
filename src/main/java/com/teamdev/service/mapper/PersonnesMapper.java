package com.teamdev.service.mapper;

import com.teamdev.domain.*;
import com.teamdev.service.dto.PersonnesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Personnes and its DTO PersonnesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonnesMapper extends EntityMapper<PersonnesDTO, Personnes> {



    default Personnes fromId(Long id) {
        if (id == null) {
            return null;
        }
        Personnes personnes = new Personnes();
        personnes.setId(id);
        return personnes;
    }
}
