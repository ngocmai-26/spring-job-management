package com.job_manager.mai.service.permission;

import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.exception.PermissionNotFound;
import com.job_manager.mai.exception.VerifyErrorException;
import com.job_manager.mai.model.Permission;
import com.job_manager.mai.model.Role;
import com.job_manager.mai.repository.PermissionRepository;
import com.job_manager.mai.request.permission.CreatePermRequest;
import com.job_manager.mai.request.permission.DeletePermRequest;
import com.job_manager.mai.request.permission.UpdatePermRequest;
import com.job_manager.mai.response.permission.IPermissionResponser;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.util.ApiResponseHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionServiceIpm extends BaseService implements PermissionService {
    private final PermissionRepository permissionRepository;

    private final IPermissionResponser iPermissionResponser;

    @Override
    public ResponseEntity<?> store(CreatePermRequest permissionRequest) throws Exception {
        boolean exited = permissionRepository.findByName(permissionRequest.getName()).isPresent();
        if (exited) {
            return ApiResponseHelper.resp(null, HttpStatus.BAD_REQUEST, Messages.PERMISSION_EXITED);
        }
        Permission permission = getMapper().map(permissionRequest, Permission.class);
        permission.setCanDelete(true);
        permission.setActive(true);
        return ApiResponseHelper.resp(permissionRepository.saveAndFlush(permission), HttpStatus.OK, Messages.DEFAULT_SUCCESS_MESSAGE);
    }

    @Override
    public ResponseEntity<?> edit(UpdatePermRequest permissionRequest, Long aLong) throws Exception {
        Permission permission = permissionRepository.findById(aLong).orElseThrow(() -> new PermissionNotFound(Messages.PERMISSION_NOTFOUND));
        if (!permission.isActive()) {
            throw new PermissionNotFound(Messages.PERMISSION_NOTFOUND);
        }
        permission.setDescription(permissionRequest.getDescription());
        return ApiResponseHelper.resp(permissionRepository.saveAndFlush(permission), HttpStatus.OK, Messages.DEFAULT_SUCCESS_MESSAGE);
    }

    @Override
    public ResponseEntity<?> destroy(Long aLong) throws Exception {
        Permission permission = permissionRepository.findById(aLong).orElseThrow(() -> new PermissionNotFound(Messages.PERMISSION_NOTFOUND));
        if (!permission.isCanDelete()) {
            throw new VerifyErrorException(Messages.CANNOT_DELETE_PERM);
        }
        if (!permission.isActive()) {
            throw new PermissionNotFound(Messages.PERMISSION_NOTFOUND);
        }
        permission.setActive(false);
        permissionRepository.save(permission);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> destroyAll(DeletePermRequest permissionRequest) throws Exception {
        permissionRequest.getDeleteIds().forEach((perm) -> {
            Permission permission = permissionRepository.findById(perm).orElse(null);
            if (permission != null && permission.isActive() && permission.isCanDelete()) {
                permission.setActive(false);
                permissionRepository.save(permission);
            }
        });
        return ApiResponseHelper.success();
    }


    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ApiResponseHelper.success(iPermissionResponser.mapTo(permissionRepository.findAll(pageable)));
    }

    @Override
    public ResponseEntity<?> getById(Long aLong) throws Exception {
        Permission permission = permissionRepository.findById(aLong).orElseThrow(() -> new PermissionNotFound(Messages.PERMISSION_NOTFOUND));
        if (!permission.isActive()) {
            throw new PermissionNotFound(Messages.PERMISSION_NOTFOUND);
        }
        return ApiResponseHelper.success(iPermissionResponser.mapTo(permission));
    }

    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) throws Exception {
        return ApiResponseHelper.success(permissionRepository.findByNameContaining(pageable, name));
    }

    @Override
    public ResponseEntity<?> SearchAndSortByProperties(Pageable pageable, String searchProperties, String sortProperties) throws Exception {
        return null;
    }
}
