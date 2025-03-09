package org.example.gitwarmsun.service.impl;

import org.example.gitwarmsun.model.Repository;
import org.example.gitwarmsun.service.RepositoryService;
import org.springframework.stereotype.Service;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.gitwarmsun.mapper.RepositoryMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private RepositoryMapper repositoryMapper;


    @Override
    public void pushToRemote(int repositoryId, String remoteUrl, String branchName) {
        // 获取仓库详情，包括路径
        Repository repository = repositoryMapper.getRepositoryDetails(repositoryId);
        if (repository == null) {
            throw new RuntimeException("Repository not found");
        }

        // 打开仓库
        try (Git git = Git.open(new File(repository.getPath()))) {
            // 配置远程仓库
            git.remoteAdd()
               .setName("origin")
               .setUri(new org.eclipse.jgit.transport.URIish(remoteUrl))
               .call();

            // 推送代码到远程仓库，并设置上游分支
            git.push()
               .setRemote("origin")
               .setPushAll()
               .setRefSpecs(new RefSpec(branchName + ":" + branchName))
               .setPushTags()
               .call();

            // 设置上游分支
            git.branchCreate()
               .setName(branchName)
               .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
               .setStartPoint("origin/" + branchName)
               .call();

        } catch (Exception e) {
            throw new RuntimeException("Failed to push changes to remote repository", e);
        }
    }

}