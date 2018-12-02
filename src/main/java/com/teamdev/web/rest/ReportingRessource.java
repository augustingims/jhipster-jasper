package com.teamdev.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamdev.repository.PersonnesRepository;
import com.teamdev.service.JasperManagerService;
import com.teamdev.service.dto.ReportingPrinter;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reporting/")
public class ReportingRessource {

    @Autowired
    private JasperManagerService jasperManagerService;

    @Autowired
    private PersonnesRepository personnesRepository;

    @RequestMapping(value = "/listePersonnes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportingPrinter> listePersonnes() {

        ReportingPrinter r = jasperManagerService.printPersonnes();

        return new ResponseEntity<>(r, HttpStatus.OK);

    }


}
