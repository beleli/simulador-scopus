package br.com.scopus.simulador.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.jerimum.fw.service.impl.AbstractCrudService;
import br.com.scopus.simulador.business.service.ProjectUserService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.ProjectUserDto;
import br.com.scopus.simulador.repository.ProjectUserRepository;
import br.com.scopus.simulador.repository.entity.Project;
import br.com.scopus.simulador.repository.entity.ProjectUser;
import br.com.scopus.simulador.repository.entity.User;

@Service
public class ProjectUserServiceImpl extends AbstractCrudService<ProjectUserDto, ProjectUser>
    implements ProjectUserService {

    @Autowired
    ProjectUserRepository projectUserRepository;

    @Override
    protected ProjectUserDto buildDtoFromEntity(ProjectUser entity) {
        return new ProjectUserDto(entity.getId(), entity.getUser().getId(), entity.getProject().getId());
    }

    @Override
    protected ProjectUser buildEntityFromDto(ProjectUserDto dto) {
        return new ProjectUser(dto.getId(), new Project(dto.getId()), new User(dto.getId()));
    }

    @Override
    protected JpaCrudRepository<ProjectUser, Long> getRepository() {
        return this.projectUserRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public void saveUserList(List<ComboBoxDto> dtos, Long projectId) {
        List<ProjectUser> users = projectUserRepository.findByProjectIdOrderByUserName(projectId);

        // remove os usuários da lista inicial
        for (ProjectUser user : users) {
            boolean find = false;
            for (ComboBoxDto dto : dtos) {
                if (dto.getId() == user.getUser().getId()) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                projectUserRepository.delete(user.getId());
            }
        }

        // adiciona os novos usuários da lista inicial
        for (ComboBoxDto dto : dtos) {
            boolean find = false;
            for (ProjectUser user : users) {
                if (dto.getId() == user.getUser().getId()) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                projectUserRepository.save(new ProjectUser(null, new Project(projectId), new User(dto.getId())));
            }
        }
    }

    @Override
    public List<ComboBoxDto> findUserList(Long projectId) {
        List<ProjectUser> userList = this.projectUserRepository.findByProjectIdOrderByUserName(projectId);
        List<ComboBoxDto> dtos = new ArrayList<>();
        for (ProjectUser user : userList) {
            dtos.add(new ComboBoxDto(user.getUser().getId(), user.getUser().getName()));
        }
        return dtos;
    }

    @Override
    public void deleteByProject(Long projectId) {
        List<ProjectUser> users = projectUserRepository.findByProjectIdOrderByUserName(projectId);
        for (ProjectUser user : users) {
            projectUserRepository.delete(user.getId());
        }
    }

    @Override
    public boolean isValidUserPermission(Long projectId, Long userId) {
        ProjectUser entity = projectUserRepository.findByProjectIdAndUserId(projectId, userId);
        return entity != null;
    }
}
