package com.job_manager.mai.controller;

import com.job_manager.mai.controller.inteface.IBaseController;
import com.job_manager.mai.request.room.*;
import com.job_manager.mai.service.message.MessageService;
import com.job_manager.mai.service.room.RoomService;
import com.job_manager.mai.util.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Room")
public class RoomController implements IBaseController<RoomRequest, CreateRoomRequest, UpdateRoomRequest, DeleteRoomRequest, String> {

    private final MessageService messageService;
    private final RoomService roomService;

    @Operation(description = "API to send a message", responses = {
            @ApiResponse(
                    description = "Success",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "Failed",
                    responseCode = "400",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "Unauthorized",
                    responseCode = "403",
                    useReturnTypeSchema = true

            )
    })
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody UserMessageRequest userMessageRequest) {
        try {
            return messageService.store(userMessageRequest);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/by-member/{memberId}")
    public ResponseEntity<?> getMemberRooms(@PathVariable(name = "memberId") String memberId) {
        try {
            return roomService.getByMember(memberId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<?> getMessageByRoom(@PathVariable(name = "roomId") String roomId, Pageable pageable) {
        try {
            return roomService.getMessageById(pageable, roomId);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getAllRoomTag() {
        try {
            return roomService.getAllTag();
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping
    @Override
    public ResponseEntity<?> add(@RequestBody CreateRoomRequest requestBody) {
        try {
            return roomService.store(requestBody);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<?> update(UpdateRoomRequest requestBody, @PathVariable(name = "id") String Id) {
        try {
            return roomService.edit(requestBody, Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable(name = "id") String Id) {
        try {
            return roomService.destroy(Id);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @DeleteMapping
    @Override
    public ResponseEntity<?> deleteAll(@RequestBody DeleteRoomRequest request) {
        try {
            return roomService.destroyAll(request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getById(@PathVariable(name = "id") String s) {
        try {
            return roomService.getById(s);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @GetMapping
    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        try {
            return roomService.getAll(pageable);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override
    public ResponseEntity<?> search(@PathVariable(name = "query") String query) {
        return null;
    }

    @GetMapping("/search/{name}")
    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, @PathVariable(name = "name") String name) {
        try {
            return roomService.searchByName(pageable, name);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @PostMapping("/add-member/{roomId}")
    public ResponseEntity<?> addMemberToRoom(@PathVariable(name = "roomId") String roomId, @RequestBody AddMemberToRoomRequest request) {
        try {
            return roomService.addMember(roomId, request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }
    @PostMapping("/remove-member/{roomId}")
    public ResponseEntity<?> removeMemberToRoom(@PathVariable(name = "roomId") String roomId, @RequestBody AddMemberToRoomRequest request) {
        try {
            return roomService.removeMember(roomId, request);
        } catch (Exception e) {
            return ApiResponseHelper.fallback(e);
        }
    }

    @Override

    public ResponseEntity<?> sortByName(String name) {
        return null;
    }
}
