package com.teamdev.service;

import com.teamdev.domain.Personnes;
import com.teamdev.repository.PersonnesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * Created by mac on 02/12/2018.
 */
@Service
@Transactional
public class InitData {

    @Autowired
    private PersonnesRepository personnesRepository;


    @PostConstruct
    void preloaddata(){

        for(int i = 0; i < 20;i++){

            if(personnesRepository.findAll().isEmpty()){
                Personnes personnes = new Personnes();
                personnes.setNoms("GIMS"+i);
                personnes.setPrenoms("Marco"+i);
                personnes.setSexe("M");
                personnes.setTelephone("679896543");
                personnes.setPays("Cameroun");
                personnes.setNationalite("Camerounaise");
                personnes.setLieuNaissance("Douala");
                personnes.setDateNaissance("02 Decembre 2018");

                personnesRepository.save(personnes);
            }

        }

    }

}
