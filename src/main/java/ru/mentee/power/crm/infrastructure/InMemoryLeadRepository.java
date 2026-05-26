package ru.mentee.power.crm.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.Repository;

public class InMemoryLeadRepository implements Repository<Lead> {
  // список для хранения объектов Lead в памяти
  private final List<Lead> storage = new ArrayList<>();

  @Override
  public void add(Lead entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Entity must not be null");
    }
    if (storage.contains(entity)) {
      return;
    }
    storage.add(entity);
  }

  @Override
  public void remove(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id must not be null");
    }
    storage.removeIf(lead -> lead.id().equals(id));
  }

  @Override
  public Optional<Lead> findById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id must not be null");
    }
    return storage.stream().filter(lead -> lead.id().equals(id)).findFirst();
  }

  @Override
  public List<Lead> findAll() {
    // возвращаем копию для защиты данных
    return new ArrayList<>(storage);
  }
}
