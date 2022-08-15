package org.datapool.services;

import org.datapool.db.Project;
import org.datapool.db.ProjectJpaRepository;
import org.datapool.db.UserRepository;
import org.datapool.dto.CreateProjectRs;
import org.datapool.dto.ErrorMessage;
import org.datapool.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {
    @Autowired
    private ProjectJpaRepository projectJpaRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Project> getProjectByUser(long ownerId){
        List<Project> projects = projectJpaRepository.findByOwnerId(ownerId);
        return projects;
    }

    public Project getProject(String projectId){
        Optional<Project> project = Optional.of(projectJpaRepository.getById(projectId));
        if (project.isPresent()) return project.get();
        else throw new NotFoundException("project not found");
    }

    public void deleteProject(Project project){
        projectJpaRepository.delete(project);
    }

    public CreateProjectRs createNewProject(long ownerId, CreateProjectRs request){
        try {
            if(!checkProjectExist(ownerId, request.getName())){
                Project project = new Project();
                project.setDescription(request.getDescription());
                project.setName(request.getName());
                project.setOwnerId(ownerId);
                project.setId(UUID.randomUUID().toString());
                projectJpaRepository.save(project);
                request.setErrorMessage(new ErrorMessage("no errors"));
                request.setResult(Result.SUCCESS);
                return request;
            } else {
                request.setErrorMessage(new ErrorMessage("project with this name already exist"));
                request.setResult(Result.ERROR);
                return request;
            }
        } catch (Exception e){
            e.printStackTrace();
            request.setErrorMessage(new ErrorMessage("internal error. Go to log."));
            request.setResult(Result.ERROR);
            return request;
        }
    }

    public boolean checkProjectExist(long ownerId, String name){
        Optional<Project> project = projectJpaRepository.findByOwnerIdAndName(ownerId, name);
        return project.isPresent();
    }

    public boolean checkProjectPermissionById(long ownerId, String projectId){
        Optional<Project> project = projectJpaRepository.findByOwnerIdAndId(ownerId, projectId);
        return project.isPresent();
    }
}
