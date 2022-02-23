package com.automate.chargecontrol.service.impl;

import com.automate.chargecontrol.service.AdjustChargeDecider;

public class PropertyBasedAdjustChargeDecider implements AdjustChargeDecider {

  private boolean adjustCurrent;

  @Override
  public boolean isAdjustChargeEnabled() {
    return isAdjustCurrent();
  }

  public boolean isAdjustCurrent() {
    return adjustCurrent;
  }

  public void setAdjustCurrent(boolean adjustCurrent) {
    this.adjustCurrent = adjustCurrent;
  }
}
