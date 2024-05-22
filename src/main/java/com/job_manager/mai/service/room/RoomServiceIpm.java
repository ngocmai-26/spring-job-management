package com.job_manager.mai.service.room;

import com.job_manager.mai.contrains.MessageStatus;
import com.job_manager.mai.contrains.MessageType;
import com.job_manager.mai.contrains.Messages;
import com.job_manager.mai.contrains.NotificationType;
import com.job_manager.mai.exception.RoomExitOnLeaderException;
import com.job_manager.mai.exception.UserNotFoundException;
import com.job_manager.mai.model.Message;
import com.job_manager.mai.model.Notification;
import com.job_manager.mai.model.Room;
import com.job_manager.mai.model.User;
import com.job_manager.mai.repository.MessageRepository;
import com.job_manager.mai.repository.RoomRepository;
import com.job_manager.mai.repository.RoomTagRepository;
import com.job_manager.mai.repository.UserRepository;
import com.job_manager.mai.request.notification.CreateNotificationRequest;
import com.job_manager.mai.request.room.AddMemberToRoomRequest;
import com.job_manager.mai.request.room.CreateRoomRequest;
import com.job_manager.mai.request.room.DeleteRoomRequest;
import com.job_manager.mai.request.room.UpdateRoomRequest;
import com.job_manager.mai.response.room.RoomResponse;
import com.job_manager.mai.service.base.BaseService;
import com.job_manager.mai.service.notification.NotificationService;
import com.job_manager.mai.util.ApiResponseHelper;
import com.job_manager.mai.util.SecurityHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoomServiceIpm extends BaseService implements RoomService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final RoomTagRepository roomTagRepository;

    private final NotificationService notificationService;

    @Override
    public ResponseEntity<?> store(CreateRoomRequest request) throws Exception {
        User user = userRepository.findByEmailOrPhone(SecurityHelper.getLoggedUser()).orElseThrow(() -> new UserNotFoundException(Messages.USER_NOT_FOUND));
        List<Room> exits = roomRepository.findAllByRoomNameAndLeader(request.getRoomName(), user);
        if (!exits.isEmpty()) {
            return ApiResponseHelper.fallback(new RoomExitOnLeaderException("Tên nhóm đã tồn tại"));
        }
        Room room = getMapper().map(request, Room.class);
        // get current logged user
        room.setId(UUID.randomUUID().toString());
        room.setLeader(user);
        Room savedRoom = roomRepository.save(room);
        request.getInitMember().forEach((m) -> {
            User member = userRepository.findById(m).orElse(null);
            if (member != null) {
                savedRoom.addMember(member);
                Message message = new Message();
                message.setContent(String.format("%s được %s thêm vào nhóm", member.getFullName(), room.getLeader().getFullName()));
                message.setMessageTypeInRoom(MessageType.NOTIFICATION_MESSAGE.getVal());
                message.setSender(null);
                message.setRoom(savedRoom);
                messageRepository.save(message);
                // push notification
                CreateNotificationRequest notification = new CreateNotificationRequest();
                notification.setTo(member);
                notification.setFrom(user);
                notification.setContent(String.format("%s đã thêm bạn vào nhóm", user.getFullName()));
                notification.setType(NotificationType.TYPE_CREATE_ROOM.getValue());
                try {
                    notificationService.createAndPushNotification(notification);
                } catch (Exception e) {
                    log.error(e.getMessage());
                } finally {
                    System.out.println("Pushed notification");
                }
            }
        });
        request.getRoomTagsId().forEach((tagId) -> {
            savedRoom.addTag(roomTagRepository.findById(tagId).orElse(null));
        });
        return ApiResponseHelper.success(roomRepository.saveAndFlush(savedRoom));
    }

    @Override
    public ResponseEntity<?> edit(UpdateRoomRequest request, String s) throws Exception {
        Room room = getMapper().map(request, Room.class);
        request.getInitMember().forEach((m) -> {
            room.addMember(userRepository.findById(m).orElse(null));
        });
        return ApiResponseHelper.success(roomRepository.saveAndFlush(room));
    }

    @Override
    public ResponseEntity<?> destroy(String s) throws Exception {
        return getResponseEntity(s);
    }

    @Override
    public ResponseEntity<?> destroyAll(DeleteRoomRequest request) throws Exception {
        for (String id : request.getDeleteIds()) {
            return getResponseEntity(id);
        }
        return ApiResponseHelper.success();
    }

    private ResponseEntity<?> getResponseEntity(String id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Room not found"));
        room.setLeader(null);
        room.setSubLeader(null);
        room.setMembers(new HashSet<>());
        room.setBannedUser(new HashSet<>());
        room.setMedia(new HashSet<>());
        room.setTags(new HashSet<>());
        roomRepository.saveAndFlush(room);
        roomRepository.delete(room);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> getAll(Pageable pageable) {
        return ApiResponseHelper.success(roomRepository.findAll(pageable));
    }

    @Override
    public ResponseEntity<?> getById(String s) throws Exception {
        return ApiResponseHelper.success(roomRepository.findById(s));
    }

    @Override
    public ResponseEntity<?> searchByName(Pageable pageable, String name) throws Exception {
        return ApiResponseHelper.success(roomRepository.findByRoomNameContaining(pageable, name));
    }

    @Override
    public ResponseEntity<?> SearchAndSortByProperties(Pageable pageable, String searchProperties, String
            sortProperties) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<?> getByMember(String memberId) {
        List<Room> rooms = roomRepository.findAll();
        User user = userRepository.findById(memberId).orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        Set<RoomResponse> roomByMember = new HashSet<>();
        PageRequest page = PageRequest.of(0, 50);
        for (Room room : rooms) {
            if (room.getMembers().contains(user)) {
                RoomResponse response = getMapper().map(room, RoomResponse.class);
                Message messages = messageRepository.findFirstByRoomOrderBySentAtDesc(room);
                Page<Message> allMessage = messageRepository.findAllByRoomOrderBySentAtDesc(page, room);
                response.setLastMessage(messages);
                response.setAllMessages(allMessage);
                roomByMember.add(response);
            }
        }
        return ApiResponseHelper.success(roomByMember);
    }

    private List<Message> mapMessages(Set<Message> messages) {
        return messages.stream().map((msg) -> {
            if (msg.getStatus() == MessageStatus.DISPLAY.getVal()) {
                return msg;
            }
            return null;
        }).collect(Collectors.toList());
    }

    private Page<Message> getMessageByPage(Pageable pageable, List<Message> messages) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), messages.size());
        return new PageImpl<>(messages.subList(start, end), pageable, messages.size());
    }

    @Override
    public ResponseEntity<?> getMessageById(Pageable pageable, String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new UsernameNotFoundException(Messages.ROOM_NOT_FOUND));
        Page<Message> messages = messageRepository.findAllByRoomOrderBySentAtDesc(pageable, room);
        return ApiResponseHelper.success(messages);
    }

    @Override
    public ResponseEntity<?> getAllByUser(Pageable pageable, String userId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getAllTag() {
        return ApiResponseHelper.success(roomTagRepository.findAll());
    }

    @Override
    public ResponseEntity<?> addMember(String roomId, AddMemberToRoomRequest request) throws Exception {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new Exception("Room not found"));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new Exception(Messages.USER_EXITED));
        User adder = userRepository.findById(request.getAdderId()).orElseThrow(() -> new Exception(Messages.USER_EXITED));
        if (room.getMembers().contains(user)) {
            return ApiResponseHelper.fallback(new Exception("User has exists in room"));

        }
        room.addMember(user);
        Message message = new Message();
        message.setSender(adder);
        message.setMessageTypeInRoom(MessageType.NOTIFICATION_MESSAGE.getVal());
        message.setContent(String.format("%s vừa được %s thêm vào nhóm", user.getFullName(), adder.getFirstName()));
        message.setSentAt(LocalDateTime.now());
        message.setStatus(MessageStatus.DISPLAY.getVal());
        message.setRoom(room);
        messageRepository.save(message);
        simpMessagingTemplate.convertAndSend("/room", message);
        roomRepository.save(room);
        return ApiResponseHelper.success();
    }

    @Override
    public ResponseEntity<?> removeMember(String roomId, AddMemberToRoomRequest request) throws Exception {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new Exception("Room not found"));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new Exception(Messages.USER_EXITED));
        User adder = userRepository.findById(request.getAdderId()).orElseThrow(() -> new Exception(Messages.USER_EXITED));
        if (!room.getMembers().contains(user)) {
            return ApiResponseHelper.fallback(new Exception("User hasn't exists in room"));

        }
        if (user.getId().equals(adder.getId())) {
            return ApiResponseHelper.fallback(new Exception("Cannot remove your self"));
        }
        if (room.getLeader().getId().equals(user.getId())) {
            return ApiResponseHelper.fallback(new Exception("You are leader and cannot remove"));
        }
        room.removeMember(user);

        Message message = new Message();
        message.setSender(adder);
        message.setMessageTypeInRoom(MessageType.NOTIFICATION_MESSAGE.getVal());
        message.setContent(user.getFullName() + " đã bị " + adder.getFirstName() + " xóa khỏi nhóm");
        message.setSentAt(LocalDateTime.now());
        message.setStatus(MessageStatus.DISPLAY.getVal());
        message.setRoom(room);
        messageRepository.save(message);
        simpMessagingTemplate.convertAndSend("/room", message);
        roomRepository.save(room);
        return ApiResponseHelper.success();
    }
}
