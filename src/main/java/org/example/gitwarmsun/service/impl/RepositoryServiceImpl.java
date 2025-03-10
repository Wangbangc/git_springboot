package org.example.gitwarmsun.service.impl;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.transport.RefSpec;
import org.example.gitwarmsun.model.Repository;
import org.example.gitwarmsun.service.RepositoryService;
import org.springframework.stereotype.Service;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.gitwarmsun.mapper.RepositoryMapper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Override
    public Repository initializeRepository(String repositoryName, String description) {
        // TODO: Implement repository initialization logic
        return null;
    }

    @Override
    public Repository createRepository(int userId, String username, String repositoryName, String description) {
        // 1. 生成仓库路径
        String repoPath = "D:\\test\\" + username + "\\" + repositoryName;
// 1. 使用正斜杠代替反斜杠D:\\test
        String repoPathWithForwardSlashes = repoPath.replace("\\", "/");

// 2. 确保盘符大写
        if (repoPathWithForwardSlashes.length() > 0 && Character.isLowerCase(repoPathWithForwardSlashes.charAt(0))) {
            repoPathWithForwardSlashes = Character.toUpperCase(repoPathWithForwardSlashes.charAt(0)) + repoPathWithForwardSlashes.substring(1);
        }

// 3. 构建 file:/// URL
        String fileUrl = "file:///" + repoPathWithForwardSlashes;
        // 2. 创建物理仓库
        try {
            // 创建bare仓库
//.setBare(true)
           Git git = Git.init().setDirectory(new File(repoPath)).call();
            git.remoteAdd()
                    .setName("origin")
                    .setUri(new org.eclipse.jgit.transport.URIish(fileUrl))
                    .call();


        } catch (GitAPIException e) {
            throw new RuntimeException("Failed to create repository", e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // 3. 保存元数据到数据库
        Repository repository = new Repository();
        repository.setName(repositoryName);
        repository.setDescription(description);
        repository.setOwnerId(userId);
        repository.setPath(repoPath);
        repositoryMapper.insertRepository(repository);

        return repository;
    }

    @Override
    public void deleteRepository(int repositoryId) {
        // 获取仓库详情，包括路径
        Repository repository = repositoryMapper.getRepositoryDetails(repositoryId);
        if (repository == null) {
            throw new RuntimeException("Repository not found");
        }

        // 删除物理仓库
        try {
            File repoDir = new File(repository.getPath());
            if (repoDir.exists()) {
                deleteDirectory(repoDir);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete physical repository", e);
        }

        // 删除数据库记录
        try {
            repositoryMapper.deleteRepository(repositoryId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete repository from database", e);
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    @Override
    public String cloneRepository(int repositoryId) {
        System.out.println("Cloning repository " + repositoryId);
        // 获取仓库详情，包括路径
        Repository repository = repositoryMapper.getRepositoryDetails(repositoryId);
        if (repository == null) {
            throw new RuntimeException("Repository not found");
        }

        // 获取浏览器默认下载路径
        String downloadPath = System.getProperty("user.home") + "\\Downloads\\" + repository.getName();

        // 克隆仓库到下载路径
        try {
            Git.cloneRepository()
                .setURI(repository.getPath())
                .setDirectory(new File(downloadPath))
                .call();
        } catch (GitAPIException e) {
            throw new RuntimeException("Failed to clone repository", e);
        }

        return downloadPath;
    }

    @Override
    public void commitChanges(int repositoryId, String message, String filePaths) {
        // 获取仓库详情，包括路径
        Repository repository = repositoryMapper.getRepositoryDetails(repositoryId);
        if (repository == null) {
            throw new RuntimeException("Repository not found");
        }
        // 打开仓库
        String branchName = "master";
        try (Git git = Git.open(new File(filePaths))) {
            File f = new File(filePaths);
            if(f.isDirectory()){
                for(File file :f.listFiles()){
                    String relativePath = file.getAbsolutePath().substring(filePaths.length() + 1);
                    git.add().addFilepattern(relativePath).call();
                }
            }else{
                String relativePath = filePaths.substring(filePaths.length() + 1);
                git.add().addFilepattern(relativePath).call();
            }
            // 提交更改
            git.commit()
                .setMessage(message)
                .call();
            // 推送代码到远程仓库
            git.push()
                .setRemote("origin")
                .setPushAll()
                .setRefSpecs(new RefSpec(branchName + ":" + branchName))
                .setPushTags()
                .call();
System.out.println(repository.getPath());
            File bareDir = new File(repository.getPath());
            Git bareGit = Git.open(bareDir);
            bareGit.pull().setRemote("origin").call();
            bareGit.reset().setMode(ResetCommand.ResetType.HARD).setRef("origin/master").call();


        } catch (IOException | GitAPIException e) {
            throw new RuntimeException("Failed to commit or push changes", e);
        }
    }

    @Override
    public void pullLatestCode(int repositoryId) {
        // TODO: Implement pull latest code logic
    }

    //获取所有仓库信息
    @Override
    public List<Repository> listAllRepositories(int userId) {

        return repositoryMapper.listAllRepositories(userId);
    }

    @Override
    public Repository getRepositoryDetails(int repositoryId) {
        // 从数据库获取仓库详情
        Repository repository = repositoryMapper.getRepositoryDetails(repositoryId);
        if (repository == null) {
            throw new RuntimeException("Repository not found");
        }

        // 获取仓库路径
        String repoPath = repository.getPath();
        File repoDir = new File(repoPath);

        // 检查路径是否存在
        if (!repoDir.exists() || !repoDir.isDirectory()) {
            throw new RuntimeException("为查找到该仓库");
        }

        // 获取文件列表
        File[] files = repoDir.listFiles();
        if (files != null) {
            // 将文件列表存储到仓库对象中
            repository.setFiles(files);
        }

        return repository;
    }
}
