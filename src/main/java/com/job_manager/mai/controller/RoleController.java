package com.job_manager.mai.controller;

import com.job_manager.mai.contrains.Permission;
import com.job_manager.mai.controller.base.BaseController;
import com.job_manager.mai.controller.inteface.IBaseController;
import com.job_manager.mai.request.role.*;
import com.job_manager.mai.service.role.RoleService;
import com.job_manager.mai.util.ApiResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin
public class RoleController implements IBaseController<RoleRequest, CreateRoleRequest, UpdateRoleRequest, DeleteRoleRequest, Long> {

    private final BaseController baseController;
    private final RoleService roleService;

    @PostMapping
    @Override
    public ResponseEntity<?> add(@Valid @RequestBody CreateRoleRequest requestBody) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_CREATE);
            return roleService.store(requestBody);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@Valid @RequestBody UpdateRoleRequest requestBody, @PathVariable(name = "id") Long Id) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_UPDATE);
            return roleService.edit(requestBody, Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long Id) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_DELETE);
            return roleService.destroy(Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<?> deleteAll(@Valid @RequestBody DeleteRoleRequest request) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_DELETE);
            return roleService.destroyAll(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long aLong) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_READ);
            return roleService.getById(aLong);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/add-perm/{id}")
    public ResponseEntity<?> addPermToRole(@PathVariable(name = "id") long id, @RequestBody ManagePermRequest request) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_UPDATE);
            return roleService.addPerm(id, request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/remove-perm/{id}")
    public ResponseEntity<?> removePermOutRole(@PathVariable(name = "id") long id, @RequestBody ManagePermRequest request) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_UPDATE);
            return roleService.removePerm(id, request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_READ);
            return roleService.getAll(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> search(String query) {
        return null;
    }

    @GetMapping("/search/by-name")
    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, @RequestParam(name = "query") String name) {
        try {
            baseController.processPermission(Permission.MANAGE_ROLE_READ);
            return roleService.searchByName(pageable, name);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> sortByName(String name) {
        return null;
    }
}
