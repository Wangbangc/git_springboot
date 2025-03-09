package org.example.gitwarmsun.service;

import org.example.gitwarmsun.model.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RepositoryService {
    Repository initializeRepository(String repositoryName, String description);
    Repository createRepository(int userId,String username, String repositoryName, String description);
    void deleteRepository(int repositoryId);
    String cloneRepository(int repositoryId);
    void commitChanges(int repositoryId, String message, List<String> filePaths);
    void pullLatestCode(int repositoryId);
    List<Repository> listAllRepositories();
    Repository getRepositoryDetails(int repositoryId);
    void pushToRemote(int repositoryId, String remoteUrl, String branchName);

}