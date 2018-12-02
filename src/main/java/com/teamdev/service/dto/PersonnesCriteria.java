package com.teamdev.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Personnes entity. This class is used in PersonnesResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /personnes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonnesCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter noms;

    private StringFilter prenoms;

    private StringFilter sexe;

    private StringFilter telephone;

    private StringFilter pays;

    private StringFilter nationalite;

    private StringFilter dateNaissance;

    private StringFilter lieuNaissance;

    public PersonnesCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNoms() {
        return noms;
    }

    public void setNoms(StringFilter noms) {
        this.noms = noms;
    }

    public StringFilter getPrenoms() {
        return prenoms;
    }

    public void setPrenoms(StringFilter prenoms) {
        this.prenoms = prenoms;
    }

    public StringFilter getSexe() {
        return sexe;
    }

    public void setSexe(StringFilter sexe) {
        this.sexe = sexe;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getPays() {
        return pays;
    }

    public void setPays(StringFilter pays) {
        this.pays = pays;
    }

    public StringFilter getNationalite() {
        return nationalite;
    }

    public void setNationalite(StringFilter nationalite) {
        this.nationalite = nationalite;
    }

    public StringFilter getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(StringFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public StringFilter getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(StringFilter lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    @Override
    public String toString() {
        return "PersonnesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (noms != null ? "noms=" + noms + ", " : "") +
                (prenoms != null ? "prenoms=" + prenoms + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (telephone != null ? "telephone=" + telephone + ", " : "") +
                (pays != null ? "pays=" + pays + ", " : "") +
                (nationalite != null ? "nationalite=" + nationalite + ", " : "") +
                (dateNaissance != null ? "dateNaissance=" + dateNaissance + ", " : "") +
                (lieuNaissance != null ? "lieuNaissance=" + lieuNaissance + ", " : "") +
            "}";
    }

}
