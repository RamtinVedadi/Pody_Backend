package com.pody.service.managers;

import com.pody.dto.requests.IdStringRequestDto;
import com.pody.dto.requests.StringRequestDto;
import com.pody.dto.requests.TwoIDRequestDto;
import com.pody.dto.responses.IdResponseDto;
import com.pody.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UserManager {

    ResponseEntity read(TwoIDRequestDto dto);

    ResponseEntity update(User user, UUID id);

    ResponseEntity delete(UUID id);

    ResponseEntity isPremium(UUID id);

    ResponseEntity uploadProfileImage(MultipartFile image, UUID id);

    ResponseEntity uploadChannelImage(MultipartFile image, UUID id);

    ResponseEntity checkEmail(StringRequestDto dto);

    ResponseEntity checkUsername(StringRequestDto dto);

    ResponseEntity resetPassword(IdStringRequestDto dto);

    ResponseEntity findUserForResetPassword(StringRequestDto dto);

    ResponseEntity listChannels(IdResponseDto dto);

    ResponseEntity updateIsChannel(IdResponseDto dto);

    ResponseEntity userListSubscriptions(UUID id);
}
