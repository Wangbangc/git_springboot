package org.example.gitwarmsun.controller;

import jakarta.annotation.security.PermitAll;
import org.example.gitwarmsun.dto.ResponseDTO;
import org.example.gitwarmsun.model.Repository;
import org.example.gitwarmsun.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RepositoryController 处理仓库相关的请求
 */
@RestController
@RequestMapping("/api/repositories")
public class RepositoryController {
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 初始化仓库 - 已完成
     */
    @PostMapping("/initialize")
    public ResponseDTO<Repository> initializeRepository(@RequestParam String repositoryName, @RequestParam(required = false) String description) {
        Repository repository = repositoryService.initializeRepository(repositoryName, description);
        return new ResponseDTO<>(200, "Repository initialized successfully", repository);
    }

    /**
     * 创建仓库 - 已完成1
     */
    @PostMapping("/create")
    @PermitAll
    public ResponseDTO<Repository> createRepository(@RequestParam int userId, @RequestParam String username,
                                       @RequestParam String repositoryName, @RequestParam String description) {
        return new ResponseDTO<>(200, "Repository deleted successfully", repositoryService.createRepository(userId, username, repositoryName, description));
    }

    /**
     * 删除仓库 - 已完成1
     */
    @DeleteMapping("/delete")
    public ResponseDTO<Void> deleteRepository(@RequestParam int repositoryId) {
        repositoryService.deleteRepository(repositoryId);
        return new ResponseDTO<>(200, "Repository deleted successfully", null);
    }

    /**
     * 克隆仓库 - 已完成1
     */
    @PostMapping("/clone")
    public ResponseDTO<String> cloneRepository(@RequestParam int repositoryId) {
        String result = repositoryService.cloneRepository(repositoryId);
        return new ResponseDTO<>(200, "Repository cloned successfully", result);
    }

    /**
     * 提交更改 - 已完成1
     */
    @PostMapping("/{repositoryId}/commit")
    public ResponseDTO<Void> commitChanges(@PathVariable int repositoryId, @RequestParam String message, @RequestParam("filePaths") String filePaths) {
        repositoryService.commitChanges(repositoryId, message, filePaths);
        return new ResponseDTO<>(200, "Changes committed successfully", null);
    }

    /**
     * 拉取最新代码 - 已完成
     */
    @PostMapping("/{repositoryId}/pull")
    public ResponseDTO<Void> pullLatestCode(@PathVariable int repositoryId) {
        repositoryService.pullLatestCode(repositoryId);
        return new ResponseDTO<>(200, "Latest code pulled successfully", null);
    }

    /**
     * 列出所有仓库 - 已完成1
     */

    @GetMapping("/list")
    public ResponseDTO<List<Repository>> listAllRepositories() {
        try {
            // 假设已经解析请求头获取了用户ID为1
            int userId = 1;
            List<Repository> repositories = repositoryService.listAllRepositories(userId);
            return new ResponseDTO<>(200, "Changes committed successfully", repositories);
        } catch (Exception e) {
            return new ResponseDTO<>(500, "Failed to retrieve repositories", null);
        }
    }

    /**
     * 获取仓库详情 - 已完成
     */
    @GetMapping("/{repositoryId}")
    public ResponseDTO<Repository> getRepositoryDetails(@PathVariable int repositoryId) {
        Repository repository = repositoryService.getRepositoryDetails(repositoryId);
        return new ResponseDTO<>(200, "Repository details retrieved successfully", repository);
    }
}