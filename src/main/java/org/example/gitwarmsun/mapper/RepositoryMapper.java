package org.example.gitwarmsun.mapper;

import org.example.gitwarmsun.model.Repository;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RepositoryMapper {
     void insertRepository(Repository repository);
  
     Repository getRepositoryDetails(int repositoryId);

     void deleteRepository(int repositoryId);

     List<Repository> listAllRepositories(int userId);
}