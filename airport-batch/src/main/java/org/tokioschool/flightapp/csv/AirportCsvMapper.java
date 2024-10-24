package org.tokioschool.flightapp.csv;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AirportCsvMapper implements FieldSetMapper<AirportCsv> {

  public static final String[] AIRPORT_CSV_FIELDS = {
    "id",
    "ident",
    "type",
    "name",
    "latitude_deg",
    "longitude_deg",
    "elevation_ft",
    "continent",
    "iso_country",
    "iso_region",
    "municipality",
    "scheduled_service",
    "gps_code",
    "iata_code",
    "local_code",
    "home_link",
    "wikipedia_link",
    "keywords"
  };


  @Override
  public AirportCsv mapFieldSet(FieldSet fieldSet) throws BindException {
    return AirportCsv.builder()
            .id(readLong(fieldSet.readString("id")))
            .ident(readString(fieldSet.readString("ident")))
            .type(readAirportType(fieldSet.readString("type")))
            .name(readString(fieldSet.readString("name")))
            .latitudeDeg(readBigDecimal(fieldSet.readString("latitude_deg")))
            .longitudeDeg(readBigDecimal(fieldSet.readString("longitude_deg")))
            .elevationFt(readInt(fieldSet.readString("elevation_ft")))
            .continent(readString(fieldSet.readString("continent")))
            .isoCountry(readString(fieldSet.readString("iso_country")))
            .isoRegion(readString(fieldSet.readString("iso_region")))
            .municipality(readString(fieldSet.readString("municipality")))
            .scheduledService(readString(fieldSet.readString("scheduled_service")))
            .gpsCode(readString(fieldSet.readString("gps_code")))
            .iataCode(readString(fieldSet.readString("iata_code")))
            .localCode(readString(fieldSet.readString("local_code")))
            .homeLink(readString(fieldSet.readString("home_link")))
            .wikipediaLink(readString(fieldSet.readString("wikipedia_link")))
            .keywords(readListOfString(fieldSet.readString("keywords")))
            .build();
  }

  //Metodos de ayuda para leer los campos del csv
  //Estos metodos se encargan de leer los campos del csv y convertirlos a los tipos de datos que necesitamos

  //Devuelve un entero, si el valor es nulo o vacio devuelve null
  protected Integer readInt(String value){
    return Optional.ofNullable(StringUtils.trimToNull(value)).map(Integer::parseInt).orElse(null);
  }

  //Devuelve un long, si el valor es nulo o vacio devuelve null
  protected Long readLong(String value){
    return Optional.ofNullable(StringUtils.trimToNull(value)).map(Long::parseLong).orElse(null);
  }

  //Devuelve un string, si el valor es nulo o vacio devuelve null
  protected String readString(String value){
    return StringUtils.trimToNull(value);
  }

  //Devuelve un BigDecimal, si el valor es nulo o vacio devuelve null
  protected BigDecimal readBigDecimal(String value){
    return Optional.ofNullable(StringUtils.trimToNull(value)).map(BigDecimal::new).orElse(null);
  }

  //Devuelve un AirportType, si el valor es nulo o vacio devuelve null
  protected AirportCsv.AirportType readAirportType(String value){
    return Optional.ofNullable(StringUtils.trimToNull(value))
            .map(AirportCsv.AirportType::ofLabel)
            .orElse(null);
  }

  //Devuelve una lista de strings, si el valor es nulo o vacio devuelve una lista vacia
  protected List<String> readListOfString(String value){
    return Optional.ofNullable(StringUtils.trimToNull(value))
            .map(commaSeparatedValues -> commaSeparatedValues.split(","))
            .map(Arrays::asList)
            .orElse(List.of());
  }

}
