package org.example.gitwarmsun.controller;

import jakarta.annotation.security.PermitAll;
import org.example.gitwarmsun.dto.ResponseDTO;
import org.example.gitwarmsun.model.Repository;
import org.example.gitwarmsun.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repositories")
public class RepositoryController {
    @Autowired
    private RepositoryService repositoryService;


    /**
     * 推送代码到远程仓库 - 已完成
     */
    @PostMapping("/{repositoryId}/push")
    public ResponseDTO<Void> pushToRemote(@PathVariable int repositoryId, @RequestParam String remoteUrl, @RequestParam String branchName) {
        repositoryService.pushToRemote(repositoryId, remoteUrl, branchName);
        return new ResponseDTO<>(200, "Changes pushed to remote repository successfully", null);
    }

}