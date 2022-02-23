package com.automate.chargecontrol.service.impl;

import com.automate.chargecontrol.generated.data.SolarState;
import com.automate.chargecontrol.service.EnergyAvailabilityService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DefaultEnergyAvailabilityService implements EnergyAvailabilityService {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private RestTemplate availableEnergyTemplate;
  private String availableEnergyServiceUrl;

  @Override
  public double getAvailableEnergy() {
    ResponseEntity<SolarState> response = getAvailableEnergyTemplate()
        .getForEntity(getAvailableEnergyServiceUrl(), SolarState.class);
    LOG.info("Response from energy service: {}", response);
    if (HttpStatus.OK.equals(response.getStatusCode())) {
      return response.getBody().getPower();
    }
    LOG.error("Didn't get a 200 code from availability service. Code received {}",
        response.getStatusCode());
    return -1;
  }

  public RestTemplate getAvailableEnergyTemplate() {
    return availableEnergyTemplate;
  }

  public void setAvailableEnergyTemplate(
      RestTemplate availableEnergyTemplate) {
    this.availableEnergyTemplate = availableEnergyTemplate;
  }

  public String getAvailableEnergyServiceUrl() {
    return availableEnergyServiceUrl;
  }

  public void setAvailableEnergyServiceUrl(String availableEnergyServiceUrl) {
    this.availableEnergyServiceUrl = availableEnergyServiceUrl;
  }
}
