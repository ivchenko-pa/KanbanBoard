package org.example.kanbanboard.repository;

import org.example.kanbanboard.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends MongoRepository<Task, String> {

}