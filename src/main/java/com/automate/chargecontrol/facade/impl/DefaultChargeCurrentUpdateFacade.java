package com.automate.chargecontrol.facade.impl;

import com.automate.chargecontrol.facade.ChargeCurrentUpdateFacade;
import com.automate.chargecontrol.service.AdjustChargeDecider;
import com.automate.chargecontrol.service.ChargerCurrentUpdater;
import com.automate.chargecontrol.service.EnergyAvailabilityService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultChargeCurrentUpdateFacade implements ChargeCurrentUpdateFacade {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private AdjustChargeDecider adjustChargeDecider;
  private ChargerCurrentUpdater chargerCurrentUpdater;
  private EnergyAvailabilityService energyAvailabilityService;
  private int bufferEnergy, baseVoltage, minCurrent;

  @Override
  public void updateChargeCurrent() {
    if (getAdjustChargeDecider().isAdjustChargeEnabled()) {
      LOG.info("Adjust current enabled. Getting available power");
      double availableEnergy = getEnergyAvailabilityService().getAvailableEnergy();
      LOG.info("Available power {} watts", availableEnergy);
      if (availableEnergy > 0) {
        int calculatedCurrent = Math
            .max(getMinCurrent(), calculateAvailableCurrent(availableEnergy));
        LOG.info("Calculated current {}", calculatedCurrent);
        getChargerCurrentUpdater().updateChargeCurrent(calculatedCurrent);
        LOG.info("Updated charge current to {}", calculatedCurrent);
      }
    }
  }

  private int calculateAvailableCurrent(double availableEnergy) {
    double chargeAvailableEnergy = availableEnergy - getBufferEnergy();
    int availableCurrent = (int) (chargeAvailableEnergy / getBaseVoltage());
    return availableCurrent;
  }


  public AdjustChargeDecider getAdjustChargeDecider() {
    return adjustChargeDecider;
  }

  public void setAdjustChargeDecider(
      AdjustChargeDecider adjustChargeDecider) {
    this.adjustChargeDecider = adjustChargeDecider;
  }

  public ChargerCurrentUpdater getChargerCurrentUpdater() {
    return chargerCurrentUpdater;
  }

  public void setChargerCurrentUpdater(
      ChargerCurrentUpdater chargerCurrentUpdater) {
    this.chargerCurrentUpdater = chargerCurrentUpdater;
  }

  public int getBufferEnergy() {
    return bufferEnergy;
  }

  public void setBufferEnergy(int bufferEnergy) {
    this.bufferEnergy = bufferEnergy;
  }

  public EnergyAvailabilityService getEnergyAvailabilityService() {
    return energyAvailabilityService;
  }

  public void setEnergyAvailabilityService(
      EnergyAvailabilityService energyAvailabilityService) {
    this.energyAvailabilityService = energyAvailabilityService;
  }

  public int getBaseVoltage() {
    return baseVoltage;
  }

  public void setBaseVoltage(int baseVoltage) {
    this.baseVoltage = baseVoltage;
  }

  public int getMinCurrent() {
    return minCurrent;
  }

  public void setMinCurrent(int minCurrent) {
    this.minCurrent = minCurrent;
  }
}
