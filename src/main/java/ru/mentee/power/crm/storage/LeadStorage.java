package ru.mentee.power.crm.storage;

import ru.mentee.power.crm.domain.Lead;

public class LeadStorage {
  private Lead[] leads = new Lead[100];

  public boolean add(Lead lead) {
    if (lead == null) {
      return false;
    }

    for (Lead value : leads) {
      if (value != null && value.getEmail().equals(lead.getEmail())) {
        return false;
      }
    }

    for (int i = 0; i < leads.length; i++) {
      if (leads[i] == null) {
        leads[i] = lead;
        return true;
      }
    }

    throw new IllegalStateException("Storage is full");
  }

  public Lead[] findAll() {
    Lead[] result = new Lead[size()];

    int resultIndex = 0;
    for (Lead lead : leads) {
      if (lead != null) {
        result[resultIndex] = lead;
        resultIndex++;
      }
    }

    return result;
  }

  public int size() {
    int count = 0;
    for (Lead lead : leads) {
      if (lead != null) {
        count++;
      }
    }
    return count;
  }

}
