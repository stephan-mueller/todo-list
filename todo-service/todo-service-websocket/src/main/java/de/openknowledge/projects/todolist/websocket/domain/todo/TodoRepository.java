/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package de.openknowledge.projects.todolist.websocket.domain.todo;

import de.openknowledge.projects.todolist.websocket.infrastructure.domain.repository.AbstractRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * A repository that provides access to {@link Todo} entities.
 */
@ApplicationScoped
public class TodoRepository extends AbstractRepository<Todo> implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(TodoRepository.class);

  public TodoRepository() {
    super();
  }

  @Inject
  public TodoRepository(final EntityManager entityManager) {
    super(entityManager);
  }

  public List<Todo> findAll() {
    LOG.debug("Searching for todos");

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Todo> cq = cb.createQuery(Todo.class);

    Root<Todo> root = cq.from(Todo.class);

    cq.select(root);

    TypedQuery<Todo> query = entityManager.createQuery(cq);
    List<Todo> results = query.getResultList();

    LOG.debug("Located {} todos", results.size());

    return results;
  }
}
