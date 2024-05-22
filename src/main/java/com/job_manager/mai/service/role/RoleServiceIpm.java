package com.job_manager.mai.service.role;

import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.exception.CannotAction;
import com.job_manager.mai.exception.PermissionNotFound;
import com.job_manager.mai.exception.RoleExists;
import com.job_manager.mai.model.Permission;
import com.job_manager.mai.model.Role;
import com.job_manager.mai.repository.PermissionRepository;
import com.job_manager.mai.repository.RoleRepository;
import com.job_manager.mai.request.role.CreateRoleRequest;
import com.job_manager.mai.request.role.DeleteRoleRequest;
import com.job_manager.mai.request.role.ManagePermRequest;
import com.job_manager.mai.request.role.UpdateRoleRequest;
import com.job_manager.mai.response.role.IRoleResponser;
import com.job_manager.mai.response.role.RoleResponse;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.util.ApiResponseHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoleServiceIpm extends BaseService implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final IRoleResponser iRoleResponser;

    @Override
    public ResponseEntity<?> store(CreateRoleRequest request) throws Exception {
        if (existsByName(request.getRoleName())) {
            throw new RoleExists(Messages.ROLE_EXISTS);
        }
        Role newRole = getMapper().map(request, Role.class);
        newRole.setCanAction(true);
        return addPermToRole(newRole, request.getPermissionIds());
    }

    private boolean existsByName(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

    private Role findById(long id) throws Exception {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(Messages.ROLE_NOT_FOUND));
    }

    private List<Role> findAllByIds(Set<Long> ids) {
        return roleRepository.findAllById(ids);
    }

    @Override
    public ResponseEntity<?> edit(UpdateRoleRequest request, Long aLong) throws Exception {
        Role role = findById(aLong);

        if (!role.isCanAction()) {
            throw new CannotAction(Messages.ROLE_CANNOT_ACTION);
        }
        BeanUtils.copyProperties(request, role, getNullPropertyNames(request));

        return addPermToRole(role, request.getPermissionIds());
    }

    private ResponseEntity<?> addPermToRole(Role role, Set<Long> permissionIds) throws PermissionNotFound {
        if (!permissionIds.isEmpty()) {
            for (long id : permissionIds) {
                Permission permission = permissionRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(Messages.PERMISSION_NOTFOUND));
                if (!permission.isActive()) {
                    throw new PermissionNotFound(Messages.PERMISSION_NOTFOUND);
                }
                role.addPerm(permission);
            }
        }
        return ApiResponseHelper.success(iRoleResponser.mapTo(roleRepository.saveAndFlush(role)));
    }

    @Override
    public ResponseEntity<?> destroy(Long aLong) throws Exception {
        Role role = findById(aLong);
        if (!role.isCanAction()) {
            throw new CannotAction(Messages.ROLE_CANNOT_ACTION);
        }
        roleRepository.deleteRolePerm(role.getId());
        roleRepository.delete(role);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> destroyAll(DeleteRoleRequest request) throws Exception {
        List<Role> roles = findAllByIds(request.getRoleIds());
        roles.forEach((r) -> {
            if (r.isCanAction()) {
                roleRepository.deleteRolePerm(r.getId());
                roleRepository.delete(r);
            }
        });
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        Page<RoleResponse> responses = iRoleResponser.mapTo(roleRepository.findAll(pageable));
        return ApiResponseHelper.success(responses);
    }

    @Override
    public ResponseEntity<?> getById(Long aLong) throws Exception {
        return ApiResponseHelper.success(iRoleResponser.mapTo(findById(aLong)));
    }

    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) throws Exception {
        return ApiResponseHelper.success(roleRepository.findAllByRoleNameContaining(pageable, name));
    }

    @Override
    public ResponseEntity<?> SearchAndSortByProperties(Pageable pageable, String searchProperties, String sortProperties) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<?> addPerm(long id, ManagePermRequest request) throws Exception {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(Messages.ROLE_NOT_FOUND));
        request.getIds().forEach((permId) -> {
            if (!role.getPermissions().contains(permissionRepository.findById(permId).orElse(null))) {
                role.addPerm(permissionRepository.findById(permId).orElse(null));
            }
        });
        roleRepository.save(role);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> removePerm(long id, ManagePermRequest request) throws Exception {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(Messages.ROLE_NOT_FOUND));
        request.getIds().forEach((permId) -> {
            role.removePerm(permissionRepository.findById(permId).orElse(null));
        });
        roleRepository.save(role);
        return ApiResponseHelper.success();
    }
}
