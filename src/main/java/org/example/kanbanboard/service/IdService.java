package org.example.kanbanboard.service;

import lombok.AllArgsConstructor;
import org.example.kanbanboard.repository.BoardRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IdService {
    private final BoardRepository repo;

    public int nextId(){

        if(repo.count() == 0) return 1;

        int recentId = repo.findAll(Sort.by("id")).getLast().id();
        return recentId + 1;
    }
}
