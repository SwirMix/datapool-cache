package org.datapool;

import org.datapool.core.cache.CacheMetadata;
import org.datapool.core.jwt.TokenObject;
import org.datapool.db.Project;
import org.datapool.dto.CreateProjectRq;
import org.datapool.dto.CreateProjectRs;
import org.datapool.dto.ErrorMessage;
import org.datapool.dto.Result;
import org.datapool.services.DatapoolManager;
import org.datapool.services.ProjectService;
import org.datapool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private DatapoolManager datapoolManager;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/")
    public ResponseEntity getYoursProjects(
            @RequestHeader String token
    ) {
        TokenObject tokenData = userService.decryptToken(token);
        projectService.getProjectByUser(tokenData.getUserId());
        return new ResponseEntity(projectService.getProjectByUser(tokenData.getUserId()), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity getYoursProjects(
            @RequestHeader String token,
            @PathVariable String projectId

    ) {
        TokenObject tokenData = userService.decryptToken(token);
        Project project = projectService.getProject(projectId);
        if (project.getOwnerId() == tokenData.getUserId()){
            List<CacheMetadata> caches = datapoolManager.getMetadataCaches();
            if (project!=null && !project.equals("")){
                for (CacheMetadata cacheMetadata : caches){
                    if (cacheMetadata.getBaseProject().equals(project.getId())){
                        datapoolManager.deleteCacheData(cacheMetadata.getCacheName());
                    }
                }
                projectService.deleteProject(project);
                return new ResponseEntity(new ErrorMessage("project was deleted"), HttpStatus.OK);
            } else {
                return new ResponseEntity(caches, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity(new ErrorMessage("permission denied"), HttpStatus.FORBIDDEN);
        }

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/create")
    public ResponseEntity createNewProject(
            @RequestBody CreateProjectRq request,
            @RequestHeader String token
    ){
        TokenObject tokenData = null;
        CreateProjectRs response = new CreateProjectRs();
        response.setDescription(request.getDescription());
        response.setName(request.getName());
        try {
            tokenData = userService.decryptToken(token);
        } catch (Exception e){
            return new ResponseEntity(new ErrorMessage("invalid token"), HttpStatus.FORBIDDEN);
        }
        response = projectService.createNewProject(tokenData.getUserId(), response);
        if (response.getResult().equals(Result.SUCCESS)) {
            return new ResponseEntity(request, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(request, HttpStatus.BAD_REQUEST);
        }
    }
}
